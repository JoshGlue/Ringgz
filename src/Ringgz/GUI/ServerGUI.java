package Ringgz.GUI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import Ringgz.Server.*;

import java.net.*;

/**
 * P2 prac wk4. ServerGui. Een GUI voor de Server.
 * 
 * @author Theo Ruys
 * @version 2005.02.21
 */
public class ServerGUI extends JFrame implements MessageUI {
	private JTextArea taMessages;
	private Server server;

	/** Construeert een ServerGUI object. */
	public ServerGUI(int port) {
		super("ServerGUI");
		getContentPane().setBackground(Color.BLACK);
		buildGUI();

		server = new Server(port, this);
		server.start();

		setVisible(true);

		addMessage("Started listening on port " + port + "...");
	}

	/** Bouwt de daadwerkelijke GUI. */
	public void buildGUI() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				server.shutdown();
			}
		});

		setSize(600, 400);

		Container cc = getContentPane();
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		taMessages = new JTextArea("", 15, 50);
		taMessages.setEnabled(false);
		taMessages.setForeground(Color.WHITE);
		taMessages.setBackground(Color.BLACK);
		taMessages.setEditable(false);
		JScrollPane scrollPane1 = new JScrollPane(taMessages);
		getContentPane().add(scrollPane1, BorderLayout.CENTER);

		JPanel pnlNord = new JPanel();
		pnlNord.setBackground(Color.BLACK);
		getContentPane().add(pnlNord, BorderLayout.NORTH);
		pnlNord.setLayout(new BorderLayout(0, 0));

		JLabel lblLog = new JLabel("Log:");
		pnlNord.add(lblLog, BorderLayout.NORTH);
		lblLog.setBackground(Color.BLACK);
		lblLog.setForeground(Color.WHITE);
		lblLog.setHorizontalAlignment(SwingConstants.CENTER);

		JLabel lblException = new JLabel("");
		lblException.setForeground(Color.RED);
		lblException.setHorizontalAlignment(SwingConstants.CENTER);
		pnlNord.add(lblException, BorderLayout.SOUTH);
	}

	/** Levert het Internetadres van deze computer op. */
	private String getHostAddress() {
		try {
			InetAddress iaddr = InetAddress.getLocalHost();
			return iaddr.getHostAddress();
		} catch (UnknownHostException e) {
			return "?unknown?";
		}
	}

	/** Voegt een bericht toe aan de TextArea op het frame. */
	public void addMessage(String msg) {
		taMessages.append(msg + "\n");
	}

}
