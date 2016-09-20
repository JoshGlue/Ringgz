package Ringgz.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import java.awt.CardLayout;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import Ringgz.Spel.*;
import Ringgz.Server.Client;
import Ringgz.Server.MessageUI;
import Ringgz.protocol.ClientProtocol;
import Ringgz.Server.Client;

import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.UIManager;
import javax.swing.JCheckBox;
import javax.swing.BoxLayout;
import javax.swing.JInternalFrame;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

/**
 * 
 * @author Christian
 * 
 */
public class SpelGUI extends JFrame implements ActionListener, Observer,
		ItemListener {

	private JPanel contentPane;
	private JPanel pnlBord;
	private JPanel pnlRingen;
	private JPanel pnlButtons;
	private JButton btnDoeZet;
	private JButton btnZetOpnieuw;
	private JButton btnHint;
	private JButton btnAnnuleerHint;
	private JButton btnZetHint;
	private JCheckBox cbAutoPlay;
	public Spel spel;
	private JButton velden[] = new JButton[PC.VAKKEN];
	private JButton figuren[];
	private Ring tempRing;
	private int tempIndex;
	private Client currClient;
	private String spelerNaam;
	private boolean autoPlay;
	private JPanel pnlSouth;
	private JPanel pnlEast;
	private JPanel panel;
	private JComboBox comboBoxBedenktijd;

	/**
	 * Construeerd een SpelGUI object met als parameters het aantal spelers en
	 * de namen van de spelers.
	 * 
	 * @param spelerNaam
	 * 
	 * @param int: spelers
	 * @param String
	 *            []: namen
	 */
	public SpelGUI(Spel spel, Client currClient, String spelerNaam) {
		super("Spel van: " + spelerNaam);
		System.out.println(spelerNaam);
		this.spelerNaam = spelerNaam;
		this.spel = spel;
		spel.addObserver(this);
		this.currClient = currClient;

		buildGUI();

		setVisible(true);

	}

	/**
	 * Create the frame.
	 */
	public void buildGUI() {
		// //////////////////// Container ////////////////
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setMinimumSize(new Dimension(750,610));

		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				currClient.shutdown();
			}
		});
		setBounds(100, 100, 800, 625);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setBackground(Color.BLACK);
		contentPane.setLayout(new BorderLayout());

		pnlBord();

		pnlEast();

		pnlSouth();

		refresh();
	}

	/**
	 * 
	 */
	private void pnlBord() {

		pnlBord = new JPanel();
		pnlBord.setBackground(Color.BLACK);
		pnlBord.setPreferredSize(new Dimension(500, 500));
		contentPane.add(pnlBord, BorderLayout.CENTER);
		pnlBord.setLayout(new GridLayout(5, 5, 15, 15));

		for (int i = 0; i < PC.VAKKEN; i++) {
			velden[i] = new JButton();
			velden[i].setEnabled(false);
			velden[i].setMargin(new Insets(0, 0, 0, 0));
			pnlBord.add(velden[i]);
			Draw draw = new Draw(spel.getBord().getVak(i).getAlleRingen());
			velden[i].add(draw);
			velden[i].addActionListener(this);
		}

	}

	private void pnlEast() {
		pnlEast = new JPanel();
		contentPane.add(pnlEast, BorderLayout.EAST);
		pnlEast.setLayout(new FlowLayout());
		pnlEast.setBackground(Color.BLACK);

		pnlRingen();
	}

	/**
	 * 
	 */
	private void pnlRingen() {
		pnlRingen = new JPanel();
		pnlEast.add(pnlRingen, BorderLayout.CENTER);
		pnlRingen.setBackground(Color.BLACK);
		Dimension d = new Dimension(200, 500);

		if (spel.getAantalSpelers() == 2) {
			pnl2Spelers(d);
		} else if (spel.getAantalSpelers() == 3) {
			pnl3Spelers(d);
		} else {
			pnl4Spelers(d);
		}
	}

	/**
	 * 
	 */
	private void pnl2Spelers(Dimension d) {
		JPanel pnl2Spelers = new JPanel();
		pnlRingen.add(pnl2Spelers, "2Spelers");
		pnl2Spelers.setLayout(new GridLayout(5, 2, 15, 15));
		pnl2Spelers.setBackground(Color.BLACK);
		pnl2Spelers.setPreferredSize(d);
		figuren = new JButton[10];
		for (int i = 0; i < figuren.length; i++) {
			figuren[i] = new JButton();
			figuren[i].setMargin(new Insets(0, 0, 0, 0));
			pnl2Spelers.add(figuren[i]);
			figuren[i].add(new Draw(spel.getSpeler(spelerNaam).getEigenRingen()
					.get(i)));
			figuren[i].addActionListener(this);
		}

	}

	/**
	 * 
	 */
	private void pnl3Spelers(Dimension d) {
		JPanel pnl3Spelers = new JPanel();
		pnlRingen.add(pnl3Spelers, "3Spelers");
		pnl3Spelers.setLayout(new GridLayout(5, 2, 15, 15));
		pnl3Spelers.setBackground(Color.BLACK);
		pnl3Spelers.setPreferredSize(d);
		figuren = new JButton[10];
		for (int i = 0; i < figuren.length; i++) {
			figuren[i] = new JButton();
			figuren[i].setMargin(new Insets(0, 0, 0, 0));
			pnl3Spelers.add(figuren[i]);
			figuren[i].add(new Draw(spel.getSpeler(spelerNaam).getEigenRingen()
					.get(i)));
			figuren[i].addActionListener(this);

		}
	}

	/**
	 * 
	 */
	private void pnl4Spelers(Dimension d) {
		JPanel pnl4Spelers = new JPanel();
		pnlRingen.add(pnl4Spelers, "4Spelers");
		pnl4Spelers.setLayout(new GridLayout(5, 2, 15, 15));
		pnl4Spelers.setBackground(Color.BLACK);
		pnl4Spelers.setPreferredSize(d);
		figuren = new JButton[5];
		JLabel[] rest = new JLabel[5];
		for (int i = 0; i < figuren.length; i++) {
			rest[i] = new JLabel();
			rest[i].setBackground(Color.BLACK);
			pnl4Spelers.add(rest[i]);

			figuren[i] = new JButton();
			figuren[i].setMargin(new Insets(0, 0, 0, 0));
			pnl4Spelers.add(figuren[i]);
			figuren[i].add(new Draw(spel.getSpeler(spelerNaam).getEigenRingen()
					.get(i)));
			figuren[i].addActionListener(this);
		}
	}

	private void pnlSouth() {
		pnlSouth = new JPanel();
		pnlSouth.setBackground(Color.BLACK);
		FlowLayout flowLayout = (FlowLayout) pnlSouth.getLayout();
		contentPane.add(pnlSouth, BorderLayout.PAGE_END);

		pnlButtons();

		pnlAutoPlay();
	}

	/**
	 * 
	 */
	private void pnlButtons() {
		pnlButtons = new JPanel();
		pnlSouth.add(pnlButtons);
		pnlButtons.setLayout(new CardLayout(0, 0));

		pnlZet();

		pnlHint();
	}

	private void pnlAutoPlay() {
		JPanel pnlAutoPlay = new JPanel();
		pnlSouth.add(pnlAutoPlay);
		pnlAutoPlay.setBackground(Color.BLACK);

		cbAutoPlay = new JCheckBox("Automatisch spelen");
		cbAutoPlay.setHorizontalAlignment(SwingConstants.CENTER);
		cbAutoPlay.addItemListener(this);
		pnlAutoPlay.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		pnlAutoPlay.add(cbAutoPlay);

		comboBoxBedenktijd = new JComboBox();
		comboBoxBedenktijd.setModel(new DefaultComboBoxModel(new String[] {
				"1", "3", "5", "10", "15", "20", "25", "30" }));
		comboBoxBedenktijd.setSelectedIndex(3);
		comboBoxBedenktijd.setToolTipText("Gewenst bedenktijd in sec.");
		pnlAutoPlay.add(comboBoxBedenktijd);
		comboBoxBedenktijd.addItemListener(this);
	}

	/**
	 * 
	 */
	private void pnlZet() {
		JPanel pnlZet = new JPanel();
		pnlZet.setBackground(Color.BLACK);
		pnlButtons.add(pnlZet, "name_73523295935322");
		pnlZet.setLayout(new GridLayout(1, 3, 50, 50));

		btnZetOpnieuw = new JButton("Zet opnieuw");
		btnZetOpnieuw.setEnabled(false);
		btnZetOpnieuw.setFont(new Font("Tahoma", Font.PLAIN, 17));
		pnlZet.add(btnZetOpnieuw);
		btnZetOpnieuw.addActionListener(this);

		btnHint = new JButton("Hint");
		btnHint.setFont(new Font("Tahoma", Font.PLAIN, 17));
		pnlZet.add(btnHint);
		btnHint.addActionListener(this);

		btnDoeZet = new JButton("Doe Zet");
		btnDoeZet.setEnabled(false);
		btnDoeZet.setFont(new Font("Tahoma", Font.PLAIN, 17));
		btnDoeZet.addActionListener(this);
		pnlZet.add(btnDoeZet);
	}

	/**
	 * 
	 */
	private void pnlHint() {
		JPanel pnlHint = new JPanel();
		pnlHint.setBackground(Color.BLACK);
		pnlButtons.add(pnlHint, "name_73527714825476");
		pnlHint.setLayout(new GridLayout(1, 2, 100, 0));

		btnAnnuleerHint = new JButton("Annuleer Hint");
		btnAnnuleerHint.setFont(new Font("Tahoma", Font.PLAIN, 17));
		pnlHint.add(btnAnnuleerHint);
		btnAnnuleerHint.addActionListener(this);

		btnZetHint = new JButton("Zet Hint");
		btnZetHint.setFont(new Font("Tahoma", Font.PLAIN, 17));
		pnlHint.add(btnZetHint);
		btnZetHint.addActionListener(this);
	}

	/**
	 * 
	 */
	private void refresh() {
		for (int i = 0; i < figuren.length; i++) {
			Ring ring = spel.getSpeler(spelerNaam).getEigenRingen().get(i);
			boolean bestaat = false;
			for (int j = 0; j < spel.getSpeler(spelerNaam)
					.getBeschikbareRingen().size(); j++) {
				if (spel.getSpeler(spelerNaam).getRing(ring.getKleur(),
						ring.getType()) != null) {
					bestaat = true;
					break;
				}
			}
			figuren[i].remove(figuren[i].getComponent(0));

			if (bestaat) {
				figuren[i].add(new Draw(ring));
				figuren[i].setEnabled(true);
			} else {
				ArrayList<Ring> legeArray = new ArrayList<Ring>();
				figuren[i].add(new Draw(legeArray));
				figuren[i].setEnabled(false);
			}

		}
		for (int i = 0; i < PC.VAKKEN; i++) {
			velden[i].remove(velden[i].getComponent(0));
			velden[i].add(new Draw(spel.getBord().getVak(i).getAlleRingen()));

		}
		aanDeBeurt(spel.getHuidigeSpeler().getNaam().equals(spelerNaam));
		revalidate();
	}

	public void aanDeBeurt(boolean aanDeBeurt) {
		for (int i = 0; i < figuren.length; i++) {
			figuren[i].setEnabled(aanDeBeurt);
		}
		btnHint.setEnabled(aanDeBeurt);
	}

	public void autoPlay(int bedenkTijd) {
		if (spel.getHuidigeSpeler().getNaam().equals(spelerNaam)
				&& spel.getMogelijkeZetten(spel.getHuidigeSpeler()).size() != 0) {
			aanDeBeurt(false);
			Strategie autoPlay = new GoedeStrategie();
			String zet = autoPlay.doeZet(spelerNaam, spel, bedenkTijd);
			boolean zetMogelijk = false;
			if (zet.equals("-1")) {
				List<Zet> mogelijkeZetten = spel.getMogelijkeZetten(spel
						.getHuidigeSpeler());
				int i = (int) Math.round(Math.random()
						* (mogelijkeZetten.size() - 1));
				tempRing = mogelijkeZetten.get(i).getRing();
				tempIndex = mogelijkeZetten.get(i).getVeld();
				zetMogelijk = true;
			} else {
				String[] zetParam = zet.split(",");
				int kleur = Integer.parseInt(zetParam[0]);
				int type = Integer.parseInt(zetParam[1]);
				int veld = Integer.parseInt(zetParam[2]);

				tempRing = new Ring(kleur, type, spelerNaam);
				tempIndex = veld;
				zetMogelijk = true;

			}
			if (zetMogelijk) {
				currClient.sendMessage(ClientProtocol.DO_MOVE
						+ ClientProtocol.DELIM + tempIndex
						+ ClientProtocol.DELIM + tempRing.getKleur()
						+ ClientProtocol.DELIM + tempRing.getType());
				for (int i = 0; i < PC.VAKKEN; i++) {
					velden[i].remove(velden[i].getComponent(0));
					velden[i].add(new Draw(spel.getBord().getVak(i)
							.getAlleRingen()));
				}
				revalidate();
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (btnDoeZet.equals(e.getSource())) {
			currClient.sendMessage(ClientProtocol.DO_MOVE
					+ ClientProtocol.DELIM + tempIndex + ClientProtocol.DELIM
					+ tempRing.getKleur() + ClientProtocol.DELIM
					+ tempRing.getType());
			revalidate();
			btnDoeZet.setEnabled(false);
			btnZetOpnieuw.setEnabled(false);

		} else if (btnZetOpnieuw.equals(e.getSource())) {
			refresh();
			btnDoeZet.setEnabled(false);
			btnZetOpnieuw.setEnabled(false);
		} else if (btnHint.equals(e.getSource())) {
			CardLayout cl = (CardLayout) pnlButtons.getLayout();
			cl.next(pnlButtons);
			Strategie hint = new GoedeStrategie();
			String zet = hint.doeZet(spelerNaam, spel, Integer
					.parseInt((String) (comboBoxBedenktijd.getSelectedItem())));
			if (zet.equals("-1")) {
				List<Zet> mogelijkeZetten = spel.getMogelijkeZetten(spel
						.getHuidigeSpeler());
				int i = (int) Math.round(Math.random()
						* (mogelijkeZetten.size() - 1));
				tempRing = mogelijkeZetten.get(i).getRing();
				tempIndex = mogelijkeZetten.get(i).getVeld();
			} else {
				String[] zetParam = zet.split(",");
				int kleur = Integer.parseInt(zetParam[0]);
				int type = Integer.parseInt(zetParam[1]);
				int veld = Integer.parseInt(zetParam[2]);

				tempRing = new Ring(kleur, type, spelerNaam);
				tempIndex = veld;

			}

			for (int i = 0; i < PC.VAKKEN; i++) {
				velden[i].setEnabled(false);
				velden[i].getComponent(0).setBackground(Color.BLACK);
				velden[i].setBackground(Color.BLACK);
			}
			
			velden[tempIndex].getComponent(0).setBackground(Color.WHITE);
			velden[tempIndex].setBackground(Color.BLACK);
			((Draw) velden[tempIndex].getComponent(0)).voegRingToe(tempRing);

		} else if (btnAnnuleerHint.equals(e.getSource())) {
			refresh();
			CardLayout cl = (CardLayout) pnlButtons.getLayout();
			cl.next(pnlButtons);
		} else if (btnZetHint.equals(e.getSource())) {
			currClient.sendMessage(ClientProtocol.DO_MOVE
					+ ClientProtocol.DELIM + tempIndex + ClientProtocol.DELIM
					+ tempRing.getKleur() + ClientProtocol.DELIM
					+ tempRing.getType());
			for (int i = 0; i < PC.VAKKEN; i++) {
				velden[i].remove(velden[i].getComponent(0));
				velden[i]
						.add(new Draw(spel.getBord().getVak(i).getAlleRingen()));
			}
			revalidate();
			CardLayout cl = (CardLayout) pnlButtons.getLayout();
			cl.next(pnlButtons);
		} else {
			for (int i = 0; i < figuren.length; i++) {
				figuren[i].getComponent(0).setBackground(Color.BLACK);
				if (figuren[i].equals(e.getSource())) {
					figuren[i].getComponent(0).setBackground(Color.WHITE);
					for (int j = 0; j < PC.VAKKEN; j++) {
						velden[j].setEnabled(false);
						velden[j].getComponent(0).setBackground(Color.BLACK);
						velden[j].setBackground(Color.BLACK);
						if (spel.geldigeZet(j, spel.getSpeler(spelerNaam)
								.getEigenRingen().get(i))) {
							velden[j].setEnabled(true);
							velden[j].getComponent(0)
									.setBackground(Color.WHITE);
							velden[j].setBackground(Color.WHITE);
							tempRing = spel.getSpeler(spelerNaam)
									.getEigenRingen().get(i);
						}
					}
				}
			}

			for (int i = 0; i < PC.VAKKEN; i++) {
				if (velden[i].equals(e.getSource())) {

					for (int j = 0; j < PC.VAKKEN; j++) {
						velden[j].setEnabled(false);
						velden[j].getComponent(0).setBackground(Color.BLACK);
						velden[j].setBackground(Color.BLACK);
					}
					((Draw) velden[i].getComponent(0)).voegRingToe(tempRing);

					tempIndex = i;
					btnZetOpnieuw.setEnabled(true);
					btnDoeZet.setEnabled(true);
				}

			}
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		refresh();
		if (autoPlay) {
			autoPlay(Integer.parseInt((String) (comboBoxBedenktijd
					.getSelectedItem())));
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (cbAutoPlay.equals(e.getSource())) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				autoPlay = true;
				int bedenkTijd = Integer.parseInt((String) (comboBoxBedenktijd
						.getSelectedItem()));
				autoPlay(bedenkTijd);
			} else {
				autoPlay = false;
				refresh();
			}
		}
	}

}