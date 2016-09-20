package Ringgz.GUI;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Ringgz.Server.*;
import Ringgz.Spel.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * @author Christian
 * 
 */
public class StartGUI extends JFrame implements ItemListener, ActionListener {

	private JPanel contentPane;
	private JPanel pnlStart;
	private JPanel pnlSwitch;
	private JPanel pnlServer;
	private JPanel pnlClient;
	private JButton btnStartServer;
	private JTextField txtAddressServer;
	private JTextField txtPoortServer;
	private JButton btnZoekSpelClient;
	private JTextField txtNaamSpelerClient;
	private JTextField txtHostnaamClient;
	private JTextField txtPoortClient;
	private JComboBox comboBoxClient;

	private int port;
	private Client currClient;
	private int aantalSpelers;
	private JPanel pnlEastServer;
	private JPanel pnlWestServer;
	private JPanel pnlCenterServer;
	private JPanel pnlCenterClient;
	private JPanel pnlWestClient;
	private JPanel pnlEastClient;
	private JLabel lblExceptionServer;
	private JLabel lblExceptionClient;

	public StartGUI() {
		super("Ringgz");
		buildGUI();
		setVisible(true);
	}

	public void buildGUI() {
		// //////////////////// Container ////////////////
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 588, 460);
		contentPane = new JPanel();
		contentPane.setBackground(Color.BLACK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		pnlStart();

		pnlSwitch();
	}

	private void pnlStart() {
		contentPane.setLayout(new BorderLayout(0, 0));
		pnlStart = new JPanel();
		pnlStart.setBackground(Color.BLACK);
		contentPane.add(pnlStart, BorderLayout.NORTH);
		pnlStart.setLayout(new BorderLayout(0, 10));

		JLabel lblWelkom = new JLabel("Welkom Bij Ringgz");
		lblWelkom.setForeground(Color.WHITE);
		lblWelkom.setHorizontalAlignment(SwingConstants.CENTER);
		lblWelkom.setFont(new Font("Tahoma", Font.PLAIN, 50));
		pnlStart.add(lblWelkom, BorderLayout.NORTH);

		JLabel lblServerClient = new JLabel(
				"Wilt u als server of als client spelen?");
		lblServerClient.setForeground(Color.WHITE);
		lblServerClient.setFont(new Font("Tahoma", Font.PLAIN, 25));
		lblServerClient.setHorizontalAlignment(SwingConstants.CENTER);
		pnlStart.add(lblServerClient, BorderLayout.CENTER);

		JComboBox comboBoxStart = new JComboBox();
		pnlStart.add(comboBoxStart, BorderLayout.SOUTH);
		comboBoxStart.setModel(new DefaultComboBoxModel(new String[] {
				PC.SERVER, PC.CLIENT }));
		comboBoxStart.setMaximumRowCount(2);
		comboBoxStart.setToolTipText("");
		comboBoxStart.addItemListener(this);
	}

	private void pnlSwitch() {
		pnlSwitch = new JPanel();
		contentPane.add(pnlSwitch);
		pnlSwitch.setLayout(new CardLayout(0, 0));

		pnlServer();

		pnlClient();
	}

