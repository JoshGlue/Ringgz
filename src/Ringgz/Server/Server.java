package Ringgz.Server;

import java.io.*;
import java.net.*;
import java.util.*;

import Ringgz.Spel.Spel;
import Ringgz.protocol.ClientHandlerProtocol;
import Ringgz.protocol.ClientProtocol;
import Ringgz.protocol.Error;

public class Server extends Thread {

	private int port;
	private MessageUI mui;
	public ArrayList<ClientHandler> threads;
	public HashMap<ArrayList<ClientHandler>, Spel> spellen;
	private ServerSocket ss = null;

	/** Construeert een nieuw Server-object. */
	public Server(int port, MessageUI mui) {
		threads = new ArrayList<ClientHandler>();
		this.port = port;
		this.mui = mui;
		spellen = new HashMap<ArrayList<ClientHandler>, Spel>();

	}

	public void addMessage(String msg) {
	
		mui.addMessage(msg);
	}

	public Spel getSpel(ClientHandler c) {
		Set<ArrayList<ClientHandler>> ksch = spellen.keySet();
		Iterator kschIterator = ksch.iterator();
		ArrayList<ClientHandler> al = null;
		while (kschIterator.hasNext()) {
			ArrayList<ClientHandler> altemp = ((ArrayList<ClientHandler>) kschIterator
					.next());
			if (altemp.contains(c))
				al = altemp;
		}
		return spellen.get(al);

	}

	/**
	 * Luistert op de port van deze Server of er Clients zijn die een verbinding
	 * willen maken. Voor elke nieuwe socketverbinding wordt een nieuw
	 * ClientHandler thread opgestart die de verdere communicatie met de Client
	 * afhandelt.
	 */
	public void run() {

		try {
			ss = new ServerSocket(port);

			while (true) {
				Socket s2 = ss.accept();
				ClientHandler c1 = new ClientHandler(this, s2);
				c1.start();

				// c1.announce();
			}
		} catch (IOException e) {
			mui.addMessage("[Failed to establish connection]");
		}
	}

	/**
	 * Stuurt een bericht via de collectie van aangesloten ClientHandlers naar
	 * alle aangesloten Clients.
	 * 
	 * @param msg
	 *            bericht dat verstuurd wordt
	 */
	public void broadcast(String msg) {
		for (ClientHandler c : threads) {
			c.sendMessage(msg);
		}
		mui.addMessage(msg);
	}

	public void broadcastSpel(String msg, ClientHandler c) {
		mui.addMessage(msg);

		Set<ArrayList<ClientHandler>> ksch = spellen.keySet();
		Iterator kschIterator = ksch.iterator();
		ArrayList<ClientHandler> al = null;
		while (kschIterator.hasNext()) {
			ArrayList<ClientHandler> altemp = ((ArrayList<ClientHandler>) kschIterator
					.next());
			if (altemp.contains(c))
				al = altemp;
		}
		if (al != null) {

			for (ClientHandler c1 : al) {
				c1.sendMessage(msg);
			}
		}

	}

	/**
	 * Voegt een ClientHandler aan de collectie van ClientHandlers toe.
	 * 
	 * @param handler
	 *            ClientHandler die wordt toegevoegd
	 */
	public void addHandler(ClientHandler handler) {
		threads.add(handler);
	}

	/**
	 * Verwijdert een ClientHandler uit de collectie van ClientHandlers.
	 * 
	 * @param handler
	 *            ClientHandler die verwijderd wordt
	 */
	public void removeHandler(ClientHandler handler) {
		if (threads.contains(handler)) {
			threads.remove(handler);
		}
	}

	public void broadcastSpeler(String msg, ClientHandler c, String naam) {
		mui.addMessage(msg);
		ClientHandler speler = null;

		Set<ArrayList<ClientHandler>> ksch = spellen.keySet();
		Iterator kschIterator = ksch.iterator();
		ArrayList<ClientHandler> al = null;
		while (kschIterator.hasNext()) {
			ArrayList<ClientHandler> altemp = ((ArrayList<ClientHandler>) kschIterator
					.next());
			if (altemp.contains(c))
				al = altemp;
		}
		for (ClientHandler c1 : al) {
			if (c1.getName().equals(naam)) {
				speler = c1;
			}
		}
		speler.sendMessage(msg);
	}

	public void shutdown() {
		for (int i = 0; i < threads.size(); i++) {
			threads.get(i).close();
			threads.remove(i);
		}
		try {
			if (ss != null)
				ss.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
