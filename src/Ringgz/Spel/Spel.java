package Ringgz.Spel;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import Ringgz.Exceptions.*;

/**
 * Deze klasse is verantwoordelijk voor de acties die gedaan worden in het spel,
 * zowel voor de Server en de Client. Dus dit is de Model-Klasse binnen de MVC.
 * 
 */
public class Spel extends Observable {

	private Bord bord;
	private int aantalSpelers;
	private Speler[] spelers;
	private int huidigeSpeler = 0;
	public int eersteZet = 0;
	private List<Zet> zetten;
	private boolean gameOver = false;

	/**
	 * In deze constructor worden alle Ringen, de spelers en het bord gecreeërd.
	 * Deze constructor wordt aangesproken op de server, omdat deze bepaalt waar
	 * de beginsteen moet komen
	 * 
	 * @require aantalSpelers >=PC.MIN_SPELERS && aantalSpelers <=PC.MAX_SPELERS
	 *          && spelerNamen.length == aantalSpelers
	 * @param aantalspelers
	 *            Het aantal spelers die in dit spel zitten
	 * @param spelerNamen
	 *            De spelernamen.
	 */
	public Spel(int aantalSpelers, String[] spelerNamen) {

		bord = new Bord();
		this.aantalSpelers = aantalSpelers;
		setSpelers(aantalSpelers, spelerNamen);
		setEersteZet();
		huidigeSpeler = (int) (Math.round(Math.random()
				* (spelerNamen.length - 1)));

	}

	/**
	 * Deze constructor doet bijna hetzelfde als de constructor hiervoor, het
	 * enige verschil is dat de eersteZet hier bepaald kan worden ipv dat het
	 * random wordt bepaald. Deze constructor wordt lokaal op de Client
	 * aangesproken.
	 * 
	 * @param aantalSpelers
	 *            Het aantal spelers die in dit spel zitten
	 * @param spelerNamen
	 *            De spelernamen.
	 * @require 6<=eersteZet<9 || 11<=eersteZet<14 || 16<=eersteZet<19
	 *          &&aantalSpelers >=PC.MIN_SPELERS && aantalSpelers
	 *          <=PC.MAX_SPELERS && spelerNamen.length == aantalSpelers
	 * @param eersteZet
	 *            de index waar de beginsteen moet komen
	 */
	public Spel(int aantalSpelers, String[] spelerNamen, int eersteZet) {
		bord = new Bord();
		this.eersteZet = eersteZet;
		this.aantalSpelers = aantalSpelers;
		setSpelers(aantalSpelers, spelerNamen);
		setEersteZet();
	}

	/**
	 * Hulpconstructor om het complete spel te deepcopy'en, alle primitieve
	 * types worden in het nieuwe Spel-object gekopieeërd
	 * 
	 * @param bord
	 *            Het huidige bord
	 * @param aantalSpelers
	 *            het aantal Spelers
	 * @param spelers
	 *            De speler-array
	 * @param huidigeSpeler
	 *            De speler die op dat moment aan de beurt is
	 * @param eersteZet
	 *            De eerste zet
	 */
	public Spel(Bord bord, int aantalSpelers, Speler[] spelers,
			int huidigeSpeler, int eersteZet) {
		this.bord = bord;
		this.aantalSpelers = aantalSpelers;
		this.huidigeSpeler = huidigeSpeler;
		this.eersteZet = eersteZet;
		this.spelers = spelers;

	}

	/**
	 * Via deze methode kan manueel worden bepaald dat het game over is
	 * 
	 * @param t
	 */
	public void setGameOver(boolean t) {
		gameOver = t;
	}

	/**
	 * Via deze methode wordt er manueel bepaald wie de volgende speler is, deze
	 * methode wordt aangeroepen, nadat de client het commando next_move heeft
	 * ontvangen
	 * 
	 * @require getSpeler(naam) == null
	 * @ensure huidigeSpeler = getSpeler(naam)
	 * @param naam
	 *            de naam van de volgende speler
	 */
	public void setSpeler(String naam) {
		Speler s = getSpeler(naam);
		huidigeSpeler = s.getNummerSpeler();
		setChanged();
		notifyObservers();
	}

	/**
	 * Deze methode wordt gebruikt om manueel in te stellen wie de volgende
	 * speler is. Deze methode wordt vanuit de server aangesproken,omdat er
	 * verder in het spel kan voorkomen dat setVolgendeSpeler() niet meer
	 * overeenkomt, omdat die speler geen zet meer kan doen
	 * 
	 * @ensure nummerSpeler < getAantalSpelers() && nummerSpeler >= 0
	 * @ensure huidigeSpeler = getSpeler(naam)
	 * @param nummerSpeler
	 *            De nummer van deze speler
	 */
	public void setSpeler(int nummerSpeler) {
		huidigeSpeler = nummerSpeler % aantalSpelers;
		setChanged();
		notifyObservers();
	}

