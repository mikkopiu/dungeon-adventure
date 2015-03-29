package dungeonadventure.properties;

/**
 * Interface for Items that can be opened
 * @author Mikko Piuhola
 */
public interface Openable {
	
	/**
	 * Open object
	 * @return Success
	 */
	public boolean open();
	
	/**
	 * Close object
	 * @return Success
	 */
	public boolean close();
	
	/**
	 * Check if the object grants game victory on open.
	 * Used with the exit hatch of the game.
	 * @return Does the object grant victory on open
	 */
	public boolean winOnOpen();
}
