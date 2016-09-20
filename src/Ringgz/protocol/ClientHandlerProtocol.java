package Ringgz.protocol;

/**
 * <p>
 * Het protocol waarmee de server (client handler) met de clients communiceert.
 * Bij het implementeren moet in ieder geval een invulling gegeven worden aan de
 * standaardcommando's. Als de handler hierdoor niet aan een verzoek van een
 * client kan voldoen, moet de betreffende foutmelding worden doorgegeven met
 * het <tt>error</tt>-commando.
 * </p>
 * <p>
 * Elk commando wordt over de socket gestuurd als een string met daarin het
 * keyword, gevolgd door de argumenten en beëindigd met een newline character.
 * De javadocs bij de commando's leggen de syntax uit bij de <tt>syntax</tt>
 * -tag, en ook eventuele pre- en postcondities. Het keyword en de argumenten
 * worden van elkaar gescheiden door een gereserveerd karakter, opgeslagen in
 * <tt>DELIM</tt>. Bijvoorbeeld:
 * </p>
 * <p>
 * <tt>
 * "startGame" + DELIM + "Pietje" + DELIM + "Henk" + "3"'\n'
 * </tt>
 * </p>
 * 
 * @author Jolien Lankheet
 * @author Mart Oude Weernink
 * @version 20130328
 */
public interface ClientHandlerProtocol {

	/**
	 * Scheidingsteken voor keywords en argumenten
	 */
	public static final String DELIM = ClientProtocol.DELIM;

	// Standaardcommando's
	/**
	 * Geeft aan de client door dat hij nu in de game zit.
	 * @require Een client heeft het <tt>join</tt>-commando gebruikt.
	 * @syntax <code>accept</code>
	 */
	public static final String ACCEPT = "accept";

	/**
	 * Geeft aan de client de spelernamen door van de partij waar hij in zit en
	 * waar de startsteen ligt.
	 * @param name            Spelernaam
	 * @param startingBase    positie Startsteen
	 * @require Alle spelers waarvan de namen worden doorgegeven bevinden zich
	 *          in de pre-game lobby en zijn dus niet met een partij bezig.
	 * @require <tt> 6<=startingBase<9 || 11<=startingBase<14 || 16<=startingBase<19 </tt>
	 * @ensure Het <tt>nextMove</tt>-commando wordt hierna doorgegeven.
	 * @ensure Kleurverdeling 
	 * 		   bij 2 spelers: Spelernaam 0: color 0 en 1
	 * 				  		  Spelernaam 1: color 2 en 3
	 * 		   bij 3 spelers: Spelernaam 0: color 0 en 3
	 * 				  		  Spelernaam 1: color 1 en 3
	 * 				  		  Spelernaam 2: color 2 en 3
	 * 		   bij 4 spelers: Spelernaam i: color i
	 * @syntax <code>startGame &lt;Int:startingBase&gt; &lt;String:name&gt;(2..4)</code>
	 */
	public static final String START_GAME = "startGame";

	/**
	 * Geeft aan de client door dat de volgende speler aan de beurt is waarbij
	 * de naam van die speler wordt vermeld.
	 * Wanneer het spel wordt gestart wordt er random een (start)speler gekozen.
	 * @param name     Naam van de speler die aan de beurt is
	 * @require Er zijn geen spelers die de verbindig hebben verbroken.
	 * @require Speler die aan de beurt is kan nog een zet doen.
	 * @require Het <tt>setMove</tt>-commando is verzonden || Het <tt>startGame</tt>-commando is verzonden.
	 * @syntax <code>nextMove &lt;String:name&gt; </code>
	 */
	public static final String NEXT_MOVE = "nextMove";
	