	/**
	 * Maakt een deepcopy van het spel
	 * 
	 * @ensure spel.state == spelclone.state
	 */
	public Spel clone() {
		ArrayList<Speler> al = new ArrayList<Speler>();
		for (Speler s : spelers) {
			al.add(s.clone());
		}
		Speler[] tempArray = new Speler[spelers.length];
		Speler[] spelerCloneArray = al.toArray(tempArray);

		Spel spelClone = new Spel(bord.clone(), aantalSpelers,
				spelerCloneArray, huidigeSpeler, eersteZet);
		return spelClone;
	}

	/**
	 * Maakt een nieuwe index aan d.m.v een modulo voor welke speler de volgende
	 * zet moet doen.
	 * 
	 * @ensure old.getHuidigeSpeler().equals(this.getHuidigeSpeler())
	 * 
	 */
	public void setVolgendeSpeler() {
		huidigeSpeler = (huidigeSpeler + 1) % aantalSpelers;
		;
	}

	/**
	 * Een simpele query die het Speler-Object opvraagt van degene die nu aan de
	 * beurt is.
	 * 
	 * @return de huidige speler
	 */
	public Speler getHuidigeSpeler() {
		return spelers[huidigeSpeler];
	}

	/**
	 * Deze methode zet de beginsteen op het bord, zowel aan de Client als aan
	 * de server kant. Bij de server bepaalt hij hem intern nog willekeurig. Bij
	 * de client zet hij de beginsteen neer op de index die de client heeft
	 * doorgekregen van de server
	 * 
	 * @ensure beginsteen staat op index 6, 7, 8, 11, 12, 13, 16, 17 of 18
	 */
	private void setEersteZet() {
		int[] mogelijkeEersteZetten = { 6, 7, 8, 11, 12, 13, 16, 17, 18 };
		List<Integer> kleuren = new ArrayList<Integer>();
		kleuren.add(0);
		kleuren.add(1);
		kleuren.add(2);
		kleuren.add(3);
		if (eersteZet == 0) {
			int randomIndex = (int) Math.round(Math.random() * 8);
			eersteZet = mogelijkeEersteZetten[randomIndex];
		}
		int type = PC.GROOTSTE_RING;
		int kleur = PC.PAARS;
		while (type >= PC.KLEINSTE_RING && kleur >= PC.ROOD) {
			Ring tempRing = new Ring(kleur, type, "beginRing");

			bord.getVak(eersteZet).setEersteRingen(tempRing);
			type--;
			kleur--;

		}

	}

