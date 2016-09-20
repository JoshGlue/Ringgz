package Ringgz.Spel;

import java.util.ArrayList;
import java.util.Calendar;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Onze enige strategie die we hebben gemaakt en ook in het toernooi hebben
 * gebruikt. Het idee hierachter is dat we bij alle mogelijke volgende zetten
 * zoveel mogelijk situaties wat hierna kan gebeuren simuleren tot het gameover
 * is en welke volgende zet de meeste mogelijke situaties wint, die wordt
 * teruggegeven als "beste zet". Theoretisch gezien is dit geen goede strategie,
 * want alle situaties worden willekeurig bepaald. Hierdoor kan je theoretisch
 * gezien alleen maar situaties krijgen die je wint of alleen maar verliest,
 * terwijl dit misschien niet de beste zet is. Echter is het niet mogelijk om
 * elke vervolgsituatie na te bootsen, want dit zijn te veel situaties in een
 * korte tijdspanne(25 seconden), dus zijn we maar uitgegaan van het principe:
 * 'the wisdom of crowds'; je vraagt heel veel mensen hoeveel persoon x weegt,
 * bijna iedereen zit er naast, maar gemiddeld genomen, komt het goede gewicht
 * eruit. Bij het testen van de strategie(zelf tegen de strategie spelen en twee
 * strategieën tegen elkaar laten spelen), kwamen we tot de conclusie dat deze
 * strategie wel de goede zetten terug geeft, omdat we geen enkele keer hadden
 * gewonnen en bij strategie-strategie, kwam er vaak een bijna-gelijkspel uit.
 * En bovenal hebben we met deze strategie met vlag en wimpel het BIT-toernooi
 * gewonnen.
 */
public class GoedeStrategie implements Strategie {
	HashMap<String, Integer> waardes;
	HashMap<String, Integer> voorkomen;
	String naam;
	Spel spel;
	int k = 0;
	int mogelijkeZetten;

	/**
	 * default constructor
	 */
	public GoedeStrategie() {

	}

	/**
	 * Berekent de beste zet.
	 * 
	 * @require spel != null & spel.getNaam(naam) != null
	 * @ensure result == besteZet
	 * @return returnt een String waarde met het format: kleur,type,index
	 */
	public String doeZet(String naam, Spel spel, int bedenkTijd) {
		int timeout = bedenkTijd * 1000;
		waardes = new HashMap<String, Integer>();
		voorkomen = new HashMap<String, Integer>();
		Calendar calendar = new GregorianCalendar();
		long begin = calendar.get(Calendar.MILLISECOND)
				+ calendar.get(Calendar.SECOND) * 1000
				+ calendar.get(Calendar.MINUTE) * 60 * 1000;
		long eind;

		Speler speler = spel.getSpeler(naam);
		mogelijkeZetten = spel.getMogelijkeZetten(speler).size();
		do {

			for (int i = 0; i < mogelijkeZetten; i++, k++) {
				boolean eersteZet = true;
				Spel spelClone = spel.clone();
				speler = spelClone.getSpeler(naam);
				String firstMove = null;
				while (!spelClone.isGameOver()) {

					int randomIndex = 0;
					List<Zet> mogelijkeZetten = spelClone
							.getMogelijkeZetten(spelClone.getHuidigeSpeler());

					if (eersteZet)
						randomIndex = i;
					else {
						randomIndex = (int) Math.round(Math.random()
								* (mogelijkeZetten.size() - 1));
					}

					if (spelClone.zetMogelijk(spelClone.getHuidigeSpeler())) {
						Zet zet = mogelijkeZetten.get(randomIndex);
						if (eersteZet
								&& spelClone.doeZet(spelClone
										.getHuidigeSpeler().getNaam(), zet
										.getRing(), zet.getVeld())) {
							firstMove = zet.getRing().getKleur() + ","
									+ zet.getRing().getType() + ","
									+ zet.getVeld();
							if (voorkomen.containsKey(firstMove)) {
								int aantal = voorkomen.get(firstMove);
								voorkomen.remove(firstMove);
								voorkomen.put(firstMove, aantal + 1);
							} else {
								voorkomen.put(firstMove, 1);
							}

							eersteZet = false;
						} else {
							spelClone.doeZet(spelClone.getHuidigeSpeler()
									.getNaam(), zet.getRing(), zet.getVeld());
						}

					} else {
						spelClone.setVolgendeSpeler();
					}
				}
				if (spelClone.isWinnaar() != null
						&& spelClone.isWinnaar().equals(speler)) {
					Set<String> keys = waardes.keySet();
					if (keys.contains(firstMove)) {
						int aantal = waardes.get(firstMove);
						waardes.remove(firstMove);
						waardes.put(firstMove, aantal + 1);
					} else {
						waardes.put(firstMove, 1);
					}
				}
			}
			Calendar calendar2 = new GregorianCalendar();
			eind = calendar2.get(Calendar.MILLISECOND)
					+ calendar2.get(Calendar.SECOND) * 1000
					+ calendar2.get(Calendar.MINUTE) * 60 * 1000;
		} while (eind - begin < timeout);

		long duur = eind - begin;
		System.out.println("duur: " + duur);

		Set<String> zetten = waardes.keySet();
		Iterator<String> keyIt = zetten.iterator();

		ArrayList<String> stringZetten = new ArrayList<String>();
		ArrayList<Double> percentageZetten = new ArrayList<Double>();
		boolean eersteKeer = true;
		while (keyIt.hasNext()) {
			String key = keyIt.next();

			double percentage = ((float) waardes.get(key) / (float) voorkomen
					.get(key)) * 100;
			if (eersteKeer) {
				percentageZetten.add(percentage);
				stringZetten.add(key);
				eersteKeer = false;
			} else {
				boolean geplaats = false;
				for (int i = 0; i < percentageZetten.size() && !geplaats; i++) {
					if (percentage >= percentageZetten.get(i)) {
						percentageZetten.add(i, percentage);
						stringZetten.add(i, key);
						geplaats = true;

					}
				}
				if (!geplaats) {
					percentageZetten.add(percentage);
					stringZetten.add(key);
				}
			}

			System.out.println("zet: " + key + " voorgekomen: "
					+ voorkomen.get(key) + " gewonnen: " + waardes.get(key)
					+ " percentage:  " + percentage);
		}
		for (int l = 0; l < percentageZetten.size(); l++) {
			System.out.println("zet: " + stringZetten.get(l) + "percentage: "
					+ percentageZetten.get(l));
		}
		System.out.println("Totaal voorgekomen :" + k);
		try {
			return stringZetten.get(0);
		} catch (IndexOutOfBoundsException e) {
			return "-1";
		}
	}

}