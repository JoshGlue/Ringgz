package Ringgz.GUI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.io.IOException;
import java.net.InetAddress;

import javax.swing.*;

import Ringgz.Server.*;
import Ringgz.protocol.ClientProtocol;
import java.awt.Color;

public class LobbyGUI extends JFrame implements MessageUI, KeyListener {

	private JTextField tfMyMessage;
	private JTextArea taMessages;

	private Client currClient;

	public LobbyGUI(String naam, InetAddress host, int port, int aantalSpelers ) throws IOException {
		super("Lobby van:" + naam);
		buildGUI();
		currClient = new Client(naam, host, port, aantalSpelers, this);
		currClient.start();		
		setVisible(true);
	}

	public void buildGUI() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		  addWindowListener(new java.awt.event.WindowAdapter() {
		         public void windowClosing(java.awt.event.WindowEvent e) {
		            currClient.shutdown();
		          }
		       });
		setSize(600,400);
		
		JPanel Lobby = new JPanel();
		Lobby.setBackground(Color.BLACK);
		Lobby.setLayout(new BorderLayout());

		JPanel pm1 = new JPanel();
		pm1.setBackground(Color.BLACK);
		JPanel pm2 = new JPanel();
		pm2.setBackground(Color.BLACK);

		pm1.setLayout(new BorderLayout());
		JLabel lbMyMessage = new JLabel("My message:");
		lbMyMessage.setForeground(Color.WHITE);
		lbMyMessage.setHorizontalAlignment(SwingConstants.CENTER);
		tfMyMessage = new JTextField("");
		tfMyMessage.setEnabled(true);
		tfMyMessage.addKeyListener(this);
		pm1.add(lbMyMessage, BorderLayout.NORTH);
		pm1.add(tfMyMessage, BorderLayout.CENTER);

		pm2.setLayout(new BorderLayout());
		JLabel lbMessages = new JLabel("Messages:");
		lbMessages.setForeground(Color.WHITE);
		lbMessages.setBackground(Color.BLACK);
		lbMessages.setHorizontalAlignment(SwingConstants.CENTER);
		taMessages = new JTextArea("", 15, 50);
		taMessages.setEnabled(false);
		taMessages.setForeground(Color.WHITE);
		taMessages.setBackground(Color.BLACK);
		taMessages.setEditable(false);
		JScrollPane scrollPane1 = new JScrollPane(taMessages);
		pm2.add(lbMessages, BorderLayout.NORTH);
		pm2.add(scrollPane1, BorderLayout.CENTER);
		

		
		Lobby.add(pm1, BorderLayout.NORTH);
		
		JLabel lblException = new JLabel("");
		lblException.setForeground(Color.WHITE);
		lblException.setBackground(Color.BLACK);
		lblException.setHorizontalAlignment(SwingConstants.CENTER);
		pm1.add(lblException, BorderLayout.SOUTH);
		Lobby.add(pm2, BorderLayout.CENTER);

		Container cc = getContentPane();
		getContentPane().setLayout(new BorderLayout(0, 0));

		cc.add(Lobby);
	}

	
	@Override
	public void addMessage(String msg) {
		taMessages.append(msg + "\n");
		taMessages.setCaretPosition(taMessages.getText().length());

	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
        if (e.getSource() == tfMyMessage && e.getKeyCode() == KeyEvent.VK_ENTER){
        	currClient.sendMessage(ClientProtocol.SEND_MESSAGE + ClientProtocol.DELIM + tfMyMessage.getText());
            tfMyMessage.setText("");
        }
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
	
	

}