package Ringgz.Server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Set;
import java.util.TreeMap;

import Ringgz.Spel.Spel;
import Ringgz.GUI.SpelGUI;
import Ringgz.protocol.ClientHandlerProtocol;
import Ringgz.protocol.ClientProtocol;
import Ringgz.protocol.Error;

public class Client extends Thread{

	private int aantalSpelers;
	private MessageUI mui;
	private Socket sock;
	private BufferedReader in;
	private BufferedWriter out;
	private static final String DELIM = ClientProtocol.DELIM;
	private Spel spel;
	private HashMap<Integer, String> errors;
	private HashMap<String, Integer> commandoAantal;
	private Set<String> commandos;


	/**
	 * Construeert een Client-object en probeert een socketverbinding met een
	 * Server op te starten.
	 */
	public Client(String naam, InetAddress host, int port, int aantalSpelers,
			MessageUI mui) throws IOException {
		super(naam);
		this.mui = mui;
		this.aantalSpelers = aantalSpelers;
		sock = new Socket();

		sock.connect(new InetSocketAddress(host, port), 5000);
		// een progressbar laten zien en als er een timeout voorkomt, melding
		// geven.

		// /IO-Stream creeeren en aanmelden op de server.
		in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
		sendMessage(ClientProtocol.JOIN + "~" + naam + "~" + aantalSpelers);
		System.out.println(getName());

		// /Lijst van commando's met zijn bijbehorende hoeveelheid parameters
		// incl het commando zelf.
		commandoAantal = new HashMap<String, Integer>();
		commandoAantal.put(ClientHandlerProtocol.START_GAME, 0);
		commandoAantal.put(ClientHandlerProtocol.NEXT_MOVE, 2);
		commandoAantal.put(ClientHandlerProtocol.SET_MOVE, 4);
		commandoAantal.put(ClientHandlerProtocol.ERROR, 3);
		commandoAantal.put(ClientHandlerProtocol.ACCEPT, 1);
		commandoAantal.put(ClientHandlerProtocol.GAME_OVER, 0);
		commandoAantal.put(ClientHandlerProtocol.BROADCAST, 3);
		
		
		commandos = commandoAantal.keySet();

		// /errorLijst
		errors = new HashMap<Integer, String>();
		Error[] errorArray = { Error.INVALID_MOVE, Error.INVALID_NAME,
				Error.INVALID_PLAYERS, Error.PLAYER_LEFT,
				Error.UNKNOWN_COMMAND, Error.GAME_OVER, Error.DEFAULT };
		for (Error e : errorArray) {
			errors.put(e.value(), e.toString());
		}
	}

	/**
	 * Leest berichten uit de socketverbinding. Elk berichtje wordt gestuurd
	 * naar de MessageUI van deze Client. Als er iets fout gaat bij het lezen
	 * van berichten, sluit deze methode de socketverbinding.
	 */
	public void run() {
		System.out.println("gestart");
		String input = null;
		do {
			try {
				input = in.readLine();
				if (input != null)
					doCommando(input);
				else
					shutdown();
			} catch (IOException e) {
				shutdown();
				input = null;
			}
		} while (!sock.isClosed() && input != null);
	}



