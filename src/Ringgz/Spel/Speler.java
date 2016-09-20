package Ringgz.Spel;

import java.util.ArrayList;

import java.util.List;

/**
 * Houdt voornamelijk de Ring-objecten van deze speler bij en de naam. Deze
 * klasse heeft 2 lijsten: beschikbare ringen en de eigen ringen. Bij de
 * beschikbare ringen-List kunnen er Ringen worden verwijderd, bij de eigen
 * ringen-List niet. Deze eigen ringen worden gebruikt om te berekenen wie de
 * meeste punten heeft.
 * 
 */
public class Speler {

	protected String naam;
	protected int[] kleuren;
	protected List<Ring> beschikbareRingen;
	private List<Ring> ringenSpeler;
	protected int nummerSpeler;

	/**
	 * Initieert het Speler object met zijn bijbehorende naam, spelernummer en
	 * de Lijsten die intern worden bijgehouden
	 * 
	 * @param naam
	 * @param nummerSpeler
	 */
	public Speler(String naam, int nummerSpeler, int[] kleuren) {
		this.nummerSpeler = nummerSpeler;
		this.naam = naam;
		this.kleuren = kleuren;
		beschikbareRingen = new ArrayList<Ring>();
		ringenSpeler = new ArrayList<Ring>();
		voegRingenToe();

	}

	/** hulpconstructor om een deepcopy van de speler te maken
	 */
	public Speler(String naam, int[] kleuren, List<Ring> beschikbareRingen,
			List<Ring> ringenSpeler, int nummerSpeler) {
		this.nummerSpeler = nummerSpeler;
		this.naam = naam;
		this.kleuren = kleuren;
		this.beschikbareRingen = new ArrayList<Ring>();
		this.ringenSpeler = new ArrayList<Ring>();
		for (int i = 0; i < beschikbareRingen.size(); i++) {
			this.beschikbareRingen.add(beschikbareRingen.get(i));
		}
		for (int i = 0; i < ringenSpeler.size(); i++) {
			this.ringenSpeler.add(ringenSpeler.get(i));
		}

	}

	/** maakt een deepcopy van deze speler
	 */
	public Speler clone() {
		Speler spelerClone = new Speler(naam, kleuren, beschikbareRingen,
				ringenSpeler, nummerSpeler);
		return spelerClone;
	}

	/**
	 * simpele query die de kleuren van deze speler geeft
	 * @return
	 */
	public int[] getKleuren() {
		return kleuren;
	}

	/**
	 * simpele query die het spelernummer geeft
	 */
	public int getNummerSpeler() {
		return nummerSpeler;
	}

	/**
	 * Voegt de ringen toe aan de beschikbare ringen-List
	 * 
	 * @param r
	 */
	public void voegRingtoe(Ring r) {
		beschikbareRingen.add(r);
	}

	/**
	 * Voegt ringen aan de ringenSpeler-list toe.
	 */
	public void voegRingenToe() {
		if (kleuren.length == 1) {
			for (int i = 0; i < 5; i++) {
				ringenSpeler.add(new Ring(kleuren[0], i, getNaam()));
			}
		} else if (kleuren.length == 2) {
			for (int i = 0; i < 5; i++) {
				ringenSpeler.add(new Ring(kleuren[0], i, getNaam()));
				ringenSpeler.add(new Ring(kleuren[1], i, getNaam()));
			}
		}
	}

	/**
	 * query die de naam opvraagt
	 * 
	 * @return
	 */
	public String getNaam() {
		return naam;

	}

	/**
	 * @return returnt de beschikbare ringen, die nog gebruikt kunnen worden in
	 *         het spel.
	 */
	public List<Ring> getBeschikbareRingen() {
		return beschikbareRingen;

	}

	/**
	 * @param tempRing
	 *            verwijdert de Ring uit de beschikbare ringen-lijst
	 */
	public void verwijderRing(Ring r) {
		beschikbareRingen.remove(r);
	}

	/**
	 * @param kleur
	 *            de kleur
	 * @param type
	 *            het type
	 * @return geeft het Ring-Object met dit kleur en type die deze speler nog
	 *         overheeft.
	 */
	public Ring getRing(int kleur, int type) {
		Ring tempRing = null;
		for (int i = 0; i < beschikbareRingen.size(); i++) {
			if (beschikbareRingen.get(i).getKleur() == kleur
					&& beschikbareRingen.get(i).getType() == type) {
				tempRing = beschikbareRingen.get(i);
			}
		}
		return tempRing;
	}

	/**
	 * @param index
	 *            de index van het Ring-object in deze lijst
	 * @return geeft een Ring-Object terug op basis van de index waar het Object
	 *         zich in deze lijst bevindt.
	 */
	public Ring getBeschikbareRing(int index) {
		return beschikbareRingen.get(index);

	}

	/**
	 * @return geeft een lijst
	 */
	public List<Ring> getEigenRingen() {
		return ringenSpeler;
	}

	public String toString() {
		String toString = "Speler " + naam
				+ " heeft de volgende ringen over: \n";
		for (Ring i : beschikbareRingen) {
			toString += i + "\n";
		}

		return toString;

	}
}
