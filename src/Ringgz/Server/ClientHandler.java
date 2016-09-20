package Ringgz.Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import Ringgz.Spel.Spel;
import Ringgz.Spel.Speler;
import Ringgz.protocol.ClientHandlerProtocol;
import Ringgz.protocol.ClientProtocol;
import Ringgz.protocol.Error;

public class ClientHandler extends Thread {

	private Server server;
	private Socket sock;
	private BufferedReader in;
	private BufferedWriter out;
	private int aantalspelers;
	public boolean playing = false;
	public boolean toevoegen = false;
	private static final String DELIM = ClientProtocol.DELIM;
	private HashMap<Integer, String> errors;
	private HashMap<String, Integer> commandoAantal;
	private Set<String> commandos;

	/**
	 * Construeert een ClientHandler object. Initialiseert de beide Data
	 * streams.
	 * 
	 * @require server != null && sock != null
	 */
	public ClientHandler(Server server, Socket sock) throws IOException {
		this.sock = sock;
		this.server = server;
		in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		out = new BufferedWriter(new PrintWriter(sock.getOutputStream()));

		// /errorLijst
		errors = new HashMap<Integer, String>();
		Error[] errorArray = { Error.INVALID_MOVE, Error.INVALID_NAME,
				Error.INVALID_PLAYERS, Error.PLAYER_LEFT,
				Error.UNKNOWN_COMMAND, Error.GAME_OVER, Error.DEFAULT };
		for (Error e : errorArray) {
			errors.put(e.value(), e.toString());
		}

		// / Commando Lijst
		commandoAantal = new HashMap<String, Integer>();
		commandoAantal.put(ClientProtocol.DO_MOVE, 4);
		commandoAantal.put(ClientProtocol.JOIN, 3);
		commandoAantal.put(ClientHandlerProtocol.ERROR, 3);
		commandoAantal.put(ClientProtocol.SEND_MESSAGE, 2);
		commandos = commandoAantal.keySet();

	}

	public void setPlaying(boolean connected) {
		this.playing = connected;
	}

	public Socket getSocket() {
		return sock;
	}

	public int aantalSpelers() {
		return aantalspelers;
	}

	/**
	 * Deze methode zorgt voor het doorzenden van berichten van de Client. Aan
	 * elk berichtje dat ontvangen wordt, wordt de naam van de Client toegevoegd
	 * en het nieuwe berichtje wordt ter broadcast aan de Server aangeboden. Als
	 * er bij het lezen van een bericht een IOException gegooid wordt,
	 * concludeert de methode dat de socketverbinding is verbroken en wordt
	 * shutdown() aangeroepen.
	 */
	public void run() {
		String input = null;
		do {
			try {
				input = in.readLine();
				server.addMessage(input);

			} catch (IOException e) {
				input = null;
			}
			if (input != null) {
				doCommando(input);
			} else
				shutdown();
		} while (!sock.isClosed() && input != null);
	}

	public void doCommando(String input) {
		String[] commandoArray = input.split(ClientProtocol.DELIM);

		if (commandoAantal.get(commandoArray[0]) != null
				&& commandoArray.length == commandoAantal.get(commandoArray[0])) {
			// Do_move
			if (commandoArray[0].equals(ClientProtocol.DO_MOVE)) {
				doMove(commandoArray);
				// Join
			} else if (commandoArray[0].equals(ClientProtocol.JOIN)) {
				join(commandoArray);

			} else if (commandoArray[0].equals(ClientHandlerProtocol.ERROR)) {
				errorHandle(commandoArray);
			} else if(commandoArray[0].equals(ClientProtocol.SEND_MESSAGE)){
				server.broadcast(ClientHandlerProtocol.BROADCAST + DELIM + getName() + DELIM + commandoArray[1]);
			}

		} else if (commandos.contains(commandoArray[0])) {
			sendMessage(ClientProtocol.ERROR + DELIM + Error.DEFAULT.value() + DELIM + "Er zijn te weinig  parameters");
			server.addMessage("te weinig parameters, de input was: " + input);
		} else sendMessage(ClientProtocol.ERROR + DELIM
				+ Error.UNKNOWN_COMMAND.value() + DELIM + Error.UNKNOWN_COMMAND);

	}