	/**
	 * Deze methode initieert alle spelers met hun bijbehorende ringen/blokken.
	 * 
	 * @param aantalSpelers
	 * @param spelerNamen
	 * @require aantalSpeler == spelerNamen.length && aantalSpelers
	 *          >=PC.MIN_SPELERS && aantalSpelers <=PC.MAX_SPELERS
	 */
	private void setSpelers(int aantalSpelers, String[] spelerNamen) {

		// ///////////////////////////////////////////////
		// //////////////////// 2 spelers ////////////////
		// ///////////////////////////////////////////////
		if (aantalSpelers == 2) {
			int[] kleuren1 = { PC.ROOD, PC.GROEN };
			int[] kleuren2 = { PC.GEEL, PC.PAARS };
			Speler[] spelersTemp = { new Speler(spelerNamen[0], 0, kleuren1),
					new Speler(spelerNamen[1], 1, kleuren2) };
			for (int i = 1; i <= 3; i++) {

				for (int type = PC.KLEINSTE_RING; type <= PC.BLOK; type++) {
					for (int kleur = PC.ROOD; kleur <= PC.GROEN; kleur++) {
						spelersTemp[0].voegRingtoe(new Ring(kleur, type,
								spelersTemp[0].getNaam()));
					}
					for (int kleur = PC.GEEL; kleur <= PC.PAARS; kleur++) {
						spelersTemp[1].voegRingtoe(new Ring(kleur, type,
								spelersTemp[1].getNaam()));
					}

				}
			}
			spelers = spelersTemp;
		}
		// ///////////////////////////////////////////////
		// //////////////////// 3 spelers ////////////////
		// ///////////////////////////////////////////////

		if (aantalSpelers == 3) {
			int[] kleuren1 = { PC.ROOD, PC.PAARS };
			int[] kleuren2 = { PC.GROEN, PC.PAARS };
			int[] kleuren3 = { PC.GEEL, PC.PAARS };
			Speler[] spelersTemp = { new Speler(spelerNamen[0], 0, kleuren1),
					new Speler(spelerNamen[1], 1, kleuren2),
					new Speler(spelerNamen[2], 2, kleuren3) };
			for (int i = 1; i <= 3; i++) {

				for (int type = PC.KLEINSTE_RING; type <= PC.BLOK; type++) {
					spelersTemp[0].voegRingtoe(new Ring(PC.ROOD, type,
							spelersTemp[0].getNaam()));

					spelersTemp[1].voegRingtoe(new Ring(PC.GROEN, type,
							spelersTemp[1].getNaam()));

					spelersTemp[2].voegRingtoe(new Ring(PC.GEEL, type,
							spelersTemp[2].getNaam()));
				}
			}
			for (int type = PC.KLEINSTE_RING; type <= PC.BLOK; type++) {
				int kleur = PC.PAARS;
				spelersTemp[0].voegRingtoe(new Ring(kleur, type, spelersTemp[0]
						.getNaam()));
				spelersTemp[1].voegRingtoe(new Ring(kleur, type, spelersTemp[1]
						.getNaam()));
				spelersTemp[2].voegRingtoe(new Ring(kleur, type, spelersTemp[2]
						.getNaam()));

			}
			spelers = spelersTemp;
		}
		// ///////////////////////////////////////////////
		// //////////////////// 4 spelers ////////////////
		// ///////////////////////////////////////////////

		if (aantalSpelers == 4) {
			int[] kleuren1 = { PC.ROOD };
			int[] kleuren2 = { PC.GROEN };
			int[] kleuren3 = { PC.GEEL };
			int[] kleuren4 = { PC.PAARS };
			Speler[] spelersTemp = { new Speler(spelerNamen[0], 0, kleuren1),
					new Speler(spelerNamen[1], 1, kleuren2),
					new Speler(spelerNamen[2], 2, kleuren3),
					new Speler(spelerNamen[3], 3, kleuren4) };
			for (int i = 1; i <= 3; i++) {

				for (int type = PC.KLEINSTE_RING; type <= PC.BLOK; type++) {
					spelersTemp[0].voegRingtoe(new Ring(PC.ROOD, type,
							spelersTemp[0].getNaam()));

					spelersTemp[1].voegRingtoe(new Ring(PC.GROEN, type,
							spelersTemp[1].getNaam()));

					spelersTemp[2].voegRingtoe(new Ring(PC.GEEL, type,
							spelersTemp[2].getNaam()));

					spelersTemp[3].voegRingtoe(new Ring(PC.PAARS, type,
							spelersTemp[3].getNaam()));

				}
			}
			spelers = spelersTemp;

		}

	}

	/**
	 * @return Het bord wordt gereturnd met al zijn eigenschappen.
	 */
	public Bord getBord() {
		return bord;
	}

	/**
	 * 
	 * Voegt alle controleermethodes bij elkaar. Hij controleert of dit type
	 * ring al aanwezig is op dit vak of dit vak niet vol is, of dit vak
	 * aangrenzend is aan andere vakken met ringen en of een blok niet aan een
	 * blok met dezelfde kleur grenst. Hij returnt true als het een geldige zet
	 * is.
	 * 
	 * @param index
	 *            De index van het vak, waar de ring moet komen
	 * @param ring
	 *            Het ring-Object dat je hier wilt neerzetten.
	 * @return
	 */

	public boolean geldigeZet(int index, Ring r) {
		int type = r.getType();
		int kleur = r.getKleur();

		return geldigeZet(index, type, kleur);
	}

	/**
	 * 
	 * Voegt alle controleermethodes bij elkaar. Hij controleert of dit type
	 * ring al aanwezig is op dit vak of dit vak niet vol is, of dit vak
	 * aangrenzend is aan andere vakken met ringen en of een blok niet aan een
	 * blok met dezelfde kleur grenst. Hij returnt true als het een geldige zet
	 * is.
	 * 
	 * @param index
	 *            De index van het vak, waar de ring moet komen
	 * @param type
	 *            Het type ring die je hier wilt neerzetten.
	 * @param kleur
	 *            De kleur ring je hier wilt neerzetten
	 * @return
	 */
	public boolean geldigeZet(int index, int type, int kleur) {

		boolean blok;
		if (type == PC.BLOK) {
			blok = bord.getVak(index).isLeeg();
		} else {
			blok = true;
		}

		return blok && !bord.getVak(index).isTypeAanwezig(type)
				&& !bord.getVak(index).isVol()
				&& bord.isAangrenzend(index, kleur)
				&& !bord.isAangrenzendBlok(index, kleur, type);

	}