	private void pnlServer() {
		pnlServer = new JPanel();
		pnlServer.setBackground(Color.BLACK);
		pnlSwitch.add(pnlServer, PC.SERVER);
		pnlServer.setLayout(new BorderLayout(0, 0));

		lblExceptionServer = new JLabel("");
		lblExceptionServer.setForeground(Color.WHITE);
		lblExceptionServer.setBackground(Color.BLACK);
		pnlServer.add(lblExceptionServer, BorderLayout.NORTH);

		pnlCenterServer = new JPanel();
		pnlCenterServer.setBackground(Color.BLACK);
		pnlServer.add(pnlCenterServer, BorderLayout.CENTER);
		pnlCenterServer.setLayout(new BorderLayout(0, 0));

		pnlWestServer = new JPanel();
		pnlWestServer.setForeground(Color.BLACK);
		pnlWestServer.setBackground(Color.BLACK);
		pnlCenterServer.add(pnlWestServer, BorderLayout.WEST);
		GridBagLayout gbl_pnlWestServer = new GridBagLayout();
		gbl_pnlWestServer.columnWidths = new int[] { 100 };
		gbl_pnlWestServer.rowHeights = new int[] { 25, 0, 0 };
		gbl_pnlWestServer.columnWeights = new double[] { 0.0 };
		gbl_pnlWestServer.rowWeights = new double[] { 0.0, 0.0,
				Double.MIN_VALUE };
		pnlWestServer.setLayout(gbl_pnlWestServer);

		JLabel lblPoortServer = new JLabel("Poort:");
		lblPoortServer.setForeground(Color.WHITE);
		GridBagConstraints gbc_lblPoortServer = new GridBagConstraints();
		gbc_lblPoortServer.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblPoortServer.insets = new Insets(10, 5, 0, 0);
		gbc_lblPoortServer.gridx = 0;
		gbc_lblPoortServer.gridy = 0;
		pnlWestServer.add(lblPoortServer, gbc_lblPoortServer);
		lblPoortServer.setFont(new Font("Tahoma", Font.PLAIN, 20));

		JLabel lblAdresServer = new JLabel("Adres:");
		lblAdresServer.setForeground(Color.WHITE);
		GridBagConstraints gbc_lblAdresServer = new GridBagConstraints();
		gbc_lblAdresServer.insets = new Insets(0, 5, 0, 0);
		gbc_lblAdresServer.anchor = GridBagConstraints.NORTH;
		gbc_lblAdresServer.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblAdresServer.gridx = 0;
		gbc_lblAdresServer.gridy = 1;
		pnlWestServer.add(lblAdresServer, gbc_lblAdresServer);
		lblAdresServer.setFont(new Font("Tahoma", Font.PLAIN, 20));

		pnlEastServer = new JPanel();
		pnlEastServer.setBackground(Color.BLACK);
		pnlCenterServer.add(pnlEastServer, BorderLayout.EAST);
		GridBagLayout gbl_pnlEastServer = new GridBagLayout();
		gbl_pnlEastServer.columnWidths = new int[] { 150, 116, 0 };
		gbl_pnlEastServer.rowHeights = new int[] { 22, 22, 0 };
		gbl_pnlEastServer.columnWeights = new double[] { 0.0, 0.0,
				Double.MIN_VALUE };
		gbl_pnlEastServer.rowWeights = new double[] { 0.0, 0.0,
				Double.MIN_VALUE };
		pnlEastServer.setLayout(gbl_pnlEastServer);

		txtPoortServer = new JTextField();
		GridBagConstraints gbc_txtPoortServer = new GridBagConstraints();
		gbc_txtPoortServer.anchor = GridBagConstraints.NORTHWEST;
		gbc_txtPoortServer.insets = new Insets(10, 0, 0, 5);
		gbc_txtPoortServer.gridx = 1;
		gbc_txtPoortServer.gridy = 0;
		pnlEastServer.add(txtPoortServer, gbc_txtPoortServer);
		txtPoortServer.setText("2727");
		txtPoortServer.setColumns(10);

		txtAddressServer = new JTextField(getHostAddress(), 12);
		GridBagConstraints gbc_txtAddressServer = new GridBagConstraints();
		gbc_txtAddressServer.insets = new Insets(0, 0, 0, 5);
		gbc_txtAddressServer.anchor = GridBagConstraints.NORTHWEST;
		gbc_txtAddressServer.gridx = 1;
		gbc_txtAddressServer.gridy = 1;
		pnlEastServer.add(txtAddressServer, gbc_txtAddressServer);
		txtAddressServer.setEditable(false);
		txtAddressServer.setColumns(10);

		btnStartServer = new JButton("Start Server");
		pnlServer.add(btnStartServer, BorderLayout.SOUTH);
		btnStartServer.addActionListener(this);
		btnStartServer.setFont(new Font("Tahoma", Font.PLAIN, 30));
	}

	private void pnlClient() {
		pnlClient = new JPanel();
		pnlClient.setBackground(Color.BLACK);
		pnlSwitch.add(pnlClient, PC.CLIENT);
		pnlClient.setLayout(new BorderLayout(0, 0));

		lblExceptionClient = new JLabel("");
		lblExceptionClient.setForeground(Color.WHITE);
		pnlClient.add(lblExceptionClient, BorderLayout.NORTH);

		pnlCenterClient = new JPanel();
		pnlCenterClient.setBackground(Color.BLACK);
		pnlClient.add(pnlCenterClient, BorderLayout.CENTER);
		pnlCenterClient.setLayout(new BorderLayout(0, 0));

		pnlWestClient = new JPanel();
		pnlWestClient.setBackground(Color.BLACK);
		pnlCenterClient.add(pnlWestClient, BorderLayout.WEST);
		GridBagLayout gbl_pnlWestClient = new GridBagLayout();
		gbl_pnlWestClient.columnWidths = new int[] { 150 };
		gbl_pnlWestClient.rowHeights = new int[] { 25, 0, 0, 0 };
		gbl_pnlWestClient.columnWeights = new double[] { 0.0 };
		gbl_pnlWestClient.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0 };
		pnlWestClient.setLayout(gbl_pnlWestClient);

