package dungeonadventure;

import dungeonadventure.items.Item;


/**
 * Known actions/commands
 * @author Mikko Piuhola
 */
public enum Action {

	// No object, i.e. only this command
	ACTION_LOOK(new String[]{"look", "see", "watch"}, Type.TYPE_HASNOOBJECT),
	ACTION_VIEW_ITEMS(new String[]{"inventory", "items", "i"}, Type.TYPE_HASNOOBJECT),
	ACTION_HELP(new String[]{"help", "h"}, Type.TYPE_HASNOOBJECT),
	ACTION_SAVE(new String[]{"save", "savegame"}, Type.TYPE_HASNOOBJECT),

	// Directional
	ACTION_GO_EAST(new String[]{"east", "e"}, Type.TYPE_DIRECTIONAL),
	ACTION_GO_WEST(new String[]{"west", "w"}, Type.TYPE_DIRECTIONAL),
	ACTION_GO_SOUTH(new String[]{"south", "s"}, Type.TYPE_DIRECTIONAL),
	ACTION_GO_NORTH(new String[]{"north", "n"}, Type.TYPE_DIRECTIONAL),
	ACTION_GO_LOCATION(new String[]{"goto"}, Type.TYPE_DIRECTIONAL),
	
	// Turning
	ACTION_LEFT(new String[]{"left", "l"}, Type.TYPE_TURNING),
	ACTION_RIGHT(new String[]{"right", "r"}, Type.TYPE_TURNING),

	// Direct Object. Has one direct object e.g. eat Pizza, drop CD
	ACTION_PICKUP(new String[]{"pickup", "get", "take","acquire", "grab"}, Type.TYPE_HASDIRECTOBJECT),
	ACTION_INSPECT(new String[]{"inspect", "examine", "read", "view"}, Type.TYPE_HASDIRECTOBJECT),
	ACTION_DROP(new String[]{"drop", "leave"}, Type.TYPE_HASDIRECTOBJECT),
	ACTION_EAT(new String[]{"eat","chew", "consume", "bite", "swallow", "drink"}, Type.TYPE_HASDIRECTOBJECT),
	ACTION_OPEN(new String[]{"open", "unlock"}, Type.TYPE_HASDIRECTOBJECT),
	ACTION_CLOSE(new String[]{"close", "lock"}, Type.TYPE_HASDIRECTOBJECT),
	ACTION_POWER_ON(new String[]{"power", "on", "poweron", "turnon", "boot", "light"}, Type.TYPE_HASDIRECTOBJECT),
	ACTION_POWER_OFF(new String[]{"off", "poweroff", "turnoff", "shutdown", "extinquish", "kill", "suppress", "defuse"}, Type.TYPE_HASDIRECTOBJECT),
	ACTION_CHARGE(new String[]{"charge","chargeup","load"}, Type.TYPE_HASDIRECTOBJECT),
	ACTION_PLAY(new String[]{"play","start"}, Type.TYPE_HASDIRECTOBJECT),
	ACTION_STOP(new String[]{"stop","pause"}, Type.TYPE_HASDIRECTOBJECT),
	ACTION_SKIP(new String[]{"skip","forward","forwards","next","fastforward","fast-forward"}, Type.TYPE_HASDIRECTOBJECT),
	ACTION_PREV(new String[]{"prev","previous","back", "backward", "backwards"}, Type.TYPE_HASDIRECTOBJECT),

	// Indirect Object. Has one direct object and one indirect object, e.g. Put CD in CD Player
	ACTION_PUT(new String[]{"put", "install", "insert"}, Type.TYPE_HASINDIRECTOBJECT),
	ACTION_TAKE(new String[]{"remove"}, Type.TYPE_HASINDIRECTOBJECT),

	// Misc
	ACTION_UNKNOWN(new String[]{}, Type.TYPE_UNKNOWN),
	ACTION_ERROR(new String[]{}, Type.TYPE_UNKNOWN),
	ACTION_PASS(new String[]{"pass", "\n"}, Type.TYPE_UNKNOWN),
	ACTION_QUIT(new String[]{"quit", "exit"}, Type.TYPE_UNKNOWN);

	private Action opposite;
	private String[] aliases;
	private Type type;
	private Item directObject;
	private Item indirectObject;
	private String targetLocationName;
	
	/**
	 * Constructor for Action
	 * @param aliases Aliases for this action
	 * @param type Type of Action
	 */
	Action(String[] aliases, Type type) {
		this.aliases = aliases;
		this.type = type;
	}
	
	// Set opposite directions
	static {
		ACTION_GO_EAST.opposite = ACTION_GO_WEST;
		ACTION_GO_WEST.opposite = ACTION_GO_EAST;
		ACTION_GO_NORTH.opposite = ACTION_GO_SOUTH;
		ACTION_GO_SOUTH.opposite = ACTION_GO_NORTH;
		ACTION_LEFT.opposite = ACTION_RIGHT;
		ACTION_RIGHT.opposite = ACTION_LEFT;
	}

	/**
	 * Get all aliases for Action
	 * @return Array of aliases
	 */
	public String[] getAliases() {
		return this.aliases;
	}

	/**
	 * Get Action's type
	 * @return Type
	 */
	public Type getType() {
		return this.type;
	}

	/**
	 * Set the object this Action is directed towards
	 * @param directObject Object Action is directed towards
	 */
	public void setDirectObject(Item directObject) {
		this.directObject = directObject;
	}
	
	/**
	 * Get the object this Action is directed towards
	 * @return Thing
	 */
	public Item getDirectObject() {
		return this.directObject;
	}
	
	/**
	 * Set indirect object for Action
	 * @param indirectObject Object Action is indirectly directed towards, e.g. CDPlayer in "insert cd in cdplayer"
	 */
	public void setIndirectObject(Item indirectObject) {
		this.indirectObject = indirectObject;
	}
	
	/**
	 * Get indirect object for Action
	 * @return Thing
	 */
	public Item getInDirectObject() {
		return this.indirectObject;
	}

	/**
	 * Get the opposite direction for given direction.
	 * Opposite directions are used for the directional enumeration constants.
	 * @return Action in opposite direction or null if not directional
	 */
	public Action getOppositeDirection() {

		if(this.getType() == Type.TYPE_DIRECTIONAL) {
			return this.opposite;
		}
		else {
			return null;
		}
	}

	/**
	 * Set a target location
	 * @param locationName String for location name
	 */
	public void setLocation(String locationName) {
		this.targetLocationName = locationName;
	}
	
	/**
	 * Get location for ActionGoLocation
	 * @return Targeted location's name
	 */
	public String getLocation() {
		return this.targetLocationName;
	}

};

enum Type {
	TYPE_DIRECTIONAL,
	TYPE_TURNING,
	TYPE_HASDIRECTOBJECT,
	TYPE_HASINDIRECTOBJECT, // TODO DISABLED FOR NOW
	TYPE_HASNOOBJECT,
	TYPE_UNKNOWN;
}