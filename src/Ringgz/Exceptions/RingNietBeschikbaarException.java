package Ringgz.Exceptions;

/**
 * Als een bepaald type en kleur niet meer gebruikt kan worden, gooit hij deze exceptie.
 *
 */
public class RingNietBeschikbaarException extends Exception {

	public RingNietBeschikbaarException(){
		super("De ring is niet meer beschikbaar");
	}
	


}
