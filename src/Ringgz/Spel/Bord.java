package Ringgz.Spel;

import java.util.ArrayList;
import java.util.List;

/**
 * De klasse Bord houdt alle Vak-Objecten bij. Deze klasse is verantwoordelijk
 * voor de meeste acties waar vakken met elkaar moeten kunnen communiceren in de
 * Model-klasse, oftewel de Spel-Klasse.
 * 
 */
public class Bord {
	private List<Vak> vakken;

	/**
	 * Deze methode wordt gebruikt om x- en y-coördinaten om te zetten naar het
	 * indexnummer van het corresponderende vak.
	 * 
	 * @param x
	 *            Het kolomnummer van dit vak.
	 * @param y
	 *            Het rijnummer van dit vak.
	 * @require x >= 0 && x<= 4 && y>=0 y<=4
	 * @ensure result >= 0 && result < PC.VAKKEN
	 * @return Geeft het indexnummer van dit vak terug hoe de indexnummers zijn
	 *         afgesproken in het protocol. Als er een ongeldige x en y wordt
	 *         ingevuld geeft deze methode -1 terug
	 */
	public int fromXY(int x, int y) {
		if (!(0 <= x && x <= 4) || !(0 <= y && y <= 4)) {
			return -1;
		} else {
			return y * 5 + x;

		}

	}

	/**
	 * Deze methode zet het indexnummer van het vak om naar de x- en
	 * y-coordinaten van het vak
	 * 
	 * @param index
	 *            Het indexnummer van het vak.
	 * @require index >= 0 && index < PC.VAKKEN
	 * @ensure x >= 0 && x<= 4 && y>=0 y<=4
	 * @return Geeft de x en y coördinaten terug in een int-Array, waarbij
	 *         int[0] x is en int[1] y. Als er een ongeldige index wordt
	 *         meeggegeven, geeft deze methode een array terug waarbij de x en
	 *         de y een waarde -1 heeft
	 */
	public int[] toXY(int index) {

		if (!(index >= 0 && index < PC.VAKKEN)) {
			int[] xy = { -1, -1 };
			return xy;

		} else {
			int y = (int) Math.floor(index / 5);
			int x = index - (5 * y);
			int[] xy = { x, y };
			return xy;
		}

	}

	/**
	 * Er wordt een nieuwe Bord geconstrueerd met 25 vakken
	 */
	public Bord() {
		vakken = new ArrayList<Vak>();
		for (int i = 0; i < PC.VAKKEN; i++) {
			vakken.add(new Vak(i));
		}

	}

	/**
	 * Deze constructor wordt alleen gebruikt om dit bord te deepcopy'en. Deze
	 * constructor wordt ook alleen aangesproken in de methode clone(). Deze
	 * methode is voornamelijk ook gemaakt om de constructor te overladen ipv te
	 * overschrijven. In de clone functie wordt ook alleen maar een lege
	 * ArrayList<Vak> doorgestuurd.
	 * 
	 * @require vakken.size() == 0
	 * @param vakken
	 *            een lege arrayList
	 */
	public Bord(ArrayList<Vak> vakken) {
		this.vakken = vakken;

	}

	/**
	 * Een methode die het Bord deepCopy't
	 * 
	 * @return geeft een exacte deepCopy van het Bord op dat moment
	 */
	public Bord clone() {
		ArrayList<Vak> tempVakken = new ArrayList<Vak>();
		Bord bordClone = new Bord(tempVakken);
		for (int i = 0; i < PC.VAKKEN; i++) {
			bordClone.getVakken().add(vakken.get(i).clone());
		}

		return bordClone;

	}

	/**
	 * Kijkt of er nog zetten mogelijk zijn op het bord
	 * 
	 * @require vakken.size() == PC.VAKKEN-1
	 * @ensure Als alle vakken vol zijn, dan wordt er false teruggegeven
	 * @return Geeft de Booleanwaarde true terug als er nog zetten mogelijk zijn
	 *         op het bord.
	 */

	public boolean zettenMogelijk() {
		int volleVakken = 0;
		for (int i = 0; i < PC.VAKKEN; i++) {
			if (vakken.get(i).isVol()) {
				volleVakken++;
			}
		}
		return volleVakken < PC.VAKKEN;

	}