	/**
	 * Deze methode verplaatst de Ring naar de gewenste index. Er wordt
	 * gecontroleert of het een geldige zet is en of het de juiste speler is die
	 * aan de beurt is. Deze methode wordt alleen toegepast door de Server, want
	 * hij moet controleren of het een goede zet is. Dit is niet de
	 * verantwoordelijkheid van de client, hiervoor gebruiken we doeZetSpeler().
	 * Als de zet goed is, is de volgende speler aan de beurt.
	 * 
	 * @require getSpeler(naam) != null && kleur >= PC.ROOD && kleur <=PC.PAARS
	 *          && type >= PC.KLEINSTE_RING && type <= PC.BLOK && index >= 0 &&
	 *          index <= PC.VAKKEN
	 * @ensure if(doeZet(naam,kleur,type,index)) !old.getBord() ==
	 *         this.getBord())
	 * 
	 * @param naam
	 *            De naam van de speler
	 * @param kleur
	 *            De kleur die verplaatst moet worden
	 * @param type
	 *            Het type dat verplaatst moet worden
	 * @param index
	 *            De index waar de ring naar verplaatst moet worden
	 * @return true als de zet goed is
	 */

	public boolean doeZet(String naam, int kleur, int type, int index) {
		Speler speler = getSpeler(naam);
		Ring ring = speler.getRing(kleur, type);
		boolean setMove = false;
		if (geldigeZet(index, type, kleur) && speler.equals(getHuidigeSpeler())
				&& !isGameOver()) {
			try {
				bord.getVak(index).voegRingToe(ring);
				speler.verwijderRing(ring);
				setVolgendeSpeler();
				setMove = true;
			} catch (RingNietBeschikbaarException e) {
				// TODO Auto-generated catch block
				System.err.println(e.getMessage());
			}
		}
		return setMove;
	}

	/**
	 * Zelfde methode als de vorige methode, alleen met minder
	 * verantwoordelijkheden, want deze methoden controleert niet of het een
	 * geldige zet is, er mag vanuit worden gegaan dat de server dit controleert
	 * 
	 * @require kleur >= PC.ROOD && kleur <=PC.PAARS && type >= PC.KLEINSTE_RING
	 *          && type <= PC.BLOK && index >= 0 && index <= PC.VAKKEN
	 * @ensure if(doeZet(naam,kleur,type,index)) !old.getBord() ==
	 *         this.getBord())
	 * @param kleur
	 *            de kleur van de ring
	 * @param type
	 *            het type van de ring
	 * @param index
	 *            de index waar de ring moet komen te staan
	 * @return
	 */
	public boolean doeZetSpeler(int kleur, int type, int index) {
		Speler speler = getHuidigeSpeler();
		Ring ring = speler.getRing(kleur, type);
		boolean setMove = false;
		try {
			bord.getVak(index).voegRingToe(ring);
			speler.verwijderRing(ring);
			setMove = true;

		} catch (RingNietBeschikbaarException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
		}
		return setMove;
	}

	/**
	 * overlaad de methode doeZet met één Ring-parameter ipv een kleur-int en
	 * een type-int. Zie doeZet(String naam, int kleur, int type, int index)
	 */

	public boolean doeZet(String naam, Ring r, int index) {
		return doeZet(naam, r.getKleur(), r.getType(), index);
	}

	/**
	 * Deze methode is aangemaakt om te kijken of een speler nog een zet kan
	 * doen en op deze grond bepalen of het spel over is
	 * 
	 * @require s != null
	 * @param s
	 *            Het Speler object
	 * @return returnt true als deze speler nog zetten kan doen
	 */
	public boolean zetMogelijk(Speler s) {
		boolean zetMogelijk = false;
		for (Ring r : s.beschikbareRingen) {
			for (int i = 0; i < PC.VAKKEN; i++) {
				if (geldigeZet(i, r)) {
					zetMogelijk = true;
					break;
				}
			}

		}
		return zetMogelijk;
	}

	/**
	 * overlaad de methode zetMogelijk(Speler s)
	 * 
	 * @require s != null
	 * @param s
	 *            De naam van de speler
	 * 
	 */

	public boolean zetMogelijk(String s) {
		Speler speler = getSpeler(s);

		return zetMogelijk(speler);
	}

