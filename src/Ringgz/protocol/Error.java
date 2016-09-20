package Ringgz.protocol;

/**
 * <p>
 * Enumeratie van foutmeldingen die de server naar de client kan sturen via de
 * methode <tt>error(Error)</tt>. De foutcode kan verkregen worden met
 * <tt>value()</tt>. De code kan weer omgezet worden naar een <tt>Error</tt>
 * -object door het als index te gebruiken in de <tt>Error</tt>-array verkregen
 * met <tt>Error.values()</tt>. Bijvoorbeeld:
 * </p>
 * <p>
 * <tt>
 * Error err = Error.values()[code];<br>
 * if (err.equals(Error.NOT_YOUR_TURN)) {<br>
 * &nbsp;&nbsp;//doe iets met het Error-object<br>
 * }
 * </tt>
 * </p>
 * <p>
 * Een standaard beschrijving van de fout kan worden verkregen met
 * <tt>toString()</tt>.
 * </p>
 * <p>
 * Let op: enums worden automatisch aangemaakt en kunnen daarna behandeld worden
 * als constantes, zoals <tt>Error.DEFAULT</tt>. Het heeft geen zin om de
 * constructor aan te roepen.
 * </p>
 * 
 * @author Jolien Lankheet
 * @author Mart Oude Weernink
 * @version 20130328
 */
public enum Error {
	INVALID_MOVE(1, "Ongeldige zet"),
	INVALID_NAME(2, "Ongeldige naam"),
	INVALID_PLAYERS(3, "Ongeldig aantal spelers"),
	PLAYER_LEFT(4, "Speler is weggegaan"),
	UNKNOWN_COMMAND(5, "Onbekend commando"),
	GAME_OVER(6, "Je kan geen zet meer doen"),
	DEFAULT(7,"Error"),
	
	// Extra
	CHALLENGE_NOT_SUPPORTED(8, "Challenge-functie wordt niet ondersteund"),
	CHAT_NOT_SUPPORTED(9, "Chat-functie wordt niet ondersteund");

	/**
	 * Beschrijving van de foutmelding
	 */
	private final String description;
	private final int code;

	/**
	 * Interne constructor voor het initialiseren van de enum-objecten.
	 * @param description: Beschrijving van de foutmelding
	 * @param code: foutcode van foutmelding
	 */
	private Error(int code, String description) {
		this.description = description;
		this.code=code;
	}

	/**
	 * Geeft de error code van de foutmelding.
	 * @return Een integer-waarde die uniek is voor elk type <tt>Error</tt>
	 */
	public int value() {
		return code;
	}

	/**
	 * Geeft een beschrijving van de foutmelding
	 * @return De beschrijving van de foutmelding
	 */
	public String toString() {
		return description;
	}
}