	private void join(String[] commandoArray) {
		if (commandoArray[0].equals(ClientProtocol.JOIN)) {
			super.setName(commandoArray[1]);
			aantalspelers = Integer.parseInt(commandoArray[2]);
			if (aantalspelers != 0)
				aantalspelers++;
			boolean geldigenaam = !getName().equals("");
			for (ClientHandler c : server.threads) {
				if (c.getName().equals(this.getName())) {
					geldigenaam = false;
					break;
				}
			}
			boolean geldigAantalSpelers = aantalspelers == 0
					|| (aantalspelers >= 2 && aantalspelers <= 4);
			if (geldigenaam && geldigAantalSpelers) {
				server.addHandler(this);
				this.sendMessage(ClientHandlerProtocol.ACCEPT);
				server.addMessage("[" + this.getName()
						+ " is aangemeld op de server en wil met "
						+ this.aantalspelers + " spelers Ringgz spelen]");
			} else {
				if (!geldigenaam) {
					server.addMessage("[" + this.getName()
							+ " is al aanwezig op de server]");
					this.sendMessage(ClientHandlerProtocol.ERROR + DELIM
							+ Error.INVALID_NAME.value() + DELIM + Error.INVALID_NAME);
				}
				if (!geldigAantalSpelers) {
					server.addMessage("[ De nieuwe speler" + this.getName()
							+ " heeft een ongeldig aantal spelers ingevuld]");
					this.sendMessage(ClientHandlerProtocol.ERROR + DELIM
							+ Error.INVALID_PLAYERS.value()+ DELIM + Error.INVALID_PLAYERS);
				}
			}
			for (int i = 2; i <= 4; i++) {
				int teller = 0;
				ArrayList<ClientHandler> clientHandlerArray = new ArrayList<ClientHandler>();
				ArrayList<String> clientHandlerNames = new ArrayList<String>();
				String spelers = "";

				for (ClientHandler c : server.threads) {
					if ((c.aantalSpelers() == i || c.aantalSpelers() == 0)
							&& clientHandlerArray.size() < i && !c.playing) {
						teller++;
						clientHandlerArray.add(c);
						clientHandlerNames.add(c.getName());
						if (clientHandlerArray.size() != i)
							spelers += c.getName() + "~";
						else
							spelers += c.getName();

					}
				}
				if (teller == i) {
					String[] namen = clientHandlerNames.toArray(new String[i]);
					Spel spel = new Spel(i, namen);
					server.spellen.put(clientHandlerArray, spel);
					server.addMessage("nieuw " + i
							+ "-spelerspel gestart met de spelers " + spelers);
					for (ClientHandler c : clientHandlerArray) {
						c.setPlaying(true);
						c.sendMessage(ClientHandlerProtocol.START_GAME + "~"
								+ spel.eersteZet + "~" + spelers);
						c.sendMessage(ClientHandlerProtocol.NEXT_MOVE + "~"
								+ spel.getHuidigeSpeler().getNaam());

					}
				}

			}
		} else {
			sendMessage(ClientProtocol.ERROR + DELIM
					+ Error.UNKNOWN_COMMAND.value() + DELIM + Error.UNKNOWN_COMMAND);
		}

	}

	private void doMove(String[] commandoArray) {
		int index = Integer.parseInt(commandoArray[1]);
		int kleur = Integer.parseInt(commandoArray[2]);
		int type = Integer.parseInt(commandoArray[3]);
		Spel spel = server.getSpel(this);

		if (spel.doeZet(getName(), kleur, type, index)) {
			server.broadcastSpel(ClientHandlerProtocol.SET_MOVE + DELIM + index
					+ DELIM + kleur + DELIM + type, this);
			if (spel.zetMogelijk(spel.getHuidigeSpeler())) {
				server.broadcastSpel(ClientHandlerProtocol.NEXT_MOVE + DELIM
						+ spel.getHuidigeSpeler().getNaam(), this);
			} else {
				server.broadcastSpeler(ClientHandlerProtocol.ERROR + DELIM
						+ Error.GAME_OVER.value() + DELIM+  Error.GAME_OVER, this, spel
						.getHuidigeSpeler().getNaam());
				int k = 0;
				while(k<=4){
					if(spel.zetMogelijk(spel.getHuidigeSpeler())){
						k = 4;}else{
					spel.setSpeler(spel.getHuidigeSpeler().getNummerSpeler() + 1);
					
					}
					k++;
				}
		
				server.broadcastSpel(ClientHandlerProtocol.NEXT_MOVE + DELIM
						+ spel.getHuidigeSpeler().getNaam(), this);
			}
			isGameOver();		

		} else {
			sendMessage(ClientProtocol.ERROR + "~"
					+ Error.INVALID_MOVE.value() + DELIM + Error.INVALID_MOVE);
		}
	}
	private void isGameOver(){
		Spel spel = server.getSpel(this);
		String gameOverString = "";
		if (spel.isGameOver()){
			for(Speler s: spel.getSpelers()){
				if(spel.isWinnaar() != null && s.equals(spel.isWinnaar())){
					int punten = spel.spelerPunten(s) + 1;
					gameOverString += DELIM +  s.getNaam() + DELIM + punten;
				}else{
					gameOverString += DELIM + s.getNaam() + DELIM + spel.spelerPunten(s);
				}
			}
			server.broadcastSpel(ClientHandlerProtocol.GAME_OVER + gameOverString, this);
		
			Set<ArrayList<ClientHandler>> ksch = server.spellen.keySet();
			Iterator kschIterator = ksch.iterator();
			ArrayList<ClientHandler> al = null;
			while (kschIterator.hasNext()) {
				ArrayList<ClientHandler> altemp = ((ArrayList<ClientHandler>) kschIterator
						.next());
				if (altemp.contains(this))
					al = altemp;
			}
			this.playing = false;

			server.spellen.remove(al);
		}
	}
	  
	private void errorHandle(String[] commandoArray) {
		server.addMessage(commandoArray[2]);

	}

	/**
	 * Deze ClientHandler meldt zich af bij de Server en stuurt vervolgens een
	 * laatste broadcast naar de Server om te melden dat de Client niet langer
	 * deelneemt aan de chatbox.
	 */
	public void close() {
		try {
			sock.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void shutdown() {
		server.removeHandler(this);
		server.broadcastSpel(
				ClientProtocol.ERROR + DELIM + Error.PLAYER_LEFT.value() + DELIM + getName() +" is weggegaan", this);
		Spel spel = server.getSpel(this);
		if(spel != null){
		spel.setGameOver(true);
		isGameOver();
		}

		
		
		this.stop();
	}

	public void sendMessage(String msg) {

		try {
			out.write(msg + "\n");
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
