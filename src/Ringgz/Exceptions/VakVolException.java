package Ringgz.Exceptions;

/**
 * Als een vak vol zit bij het spel Ringzz, dan wordt deze exceptie gegooid
 *
 */
public class VakVolException extends Exception {

	public VakVolException(){
		super("Dit vak zit vol, probeer een ander vak");
	}
	


}
