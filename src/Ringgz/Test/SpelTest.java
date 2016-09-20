/**
 * 
 */
package Ringgz.Test;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import Ringgz.Spel.Spel;
import Ringgz.Spel.Speler;

/**
 * @author s1385801
 * 
 */
public class SpelTest {

	Spel tweeSpelerSpel;
	Spel drieSpelerSpel;
	Spel vierSpelerSpel;

	private final static String speler1 = "s1";
	private final static String speler2 = "s2";
	private final static String speler3 = "s3";
	private final static String speler4 = "s4";
	private final static int beginIndex = 6;
	private final String[] tweeSpelers = { speler1, speler2 };
	private final String[] drieSpelers = { speler1, speler2, speler3 };
	private final String[] vierSpelers = { speler1, speler2, speler3, speler4 };

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setup() {
		tweeSpelerSpel = new Spel(2, tweeSpelers, beginIndex);
		drieSpelerSpel = new Spel(3, drieSpelers, beginIndex);
		vierSpelerSpel = new Spel(4, vierSpelers, beginIndex);
		tweeSpelerSpel.setSpeler(speler2);
		drieSpelerSpel.setSpeler(speler2);
		vierSpelerSpel.setSpeler(speler2);
	}

	@Test
	public void testBeginsituatie() {

		Assert.assertTrue("Controleren of de beginsteen op de goede plek ligt",
				tweeSpelerSpel.getBord().getVak(beginIndex).isVol());

		for (int i = 0; i < 25; i++) {
			if (i != beginIndex)
				Assert.assertTrue(
						"controleren of de rest van de velden leeg zijn; veld "
								+ i, tweeSpelerSpel.getBord().getVak(i)
								.isLeeg());
		}

		for (Speler s : tweeSpelerSpel.getSpelers()) {
			Assert.assertEquals("testen of " + s.getNaam()
					+ " 30 ringen/blokken heeft", 30, s.getBeschikbareRingen()
					.size());
		}
		for (Speler s : drieSpelerSpel.getSpelers()) {
			Assert.assertEquals("testen of " + s.getNaam()
					+ " 20 ringen/blokken heeft", 20, s.getBeschikbareRingen()
					.size());
		}
		for (Speler s : vierSpelerSpel.getSpelers()) {
			Assert.assertEquals("testen of " + s.getNaam()
					+ " 15 ringen/blokken heeft", 15, s.getBeschikbareRingen()
					.size());
		}
		// controleren of het gelijkspel is, want er heeft niemand nog een zet
		// gedaan.
		Assert.assertNull("Controleren of het gelijkspel is",
				tweeSpelerSpel.isWinnaar());
		Assert.assertNull("Controleren of het gelijkspel is",
				drieSpelerSpel.isWinnaar());
		Assert.assertNull("Controleren of het gelijkspel is",
				vierSpelerSpel.isWinnaar());

	}