	/**
	 * @require speler != null
	 * @param speler
	 *            Het speler object
	 * @return een lijst met Zet-objecten welke zetten er nog mogelijk zijn voor
	 *         deze speler
	 */
	public List<Zet> getMogelijkeZetten(Speler speler) {
		zetten = new ArrayList<Zet>();
		Spel s = this.clone();
		for (int i = 0; i < PC.VAKKEN; i++) {
			for (int j = 0; j < speler.getBeschikbareRingen().size(); j++) {
				if (s.geldigeZet(i, speler.getBeschikbareRing(j))) {
					Zet zet = new Zet(i, speler.getBeschikbareRing(j));
					zetten.add(zet);
				}
			}
		}

		return zetten;
	}

	/**
	 * Controleert of het spel afgelopen is
	 * 
	 * @ensure !isGameOver als het geen game over is volgens de spelregels
	 * @return true als het spel over is
	 */
	public boolean isGameOver() {
		if (aantalSpelers == 2) {
			gameOver = (!zetMogelijk(spelers[0]) && !zetMogelijk(spelers[1]))
					|| !getBord().zettenMogelijk() || gameOver;
		}
		if (aantalSpelers == 3) {
			gameOver = (!zetMogelijk(spelers[0]) && !zetMogelijk(spelers[1]) && !zetMogelijk(spelers[2]))
					|| !getBord().zettenMogelijk() || gameOver;
		}
		if (aantalSpelers == 4) {
			gameOver = (!zetMogelijk(spelers[0]) && !zetMogelijk(spelers[1])
					&& !zetMogelijk(spelers[2]) && !zetMogelijk(spelers[3]))
					|| !getBord().zettenMogelijk() || gameOver;
		}
		return gameOver;
	}

	/**
	 * @param spelerInt
	 *            de index van de speler
	 * @return returnt een Speler-Object a.h.v de index
	 */
	public Speler getSpeler(int spelerInt) {

		return spelers[spelerInt];
	}

	/**
	 * simpele query die het aantal spelers returnt
	 * 
	 * @return
	 */
	public int getAantalSpelers() {
		return aantalSpelers;
	}

	/**
	 * @return returnt de speler-array
	 */
	public Speler[] getSpelers() {
		return spelers;
	}

	/**
	 * Vraagt een Speler-Object op
	 * 
	 * @param naam
	 *            De naam van deze speler
	 * @return
	 */
	public Speler getSpeler(String naam) {
		Speler speler = null;
		for (int i = 0; i < spelers.length; i++) {
			if (spelers[i].getNaam().equals(naam)) {
				speler = spelers[i];
			}
		}
		return speler;
	}

	/**
	 * Weergave van de punten van alle spelers.
	 * 
	 * @return returnt een String-representatie van het totaal aantal punten per
	 *         speler
	 */
	public String punten() {
		String punten = "Het totaal aantal punten is:\n";
		for (Speler i : spelers) {
			punten += i.getNaam() + ": " + spelerPunten(i) + "\n";
		}
		return punten;
	}

	/**
	 * geeft het Speler-object terug van de speler die de winnaar op dat moment
	 * de winnaar is.
	 * 
	 * @require isGameOver()
	 * @return geeft het Speler-object terug van de speler die de winnaar op dat
	 *         moment de winnaar is. Bij gelijkspel is het resultaat null
	 */
	public Speler isWinnaar() {
		Speler speler = null;

		int grootste = 0;
		for (Speler s : spelers) {

			if (spelerPunten(s) > spelerPunten(speler)) {
				speler = s;
				grootste = spelerPunten(s);
			} else if ((speler != null)
					&& spelerPunten(s) == spelerPunten(speler)
					&& grootste == spelerPunten(s)) {
				if (s.getBeschikbareRingen().size() < speler
						.getBeschikbareRingen().size()) {
					speler = s;
				}
				if (s.getBeschikbareRingen().size() == speler
						.getBeschikbareRingen().size()) {
					speler = null;
				}

			}
			;
		}

		return speler;
	}

	/**
	 * Vraagt de punten van een individuele speler op.
	 * 
	 * @require s != null
	 * @param s
	 *            het Speler-Object waar je de punten van wilt weten
	 * @return return een Integer-waarde hoeveel punten deze speler heeft.
	 */
	public int spelerPunten(Speler s) {
		int punten = 0;

		if (!(s == null)) {

			for (int i = 0; i < PC.VAKKEN; i++) {
				for (int j : s.getKleuren()) {
					if (bord.getVak(i).winnaarVak() == j
							&& !(aantalSpelers == 3 && bord.getVak(i)
									.winnaarVak() == 3))
						punten++;
				}
			}

		}

		return punten;

	}

}