	/**
	 * Geeft aan de client door dat er een zet is gedaan en specificeert deze.
	 * Als de server een zet heeft goedgekeurd wordt dit commando verstuurd naar
	 * alle clients van de betreffende partij, inclusief de client die de zet
	 * deed.
	 * @param field
	 *            Nummer van het vakje waar de ring geplaatst is, beginnend
	 *            linksboven met 0 en oplopend langs de leesrichting
	 * @param color
	 *            Kleur van de ring die geplaatst is. Lopend van 0 t/m 3.
	 * @param format
	 *            Formaat van de ring die geplaatst is. Loopt van 0 t/m 4. 0 is
	 *            kleine dichte ring, 1 t/m 3 de andere ringen oplopend in
	 *            grootte. 4 is de ring die een heel vak vult.
	 * @require <tt> 0<=field<25 </tt>
	 * @require <tt> 0<=color<4 </tt>
	 * @require <tt> 0<=format<5 </tt>
	 * @require Er is nog geen ring van hetzelfde formaat geplaatst op de
	 *          gewenste positie && er is nog geen ring 4 op de gewenste positie
	 *          geplaatst && de startsteen ligt niet op de gewenste positie &&
	 *          ring 4 van dezelfde kleur ligt niet direct (boven/link/rechts) naast de gewenste
	 *          positie
	 * @syntax <code>setMove &lt;Int:field&gt; &lt;Int:color&gt; &lt;Int:format&gt;</code>
	 */
	public static final String SET_MOVE = "setMove";

	/**
	 * Geeft aan de client door dat het spel over is en wie de winnaars zijn
	 * door de spelers en het bijbehorende aantal punten op volgorde te zetten.
	 * @param resultList          Lijst met resultaat van spelletje
	 * @syntax <code>gameOver (&lt;String:name&gt;&lt;Int:points&gt;)(0..4)</code>
	 */
	public static final String GAME_OVER = "gameOver";

	/**
	 * Geeft een fout door aan de client.
	 * @param code
	 *            Een foutcode die de fout beschrijft. Gebruik de methode
	 *            <tt>value()</tt> op een <tt>Error</tt>-instantie om de code te
	 *            verkrijgen.
	 * @param description
	 *            Een beschrijving van de fout. Voor een standaardbeschrijving
	 *            kan <tt>toString()</tt> van een <tt>Error</tt>-instantie
	 *            gebruikt worden. De server is vrij om te kiezen tussen de
	 *            standaardbeschrijving of de meegegeven beschrijving.
	 * @require <tt>0 <= code < Error.values().length</tt>
	 * @require <tt>description != ""</tt>
	 * @require <tt>for 0 <= i < description.length():<br>
	 * &nbsp;&nbsp;name.charAt(i) != DELIM</tt>
	 * @syntax <code>error &lt;Int:code&gt; &lt;String:description&gt;</code>
	 */
	public static final String ERROR = "error";

	// Chatcommando
	/**
	 * Geeft een chatbericht door aan de client, met de naam van de afzender.
	 * @param name         Naam van de afzender
	 * @param message      Bericht
	 * @syntax <code>chat &lt;String:name&gt; &lt;String:message&gt;</code>
	 */
	public static final String BROADCAST = "broadcast";

	// Challenge-commando's
	/**
	 * Geeft een lijst van namen van alle spelers.
	 * @param name         Spelernaam
	 * @ensure Wanneer een speler geen lobby heeft wordt dit afgevangen met het
	 *         het <tt>error</tt>-commando.
	 * @syntax <code>players &lt;String:name&gt;*</code>
	 */
	public static final String GETPLAYERS = "getPlayers";

	/**
	 * Geeft aan de client door dat hij uitgenodigd is door de gegeven speler.
	 * @param name         Naam van de uitnodiger
	 * @syntax <code>challengedBy &lt;String:name&gt;</code>
	 */
	public static final String CHALLENGED_BY = "challengedBy";

	/**
	 * Geeft aan de client door dat zijn uitnodiging door de gegeven speler of
	 * spelers is afgewezen en dat de partij niet doorgaat.
	 * @param name         Naam van de genodigde
	 * @syntax <code>challengeRejected &lt;String:name&gt;</code>
	 */
	public static final String CHALLENGE_REJECTED = "challengeRejected";
}