	@Test
	public void testSpelSituatie() {

		// /illegale zet, want speler1 is niet aan de beurt
		tweeSpelerSpel.doeZet(speler1, 0, 4, 1);
		Assert.assertEquals("Controleren of deze zet is gedaan", 0,
				tweeSpelerSpel.getBord().getVak(1).getAlleRingen().size());
		Assert.assertEquals(
				"Controleren of deze speler nog al zijn stenen heeft", 30,
				tweeSpelerSpel.getSpeler(speler1).getBeschikbareRingen().size());

		// Illegale zet, want speler2 heeft geen ringen van de kleur 0;
		tweeSpelerSpel.doeZet(speler2, 0, 4, 1);
		Assert.assertEquals("Controleren of deze zet is gedaan", 0,
				tweeSpelerSpel.getBord().getVak(1).getAlleRingen().size());
		Assert.assertEquals("Controleren of speler1 nog al zijn stenen heeft",
				30, tweeSpelerSpel.getSpeler(speler1).getBeschikbareRingen()
						.size());
		Assert.assertEquals("Controleren of speler2 nog al zijn stenen heeft",
				30, tweeSpelerSpel.getSpeler(speler2).getBeschikbareRingen()
						.size());

		// illegale zet, want hij is niet aangrenzend aan de beginsteen(6)
		tweeSpelerSpel.doeZet(speler2, 2, 4, 12);
		Assert.assertEquals("Controleren of deze zet is gedaan", 0,
				tweeSpelerSpel.getBord().getVak(12).getAlleRingen().size());

		// testen of een type op een bepaald vak kan staan waar dit type al
		// aanwezig is
		tweeSpelerSpel.doeZet(speler2, 2, 1, 5);
		tweeSpelerSpel.doeZet(speler1, 0, 1, 5);
		Assert.assertEquals(
				"Controleren of de zet van de eerste speler is gedaan", 1,
				tweeSpelerSpel.getBord().getVak(5).getAlleRingen().size());
		Assert.assertEquals("Controleren of speler1 nog wel aan de beurt is",
				speler1, tweeSpelerSpel.getHuidigeSpeler().getNaam());

		// testen of het vak vol is als er een blok wordt toegevoegd.
		tweeSpelerSpel.doeZet(speler1, 0, 4, 7);
		Assert.assertTrue("Controleren of het vak vol is/een steen op staat",
				tweeSpelerSpel.getBord().getVak(7).isVol());

		// testen of de XY-functies werken, omdat als er nu een een steen op de
		// index 4 wordt gezet(1 minder dan de index waar nu wel een steen van
		// speler2 staat), maar in werkelijkheid is index 4 rechtsboven, dus
		// hiermee test ook meteen of er een zet gedaan kan worden, als er geen
		// aangrenzende kleur is
		tweeSpelerSpel.doeZet(speler2, 2, 4, 4);
		Assert.assertEquals("Controleren of deze zet niet is gedaan", 0,
				tweeSpelerSpel.getBord().getVak(4).getAlleRingen().size());

		// tussentijdse controle of alle ringen van de spelers nog kloppen
		Assert.assertEquals("Controleren of speler2 nog genoeg stenen heeft",
				29, tweeSpelerSpel.getSpeler(speler2).getBeschikbareRingen()
						.size());
		Assert.assertEquals("Controleren of speler1 nog genoeg stenen heeft",
				29, tweeSpelerSpel.getSpeler(speler1).getBeschikbareRingen()
						.size());

		// controleren of deze zet wordt gedaan, omdat deze een aangrenzende
		// ring heeft.
		tweeSpelerSpel.doeZet(speler2, 2, 1, 1);
		Assert.assertEquals("Controleren of deze zet is gedaan", 1,
				tweeSpelerSpel.getBord().getVak(1).getAlleRingen().size());

		// /controleren of een blok naast een blok met dezelfde kleur kan worden
		// gezet.
		tweeSpelerSpel.doeZet(speler1, 0, 4, 8);
		Assert.assertEquals("Controleren of deze zet niet is gedaan", 0,
				tweeSpelerSpel.getBord().getVak(8).getAlleRingen().size());

		// Speler1 heeft nu 2 blokken op het veld en speler2 2 ringen. Dit zou
		// de stand dus moeten maken dat het 0-2 staat.
		Assert.assertEquals("speler1 zou 0 punten moeten hebben", 0,
				tweeSpelerSpel.spelerPunten(tweeSpelerSpel.getSpeler(speler1)));
		Assert.assertEquals("speler2 zou 2 punten moeten hebben", 0,
				tweeSpelerSpel.spelerPunten(tweeSpelerSpel.getSpeler(speler1)));
		Assert.assertEquals("speler2 zou nu de winnaar moeten zijn", speler2,
				tweeSpelerSpel.isWinnaar().getNaam());

		// testen of het nog geen game-over is
		Assert.assertFalse("Het zou niet game-over moeten zijn",
				tweeSpelerSpel.isGameOver());

	}

}
