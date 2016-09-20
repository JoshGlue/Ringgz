package Ringgz.Spel;

import java.util.ArrayList;

import java.util.List;

import Ringgz.Exceptions.*;

/**
 * De geplaatste Ring-Objecten worden in Vak-Objecten opgeslagen.
 * 
 */
public class Vak {

	/**
	 * Bevat alle ringen die op dit vak staan
	 */
	private List<Ring> ringen;
	private int index;

	/**
	 * @param index
	 *            is de waarde van de locatie van dit vak waar het zich op het
	 *            Bord bevindt.
	 * @require index >=0 && index <=24
	 */
	public Vak(int index) {
		this.ringen = new ArrayList<Ring>();
		this.index = index;
	}

	public Vak clone() {
		Vak vakClone = new Vak(index);
		for (int i = 0; i < ringen.size(); i++) {
			vakClone.setEersteRingen(ringen.get(i));
		}

		return vakClone;

	}

	/**
	 * @return Geeft de index terug.
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @return Returnt een lijst van alle Ring-Objecten
	 */
	public List<Ring> getAlleRingen() {
		return ringen;
	}

	/**
	 * Voegt een Ring-Object toe aan dit vak. Gooit een
	 * RingNietBeschikbaarException, als dit Ring-Object niet bestaat
	 * 
	 * @param r
	 *            Het Ring-Object dat toegevoegd moet worden.
	 * @throws RingNietBeschikbaarException
	 * 
	 */
	public boolean voegRingToe(Ring r) throws RingNietBeschikbaarException {
		boolean toegevoegd = false;

		if (r != null) {
			if (ringen.size() == 0) {
				ringen.add(r);
			} else {
				for (int i = 0; i < ringen.size() && !toegevoegd; i++) {
					if (r.getType() > ringen.get(i).getType()) {
						ringen.add(i, r);
						toegevoegd = true;
					}
				}
				if (!toegevoegd) {
					ringen.add(r);
					toegevoegd = true;
				}
			}
			return toegevoegd;
		} else
			throw new RingNietBeschikbaarException();

	}

	/**
	 * @param r
	 *            Deze methode controleert niks, hij voegt alleen stomweg de
	 *            eerste ringen toe aan dit vak bij de eerste zet.
	 */
	public void setEersteRingen(Ring r) {
		ringen.add(r);

	}

	/**
	 * Deze functie wordt voornamelijk gebruikt voor de kopie van de Client om
	 * zijn strategie door te rekenen, want hij moet de Ring's ook weer
	 * verwijderen
	 * 
	 * @param r
	 *            Het Ring-Object dat verwijderd moet worden.
	 */
	public void verwijderRing(Ring r) {
		ringen.remove(r);
	}

	/**
	 * @return Vraagt het aantal Ringen op, op dit vak
	 */
	public int aantal() {
		return ringen.size();
	}

	/**
	 * @return true als het vak leeg is.
	 */
	public boolean isLeeg() {

		return aantal() == 0;
	}

	/**
	 * @param spelers
	 *            De speler-Array. wordt geinitieerd in de spel-klasse
	 * @return een Speler-Object van de winnaar van dit vak. returnt null als
	 *         het gelijkspel is.
	 */
	public int winnaarVak() {
		int kleur1 = 0;
		int kleur2 = 0;
		int kleur3 = 0;
		int kleur4 = 0;

		for (int j = 0; j < ringen.size(); j++) {

			if (!ringen.get(j).isBlok()) {
				switch (ringen.get(j).getKleur()) {
				case 0:
					kleur1++;
					break;
				case 1:
					kleur2++;
					break;
				case 2:
					kleur3++;
					break;
				case 3:
					kleur4++;
					break;

				}

			}
		}
		int winnaarVak = -1;

		if (kleur1 > kleur2 && kleur1 > kleur3 && kleur1 > kleur4) {
			winnaarVak = 0;
		}

		if (kleur2 > kleur1 && kleur2 > kleur3 && kleur2 > kleur4) {
			winnaarVak = 1;
		}
		if (kleur3 > kleur1 && kleur3 > kleur2 && kleur3 > kleur4) {
			winnaarVak = 2;
		}
		if (kleur4 > kleur1 && kleur4 > kleur2 && kleur4 > kleur3) {

			winnaarVak = 3;
		}

		return winnaarVak;

	}

	/**
	 * @return Returnt true als er al 4 ringen op dit vak liggen of als er een
	 *         blok aanwezig is op dit vak.
	 */
	public boolean isVol() {
		boolean vol = ringen.size() == 4;
		for (int i = 0; i < aantal(); i++) {
			if (ringen.get(i).isBlok()) {
				vol = true;
				break;
			}
		}
		return vol;

	}

	/**
	 * Controleert of het type al aanwezig is op dit vak
	 * 
	 * @param type
	 *            het type dat gezocht moet worden
	 * @return Returnt true als het type aanwezig is op dit vak
	 */
	public boolean isTypeAanwezig(int type) {
		boolean isAanwezig = false;
		for (int i = 0; i < aantal(); i++) {
			if (ringen.get(i).getType() == type) {
				isAanwezig = true;
				break;
			}
		}
		return isAanwezig;

	}

	/**
	 * Controleert of de kleur al aanwezig is op dit vak
	 * 
	 * @param type
	 *            de kleur dat gezocht moet worden
	 * @return Returnt true als de kleur aanwezig is op dit vak
	 */
	public boolean isKleurAanwezig(int kleur) {
		boolean isAanwezig = false;
		for (int i = 0; i < aantal(); i++) {
			if (ringen.get(i).getKleur() == kleur) {
				isAanwezig = true;
				break;
			}
		}
		return isAanwezig;

	}

	public String toString() {
		String alleStringen = "Vak " + getIndex()
				+ " heeft de volgende ringen \n";
		for (int j = 0; j < aantal(); j++) {
			alleStringen += "kleur: " + ringen.get(j).getKleur() + " type: "
					+ ringen.get(j).getType() + " eigenaar: "
					+ ringen.get(j).getNaam() + "\n";
		}

		return alleStringen;

	}

}