		JLabel lblHostnaamClient = new JLabel("Hostnaam:");
		lblHostnaamClient.setForeground(Color.WHITE);
		GridBagConstraints gbc_lblHostnaamClient = new GridBagConstraints();
		gbc_lblHostnaamClient.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblHostnaamClient.insets = new Insets(0, 5, 0, 5);
		gbc_lblHostnaamClient.gridx = 0;
		gbc_lblHostnaamClient.gridy = 0;
		pnlWestClient.add(lblHostnaamClient, gbc_lblHostnaamClient);
		lblHostnaamClient.setFont(new Font("Tahoma", Font.PLAIN, 20));

		JLabel lblPoortClient = new JLabel("Poort:");
		lblPoortClient.setForeground(Color.WHITE);
		GridBagConstraints gbc_lblPoortClient = new GridBagConstraints();
		gbc_lblPoortClient.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblPoortClient.insets = new Insets(0, 5, 0, 5);
		gbc_lblPoortClient.gridx = 0;
		gbc_lblPoortClient.gridy = 1;
		pnlWestClient.add(lblPoortClient, gbc_lblPoortClient);
		lblPoortClient.setFont(new Font("Tahoma", Font.PLAIN, 20));

		JLabel lblNaamSpelerClient = new JLabel("Naam Speler:");
		lblNaamSpelerClient.setForeground(Color.WHITE);
		GridBagConstraints gbc_lblNaamSpelerClient = new GridBagConstraints();
		gbc_lblNaamSpelerClient.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblNaamSpelerClient.insets = new Insets(0, 5, 0, 5);
		gbc_lblNaamSpelerClient.gridx = 0;
		gbc_lblNaamSpelerClient.gridy = 2;
		pnlWestClient.add(lblNaamSpelerClient, gbc_lblNaamSpelerClient);
		lblNaamSpelerClient.setFont(new Font("Tahoma", Font.PLAIN, 20));

		JLabel lblAantalSpelersClient = new JLabel("Aantal Spelers:");
		lblAantalSpelersClient.setForeground(Color.WHITE);
		GridBagConstraints gbc_lblAantalSpelersClient = new GridBagConstraints();
		gbc_lblAantalSpelersClient.insets = new Insets(0, 5, 0, 5);
		gbc_lblAantalSpelersClient.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblAantalSpelersClient.gridx = 0;
		gbc_lblAantalSpelersClient.gridy = 3;
		pnlWestClient.add(lblAantalSpelersClient, gbc_lblAantalSpelersClient);
		lblAantalSpelersClient.setFont(new Font("Tahoma", Font.PLAIN, 20));

