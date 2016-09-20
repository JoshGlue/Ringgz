package Ringgz.protocol;

/**
 * <p>
 * Het protocol waarmee de client met de server (ClientHandler) communiceert.
 * Bij het implementeren moet in ieder geval een invulling gegeven worden aan de
 * standaardcommando's. Als de client hierdoor niet aan een verzoek van een
 * server kan voldoen, moet de betreffende foutmelding worden doorgegeven met
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
 * "doMove" + DELIM + 12 + DELIM + 2 + DELIM + 3 + DELIM + '+' + '\n'
 * </tt>
 * </p>
 * <p>
 * Vanzelfsprekend mag het scheidingsteken niet in een spelernaam worden
 * opgenomen.
 * </p>
 * 
 * @author Jolien Lankheet
 * @author Mart Oude Weernink
 * @version 20130328
 */
public interface ClientProtocol {

	/**
	 * Scheidingsteken voor keywords en argumenten.
	 */
	public static final String DELIM = "~";

	// Standaardcommando's
	/**
	 * Meldt zich aan bij de server en geeft zijn spelernaam door aan de server.
	 * Geeft ook het aantal gewenste tegenstanders mee, maximaal 3. Als 0 ingegeven
	 * wordt, wordt een random aantal tegenspelers gekozen.
	 * @param name         Spelernaam
	 * @param players      Gewenste aantal tegenspelers
	 * @require <tt>name != "" </tt>
	 * @require !name.contains(DELIM)
	 * @require <tt>0<=players<4 </tt>
	 * @syntax <code>join &lt;String:name&gt; &lt;Int:players&gt;</code>
	 */
	public static final String JOIN = "join";

	/**
	 * Verzoekt de server om een zet te doen. Een zet bestaat uit het plaatsen
	 * van een ring. De client krijgt zijn eigen zet terug van de server, mits
	 * deze is goedgekeurd.
	 * @param field
	 *            Nummer van het vakje waarin de ring geplaatst moet worden,
	 *            beginnend linksboven met 0 en oplopend langs de leesrichting
	 * @param color
	 *            Kleur die de ring moet hebben. Lopend van 0 t/m 3.
	 * @param format
	 *            'Grootte' van de ring. Loopt van 0 t/m 4. 0 is kleine dichte
	 *            ring, 1 t/m 3 de andere ringen oplopend in grootte. 4 is de
	 *            ring die een heel vak vult.
	 * @require <tt> 0<=field<25 </tt>
	 * @require <tt> 0<=color<4 </tt>	
	 * @require <tt> 0<=format<5 </tt>
	 * @require De startsteen ligt niet op de gekozen positie && er is nog geen
	 *          ring van dezelfde grootte geplaatst op de gekozen positie &&
	 *          steen 4 is niet op de gekozen positie geplaatst.
	 * 
	 * @syntax <code>doMove &lt;Int:field&gt; &lt;Int:color&gt; &lt;Int:format&gt;</code>
	 */
	public static final String DO_MOVE = "doMove";

	/**
	 * Geeft een fout door aan de server.
	 * @param code
	 *            Een foutcode die de fout beschrijft. Gebruik de methode
	 *            <tt>value()</tt> op een <tt>Error</tt>-instantie om de code te
	 *            verkrijgen.
	 * @param description
	 *            Een beschrijving van de fout. Voor een standaardbeschrijving
	 *            kan <tt>toString()</tt> van een <tt>Error</tt>-instantie
	 *            gebruikt worden. De client is vrij om te kiezen tussen de
	 *            standaardbeschrijving of de meegegeven beschrijving.
	 * @require <tt>(code == Error.DEFAULT.value())</tt>
	 * @require <tt>description != ""</tt>
	 * @require <tt>for 0 <= i < description.length():<br>
	 * &nbsp;&nbsp;name.charAt(i) != DELIM</tt>
	 * @syntax <code>error &lt;Int:code&gt; &lt;String:description&gt;</code>
	 */
	public static final String ERROR = "error";

	// Chatcommando
	/**
	 * Geeft een chatbericht door aan de server.
	 * @param message          Bericht
	 * @syntax <code>sendMessage &lt;String:message&gt;</code>
	 */
	public static final String SEND_MESSAGE = "sendMessage";

	// Challenge-commando's
	/**
	 * Verzoekt de server om een lijst van spelers die in het spel zitten.
	 * @require Deze client bevindt zich in de lobby.
	 * @syntax <code>getPlayers</code>
	 */
	public static final String GET_PLAYERS = "getPlayers";

	/**
	 * Verzoekt de server om ŽŽn of meerdere spelers uit te nodigen voor een
	 * potje.
	 * @param name
	 *            Namen van de gewenste tegenstanders
	 * @require <tt>name != ""</tt>
	 * @require De gegeven namen zijn bekend bij de server.
	 * @syntax <code>challenge &lt;String:name&gt;(1..3)</code>
	 */
	public static final String CHALLENGE = "challenge";

	/**
	 * Accepteert een uitnodiging. De partij kan ook door iemand anders
	 * geannuleerd worden.
	 * @param name
	 *            Naam van de uitnodiger
	 * @require <tt>naam</tt> heeft deze client uitgenodigd.
	 * @ensure Eventuele andere openstaande uitnodigingen worden afgewezen met
	 *         het <tt>challengeReject</tt>-commando.
	 * @syntax <code>challengeAccept &lt;String:name&gt;</code>
	 */
	public static final String CHALLENGE_ACCEPT = "challengeAccept";

	/**
	 * Wijst een uitnodiging af. De server zal de partij nu annuleren.
	 * @param name
	 *            Naam van de uitnodiger
	 * @require <tt>naam</tt> heeft deze client uitgenodigd.
	 * @syntax <code>challengeReject &lt;String:name&gt;</code>
	 */
	public static final String CHALLENGE_REJECT = "challengeReject";

}