	/**
	 * Controleert of het vak waar de gebruiker een steen wilt plaatsen qua
	 * kleur aangrenzend is aan een ander vak.
	 * 
	 * @require index >= 0 && index <PC.VAKKEN && kleur>=PC.ROOD &&
	 *          kleur<=PC.PAARS && user heeft deze kleur in het spel
	 * @param index
	 *            De index van het vak dat gecontroleerd wordt of het
	 *            aangrenzende vakken heeft met deze kleur.
	 * @param kleur
	 *            De kleur van de ring dat de gebruiker hier wil plaatsen.
	 * @return Geeft true terug als één van de aangrenzende vakken deze kleur
	 *         heeft.
	 */

	public boolean isAangrenzend(int index, int kleur) {
		Vak gekozenvak = getVak(index);
		List<Vak> aangrenzendeVakken = new ArrayList<Vak>();
		boolean aangrenzend = false;
		if (gekozenvak.isKleurAanwezig(kleur)) {
			aangrenzend = true;
		} else {
			int x = toXY(index)[0];
			int y = toXY(index)[1];

			int index1 = fromXY(x + 1, y);
			int index2 = fromXY(x - 1, y);
			int index3 = fromXY(x, y - 1);
			int index4 = fromXY(x, y + 1);

			if (index1 != -1) {
				aangrenzendeVakken.add(getVak(index1));
			}
			if (index2 != -1) {
				aangrenzendeVakken.add(getVak(index2));
			}
			if (index3 != -1) {
				aangrenzendeVakken.add(getVak(index3));
			}
			if (index4 != -1) {
				aangrenzendeVakken.add(getVak(index4));
			}
			for (int i = 0; i < aangrenzendeVakken.size(); i++) {
				aangrenzend = aangrenzend
						|| aangrenzendeVakken.get(i).isKleurAanwezig(kleur);
			}

		}

		return aangrenzend;

	}

	/**
	 * Controleert of er een blok met dezelfde kleur naast dit vak staat, als de
	 * Ring die op dit vak ook een blok is met deze kleur
	 * 
	 * @param index
	 *            De index waar de ring geplaatst wordt.
	 * @param kleur
	 *            De kleur van deze Ring.
	 * @require kleur >= PC.ROOD && kleur <=PC.PAARS && type >= PC.KLEINSTE_RING
	 *          && type <=PC.BLOK && index >= 0 && index < PC.VAKKEN
	 * @param type
	 *            Het type van deze ring
	 * @return geeft true terug als de ring die geplaatst moet worden een blok
	 *         is en als een aangrenzend vak ook een blok heeft van dezelfde
	 *         kleur.
	 */
	public boolean isAangrenzendBlok(int index, int kleur, int type) {
		List<Vak> aangrenzendeVakken = new ArrayList<Vak>();

		boolean aangrenzend = false;
		int x = toXY(index)[0];
		int y = toXY(index)[1];

		int index1 = fromXY(x + 1, y);
		int index2 = fromXY(x - 1, y);
		int index3 = fromXY(x, y - 1);
		int index4 = fromXY(x, y + 1);

		if (index1 != -1) {
			aangrenzendeVakken.add(getVak(index1));
		}
		if (index2 != -1) {
			aangrenzendeVakken.add(getVak(index2));
		}
		if (index3 != -1) {
			aangrenzendeVakken.add(getVak(index3));
		}
		if (index4 != -1) {
			aangrenzendeVakken.add(getVak(index4));
		}
		for (int i = 0; i < aangrenzendeVakken.size(); i++) {
			aangrenzend = aangrenzend
					|| (type == PC.BLOK
							&& aangrenzendeVakken.get(i).isTypeAanwezig(type) && aangrenzendeVakken
							.get(i).isKleurAanwezig(kleur));
		}

		return aangrenzend;

	}

	/**
	 * @return geeft de lijst met alle vakken terug.
	 */
	public List<Vak> getVakken() {
		return vakken;
	}

	/**
	 * @param index
	 *            De index van het vak dat je wilt terugkeren
	 * @return Returnt het vak met zijn ringen
	 */
	public Vak getVak(int index) {

		return vakken.get(index);

	}

}