	public void doCommando(String input) {
		String[] commandoArray = input.split(ClientProtocol.DELIM);

		if (commandoAantal.get(commandoArray[0]) != null
				&& (commandoArray.length == commandoAantal
						.get(commandoArray[0]) || commandoAantal
						.get(commandoArray[0]) == 0)) {

			if (commandoArray[0].equals(ClientHandlerProtocol.START_GAME)) {
				startGame(commandoArray);
			} else if (commandoArray[0].equals(ClientHandlerProtocol.NEXT_MOVE)) {
				nextMove(commandoArray);
			} else if (commandoArray[0].equals(ClientHandlerProtocol.SET_MOVE)) {
				setMove(commandoArray);
			} else if (commandoArray[0].equals(ClientHandlerProtocol.GAME_OVER)) {
				gameOver(commandoArray);
			} else if (commandoArray[0].equals(ClientHandlerProtocol.ERROR)) {
				errorHandle(commandoArray);
			} 
		 else if (commandoArray[0].equals(ClientHandlerProtocol.BROADCAST)) {
			mui.addMessage("["+ commandoArray[1] + "]: " +  commandoArray[2]);
		} 
			else if (commandoArray[0].equals(ClientProtocol.CHALLENGE)) {
				sendMessage(ClientProtocol.ERROR + DELIM + Error.CHALLENGE_NOT_SUPPORTED.value() + DELIM +Error.CHALLENGE_NOT_SUPPORTED);
			}else if (commandoArray[0].equals(ClientHandlerProtocol.ACCEPT)) {
				mui.addMessage("succesvol aangemeld op de server");
			} else if (commandos.contains(commandos)) {
				sendMessage(ClientProtocol.ERROR + DELIM
						+ Error.DEFAULT.value() + DELIM
						+ "Er zijn te weinig parameters");
				mui.addMessage("te weinig parameters, de input was: " + input);
			}
		}
		 else {
			sendMessage(ClientProtocol.ERROR + DELIM
					+ Error.UNKNOWN_COMMAND + DELIM
					+ Error.UNKNOWN_COMMAND);
			mui.addMessage("Onbekend Commando, de input was: " + input);
		 }
	}

	private void gameOver(String[] commandoArray) {
		ArrayList<String> punten = new ArrayList<String>();
		ArrayList<String> namen = new ArrayList<String>();
		int teller = 0;
		String message = "punten";
		for (int i = 1; i < commandoArray.length; i++) {
			if (teller == 0) {
				namen.add(commandoArray[i]);
				punten.add(commandoArray[i + 1]);

			}
			teller++;
			teller = teller % 2;
		}
		for (int i = 0; i < namen.size(); i++) {

			message += "\nnaam: " + namen.get(i) + " punten: " + punten.get(i);

		}
		mui.addMessage(message);

	}

	private void errorHandle(String[] commandoArray) {
		mui.addMessage(commandoArray[2]);
		
		if(commandoArray[1].equals(Error.INVALID_MOVE)){
			spel.notifyObservers();
		}

	}

	public void startGame(String[] commandoArray) {
		if (commandoArray.length < 4 && commandoArray.length > 6) {
			sendMessage(ClientProtocol.ERROR + DELIM + Error.DEFAULT + DELIM
					+ "Er zijn te weinig parameters");
		} else {

		}
		int aantalSpelers = commandoArray.length - 2;
		ArrayList<String> namen = new ArrayList<String>();
		for (int i = 2; i < commandoArray.length; i++) {
			namen.add(commandoArray[i]);
		}
		String[] namenArray = namen.toArray(new String[namen.size()]);
		spel = new Spel(aantalSpelers, namenArray,
				Integer.parseInt(commandoArray[1]));
		String namenStrings = "";
		for(String s: namenArray){
			namenStrings += " " + s;
		}
		mui.addMessage("nieuw " + aantalSpelers + "-spelerspel gestart met" + namenStrings);
		new SpelGUI(spel, this, getName());
	}

	public void nextMove(String[] commandoArray) {
		spel.setSpeler(commandoArray[1]);
	}

	public void setMove(String[] commandoArray) {
		int index = Integer.parseInt(commandoArray[1]);
		int kleur = Integer.parseInt(commandoArray[2]);
		int type = Integer.parseInt(commandoArray[3]);
		spel.doeZetSpeler(kleur, type, index);
	}

	/** Stuurt een bericht over de socketverbinding naar de ClientHandler. */
	public void sendMessage(String msg) {
		try {
			if (!sock.isClosed()) {
				out.write(msg + "\n");
				out.flush();
			} else {
				mui.addMessage("server bestaat niet meer");
			}
		} catch (IOException e) {
			sendMessage(msg);
		}
	}

	/** Sluit de socketverbinding van deze client. */
	public void shutdown() {
		try {
			sock.close();
		} catch (IOException e) {
			mui.addMessage("[Connection closed]");
		}
	}

}