		pnlEastClient = new JPanel();
		pnlEastClient.setBackground(Color.BLACK);
		pnlCenterClient.add(pnlEastClient, BorderLayout.EAST);
		GridBagLayout gbl_pnlEastClient = new GridBagLayout();
		gbl_pnlEastClient.columnWidths = new int[] { 150 };
		gbl_pnlEastClient.rowHeights = new int[] { 25 };
		gbl_pnlEastClient.columnWeights = new double[] { 0.0, 0.0 };
		gbl_pnlEastClient.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0 };
		pnlEastClient.setLayout(gbl_pnlEastClient);

		txtHostnaamClient = new JTextField();
		GridBagConstraints gbc_txtHostnaamClient = new GridBagConstraints();
		gbc_txtHostnaamClient.anchor = GridBagConstraints.NORTHWEST;
		gbc_txtHostnaamClient.insets = new Insets(0, 0, 0, 5);
		gbc_txtHostnaamClient.gridx = 1;
		gbc_txtHostnaamClient.gridy = 0;
		pnlEastClient.add(txtHostnaamClient, gbc_txtHostnaamClient);
		txtHostnaamClient.setColumns(10);

		txtPoortClient = new JTextField("2727", 5);
		GridBagConstraints gbc_txtPoortClient = new GridBagConstraints();
		gbc_txtPoortClient.anchor = GridBagConstraints.NORTHWEST;
		gbc_txtPoortClient.insets = new Insets(0, 0, 0, 5);
		gbc_txtPoortClient.gridx = 1;
		gbc_txtPoortClient.gridy = 1;
		pnlEastClient.add(txtPoortClient, gbc_txtPoortClient);
		txtPoortClient.setColumns(10);

		txtNaamSpelerClient = new JTextField();
		GridBagConstraints gbc_txtNaamSpelerClient = new GridBagConstraints();
		gbc_txtNaamSpelerClient.anchor = GridBagConstraints.NORTHWEST;
		gbc_txtNaamSpelerClient.insets = new Insets(0, 0, 0, 5);
		gbc_txtNaamSpelerClient.gridx = 1;
		gbc_txtNaamSpelerClient.gridy = 2;
		pnlEastClient.add(txtNaamSpelerClient, gbc_txtNaamSpelerClient);
		txtNaamSpelerClient.setColumns(10);

		comboBoxClient = new JComboBox();
		GridBagConstraints gbc_comboBoxClient = new GridBagConstraints();
		gbc_comboBoxClient.anchor = GridBagConstraints.NORTHWEST;
		gbc_comboBoxClient.gridx = 1;
		gbc_comboBoxClient.gridy = 3;
		pnlEastClient.add(comboBoxClient, gbc_comboBoxClient);
		comboBoxClient.setModel(new DefaultComboBoxModel(new String[] {
				"Random", "2", "3", "4" }));
		comboBoxClient.setMaximumRowCount(4);

		btnZoekSpelClient = new JButton("Zoek Spel");
		btnZoekSpelClient.addActionListener(this);
		btnZoekSpelClient.setFont(new Font("Tahoma", Font.PLAIN, 30));
		pnlClient.add(btnZoekSpelClient, BorderLayout.SOUTH);
	}

	private String getHostAddress() {
		try {
			InetAddress iaddr = InetAddress.getLocalHost();
			return iaddr.getHostAddress();
		} catch (UnknownHostException e) {
			return "?unknown?";
		}
	}

	private boolean checkClientFields() {
		boolean adr = false;
		boolean por = false;
		boolean nam = false;

		if (txtHostnaamClient.getText().length() != 0) {
			adr = true;
		} else {
			adr = false;
		}

		if (txtPoortClient.getText().length() != 0) {
			por = true;
		} else {
			por = false;
		}

		if (txtNaamSpelerClient.getText().length() >= 4) {
			nam = true;
		} else {
			nam = false;
		}

		return adr && por && nam;

	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		CardLayout cl = (CardLayout) (pnlSwitch.getLayout());
		cl.show(pnlSwitch, (String) e.getItem());
	}

	/**
	 * Als de "Start Listening" knop wordt ingedrukt wordt de methode
	 * startListening() aangeroepen.
	 */
	public void actionPerformed(ActionEvent e) {
		if (btnStartServer.equals(e.getSource())) {
			port = Integer.parseInt(txtPoortServer.getText());
			
			ServerGUI gui = new ServerGUI(port);
		}
		if (btnZoekSpelClient.equals(e.getSource())) {
			connect();
		}

	}

	public void connect() {
		InetAddress iaddress;

		try {
			iaddress = InetAddress.getByName(txtHostnaamClient.getText());
		} catch (UnknownHostException e) {
			iaddress = null;
		}

		port = -1;
		try {
			port = Integer.parseInt(txtPoortClient.getText());
			if (port < 1) {
				// Error voor poort niet goed
				port = -1;
			}
		} catch (NumberFormatException e) {
			// Error poort moet nummer zijn
		}

		if (comboBoxClient.getSelectedItem().equals("Random"))
			aantalSpelers = 0;
		else {
			aantalSpelers = Integer.parseInt((String) (comboBoxClient
					.getSelectedItem()));
			aantalSpelers = aantalSpelers - 1;

		}

		String naam = txtNaamSpelerClient.getText();

		if (port != -1 && iaddress != null) {
			try {
				new LobbyGUI(naam, iaddress, port, aantalSpelers);

			} catch (IOException e) {
				// Error failed to connect
			}
		} else {
			// Error er klopt nog iets niet
			// addMessage("[Hostname " + tfAddress.getText() + " and/or port "
			// + port + " invalid]");
		}
	}

	public static void main(String args[]) {
		StartGUI gui = new StartGUI();
	}

}