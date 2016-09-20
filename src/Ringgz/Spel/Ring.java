package Ringgz.Spel;

/**
 * Simpele klasse met basisfuncties(kleur opvragen, object initieren en vragen of de ring een blok is)
 *
 */
public class Ring {

	private int kleur;
	private int type;
	private String naam;

	/**
	 * Construeert een nieuw Ring-Object
	 * 
	 * @require kleur >= PC.ROOD && kleur <= PC.PAARS && type >=PC.KLEINSTE_RING && type <= PC.BLOK
	 * @param type
	 *            Waarde 0 is de kleinste ring, oplopend tot de grootste ring,
	 *            die waarde 3 heeft. Waarde 4 is een blok.
	 * @param kleur
	 *            Bepaalt de 4 beschikbare kleuren.
	 */
	public Ring(int kleur, int type, String naam) {
		this.naam = naam;
		this.kleur = kleur;
		this.type = type;
	}

	/**
	 * @return Geeft de kleur van het Ring-Object terug.
	 */
	public int getKleur() {
		return kleur;
	}

	/**
	 * @return Geeft het type van het Ring-Object terug.
	 */
	public int getType() {
		return type;
	}
	public String getNaam(){
		return naam;
	}

	/**
	 * @return Geeft een boolean terug of de ring een blok is. Dit is
	 *         belangrijk, omdat een blok niet meegerekend wordt in de
	 *         puntentelling.
	 */
	public boolean isBlok() {
		return type == PC.BLOK;
	}
	
	public String toString(){
		return "kleur: " + getKleur() + " type: " + getType();
	}

}
