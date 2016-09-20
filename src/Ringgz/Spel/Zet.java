package Ringgz.Spel;

/**
 * Hulpklasse die bijhoudt welke zetten nog gedaan kunnen worden
 * 
 */
public class Zet {
	private int veld;
	private Ring ring;

	/**
	 * Simpele constructor die de instantievariabele instelt
	 * 
	 * @param veld
	 * @param ring
	 */
	public Zet(int veld, Ring ring) {
		this.veld = veld;
		this.ring = ring;
	}

	/** simpele query die het veld terug geeft
	 * @return veld
	 */
	public int getVeld() {
		return veld;
	}

	/** simpele query die het ring-object teruggeeft
	 * @return ring
	 */
	public Ring getRing() {
		return ring;
	}
}
