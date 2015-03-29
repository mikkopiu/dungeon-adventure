package dungeonadventure.items;

import dungeonadventure.properties.Openable;


/**
 * Door, winOnOpen can be set to true to make this a game winning door.
 * Used as the exit hatch of the game, opening of which wins the game.
 * @author Mikko Piuhola
 */
public class Door extends Item implements Openable {

	private static final long serialVersionUID = 846589922934378737L;
	
	boolean winOnOpen;
	boolean open;

	/**
	 * Create a default door, winOnOpen is false
	 */
	public Door() {
		this(false);
	}
	
	/**
	 * Create a door with a possibility to set winOnOpen
	 * @param winOnOpen Grants game win upon open
	 */
	public Door(boolean winOnOpen) {
		this("Door", "a door to some other location", winOnOpen);
	}
	
	/**
	 * Create full door with a name, description and winOnOpen
	 * @param name Name of the Door
	 * @param description Description of the Door
	 * @param winOnOpen Grants win upon open
	 */
	public Door(String name, String description, boolean winOnOpen) {
		super(name, description);
		this.winOnOpen = winOnOpen;
		this.open = false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean open() {
		if (this.winOnOpen) {
			return true;
		}
		
		return this.open;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean close() {
		return !this.open;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean winOnOpen() {
		return this.winOnOpen;
	}
}
