package Ringgz.Spel;

/**
 * interface waar meerdere strategieën op geimplementeerd kunnen worden
 *
 */
public interface Strategie {
	
	/**Bepaalt de beste zet.
	 * @param naam
	 * @param spel
	 * @return een String met de format kleur, type, index
	 */
	public String doeZet(String naam, Spel spel, int bedenkTijd);

}
