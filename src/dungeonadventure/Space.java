package dungeonadventure;
import java.io.Serializable;
import java.util.HashMap;

import dungeonadventure.items.Item;
import dungeonadventure.properties.Nameable;

/**
 * A space/location. Space has a map of possible next Spaces (places to move into).
 * Space can also contains items.
 * @author Mikko Piuhola
 */
public class Space implements Nameable,Serializable {

	private static final long serialVersionUID = 3657051461692034169L;
	
	private String name;
	private String description;
	private HashMap<String, Space> nextSpaces;
	private HashMap<String, Item> items; // TODO: Should we use locations that contain Items/Spaces instead?

	private boolean hasChargingStation;
	
	/**
	 * Create a Space with a name
	 * @param name Name for the Space
	 * @param description Description of the Space
	 */
	public Space(String name, String description) {
		this(name, description, new HashMap<String, Space>(), new HashMap<String, Item>());
	}
	
	/**
	 * Create a Space with a name and next spaces
	 * @param name Name for the Space
	 * @param description Description of the Space
	 * @param spaces Next possible spaces to move to from Space
	 */
	public Space(String name, String description, HashMap<String, Space> spaces) {
		this(name, description, spaces, new HashMap<String, Item>());
	}
	
	/**
	 * Create a Space with a name, next spaces and items
	 * @param name Name for the Space
	 * @param description Description of the Space
	 * @param spaces Next possible spaces to move to from Space
	 * @param items Items in Space
	 */
	public Space(String name, String description, HashMap<String, Space> spaces, HashMap<String, Item> items) {
		this.name = name;
		this.description = description;
		this.nextSpaces = spaces;
		this.items = items;
		this.hasChargingStation = false;
	}
	
	/**
	 * Set whether this Space has a charging station or not
	 * @param has Does Space have charging station
	 */
	public void setHasChargingStation(boolean has) {
		this.hasChargingStation = has;
	}
	
	/**
	 * Get space's name
	 * @return Space's name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Add a new Space as a next possible space for this space
	 * @param direction Direction where next space is in relation to this Space
	 * @param nextSpace Next space
	 */
	public void addNextSpace(String direction, Space nextSpace) {
		this.nextSpaces.put(direction.toLowerCase(), nextSpace);
		this.nextSpaces.put(nextSpace.getName().toLowerCase(), nextSpace);
	}
	
	/**
	 * Does this Space have a connection to given Space's name or direction
	 * @param dirOrName Direction or name of the wanted next Space
	 * @return Does connection to next Space exist
	 */
	public boolean hasNextSpace(String dirOrName) {
		return this.nextSpaces.containsKey(dirOrName.toLowerCase());
	}
	
	/**
	 * Get a space by given space's name
	 * @param dirOrName Direction or name of the wanted next Space
	 * @return Next Space object
	 */
	public Space getNextSpace(String dirOrName) {
		return this.nextSpaces.get(dirOrName.toLowerCase());
	}
	
	/**
	 * Add a new item into the Space
	 * @param cmd User input command
	 * @param item Item
	 */
	public void addItem(String cmd, Item item) {
		this.items.put(cmd.toLowerCase(), item);
	}
	
	/**
	 * Get Items in Space
	 * @return HashMap of Items in Space
	 */
	public HashMap<String,Item> getItems() {
		// Don't allow modifications of the member variable
		return (HashMap<String, Item>) this.items;
	}
	
	/**
	 * Take Item from Space
	 * @param name Name of Item to take
	 * @return Taken Item
	 */
	public Item takeItem(String name) {
		String lowName = name.toLowerCase();
		if (this.items.containsKey(lowName)) {
			Item tempItem = this.items.get(lowName);
			this.items.remove(lowName);
			return tempItem;
		}
		return null;
	}
	
	/**
	 * Check if Space contains specific Item
	 * @param item Item to check for
	 * @return Space contains item
	 */
	public boolean hasItem(Item item) {
		// Check for null targets
		if (item == null) {
			return false;
		}
		
		return this.items.containsKey(item.getName().toLowerCase());
	}
	
	/**
	 * Get the Space's description and if it contains a charging station
	 */
	@Override
	public String toString() {
		String desc = this.description;
		if (this.hasChargingStation) {
			desc += "\nAnd there's the charging station for the mine's flashlights. That could come in handy if you flaslight runs out of battery.";
		}
		return desc;
	}

	/**
	 * Method for checking if this is a Space where Chargeable objects can be charged
	 * @return Has a charging station
	 */
	public boolean hasCharging() {
		return this.hasChargingStation;
	}
}
