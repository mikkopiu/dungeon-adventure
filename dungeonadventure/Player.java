package dungeonadventure;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import dungeonadventure.items.Food;
import dungeonadventure.items.Item;
import dungeonadventure.items.Lightsource;
import dungeonadventure.observers.ExplodeObserver;
import dungeonadventure.observers.WaterPipeBreakObserver;
import dungeonadventure.properties.Eatable;
import dungeonadventure.properties.Hostable;
import dungeonadventure.properties.Inspectable;
import dungeonadventure.properties.Installable;
import dungeonadventure.properties.Nameable;


/**
 * Player has an inventory of Items and a location (Space).
 * He reacts to game ending situations like explosion and water pipe breaking.
 * @author Mikko Piuhola
 */
public class Player implements Nameable,Inspectable,Serializable,ExplodeObserver,WaterPipeBreakObserver {

	private static final long serialVersionUID = -1860691229021937577L;
	
	private String name;
	private String description;
	private Inventory inventory;
	private Space currentSpace;
	private String currentDirection;
	
	private boolean alive;
	private GameState gameState;

	/**
	 * Constructor with a starting Space.
	 * Player must always be constructed with a default Space
	 * @param gameState GameState-object
	 * @param space Starting Space
	 */
	public Player(GameState gameState, Space space) {
		this(gameState, new Inventory(), space);
	}
	
	/**
	 * Constructor with an pre-set Inventory and a starting Space
	 * @param gameState GameState-object
	 * @param inventory Player inventory
	 * @param space Current location
	 */
	public Player(GameState gameState, Inventory inventory, Space space) {
		this.name = "Mark";
		this.description = "a miner";
		this.inventory = inventory;
		this.currentSpace = space;
		this.currentDirection  = "north";
		this.alive = true;
		this.gameState = gameState;
	}
	
	// NAMEABLE
	
	/**
	 * Get Player's name
	 * @return Player's name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Get Player's description
	 * @return String description
	 */
	public String toString() {
		return this.description;
	}
	
	// INSPECTABLE
	
	/**
	 * Describe Player
	 */
	public void inspect() {
		System.out.println(this.toString());
	}
	
	/**
	 * Check if Player has Item in Inventory
	 * @param item Item to check for
	 * @return Does Player have Item currently
	 */
	public boolean hasItem(Item item) {
		if (item == null) {
			return false;
		}
		return this.inventory.getItem(item.getName()) != null;
	}
	
	/**
	 * Pickup item and add to player inventory
	 * @param item Item to pickup
	 */
	public void pickupItem(Item item) {
		if (item != null) {
			this.inventory.addItem(item);
		}
	}

	/**
	 * Completely drop item, without leaving in current Space.
	 * Might be that the Player uses some Item, or inserts it into something.
	 * @param item Item to drop
	 * @return Item that was dropped
	 */
	public Item drop(Item item) {
		if (this.inventory.removeItem(item.getName())) {
			return item;
		} else {
			return null;
		}
	}
	
	/**
	 * Drop item and leave it in current Space
	 * @param item Item to drop
	 */
	public void dropItem(Item item) {
		Item tempItem = this.inventory.getItem(item.getName());
		this.currentSpace.addItem(tempItem.getName(), tempItem); // Add to Space
		this.inventory.removeItem(tempItem.getName()); // Remove from Inventory
	}
	
	/**
	 * Get player inventory
	 * @return Player inventory
	 */
	public Inventory getInventory() {
		return this.inventory;
	}
	
	/**
	 * Print list of items in player's inventory
	 * Pass-through to Inventory.printInventory
	 */
	public void printInventoryList() {
		this.inventory.printInventory();
	}
	
	/**
	 * Insert one Item into another
	 * @param itemToPut Item to put into the other Item
	 * @param itemToPutInto Item that will hold the other Item
	 * @return Success or not
	 */
	public boolean putItemInItem(Installable itemToPut, Hostable itemToPutInto) {
		return itemToPutInto.install(itemToPut);
	}
	
	/**
	 * Get the current Space of the Player
	 * @return Current Space of the Player
	 */
	public Space getCurrentSpace() {
		return this.currentSpace;
	}
	
	/**
	 * Move player to direction or space (by name)
	 * @param a Directional Action
	 */
	public void move(Action a) {
		switch (a) {
			case ACTION_GO_NORTH:
				this.moveToSpace(this.currentSpace.getNextSpace("north"));
				break;
			case ACTION_GO_EAST:
				this.moveToSpace(this.currentSpace.getNextSpace("east"));
				break;
			case ACTION_GO_SOUTH:
				this.moveToSpace(this.currentSpace.getNextSpace("south"));
				break;
			case ACTION_GO_WEST:
				this.moveToSpace(this.currentSpace.getNextSpace("west"));
				break;
			case ACTION_GO_LOCATION:
				if (this.currentSpace.hasNextSpace(a.getLocation())) {
					this.moveToSpace(this.currentSpace.getNextSpace(a.getLocation()));
				} else {
					System.out.println("No such place is visible from here, you stay put");
				}
				break;
			default:
				System.out.println("There's nothing in that direction, you stay put");
				break;
		}
	}
	
	/**
	 * Move player to space
	 * @param space Space to move into
	 */
	private void moveToSpace(Space space) {
		if (space == null) {
			System.out.println("There's nothing in that direction");
		} else {
			this.currentSpace = space;
			this.look();
		}
	}
	
	/**
	 * Get the direction this Player is currently looking
	 * @return Direction name the Player is currently looking
	 */
	public String getCurrentDirection() {
		return this.currentDirection;
	}
	
	/**
	 * Turn Player to the left by 90 degrees
	 */
	public void turnLeft() {
		switch (this.currentDirection) {
			case "north":
				this.currentDirection = "west";
				System.out.println("You are now facing west");
				break;
			case "west":
				this.currentDirection = "south";
				System.out.println("You are now facing south");
				break;
			case "south":
				this.currentDirection = "east";
				System.out.println("You are now facing east");
				break;
			case "east":
				this.currentDirection = "north";
				System.out.println("You are now facing north");
				break;
		}
		
		this.lookNextSpaces();
	}
	
	/**
	 * Turn Player to the right by 90 degrees
	 */
	public void turnRight() {
		switch (this.currentDirection) {
			case "north":
				this.currentDirection = "east";
				System.out.println("You are now facing east");
				break;
			case "east":
				this.currentDirection = "south";
				System.out.println("You are now facing south");
				break;
			case "south":
				this.currentDirection = "west";
				System.out.println("You are now facing west");
				break;
			case "west":
				this.currentDirection = "north";
				System.out.println("You are now facing north");
				break;
		}
		
		this.lookNextSpaces();
	}
	
	/**
	 * Eat food item and remove from Inventory or the current Space
	 * @param foodItem Food to eat
	 */
	public void eat(Food foodItem) {
		if (foodItem instanceof Eatable) {
			foodItem.eat();
			if (this.hasItem((Item)foodItem)) {
				this.inventory.removeItem(foodItem.getName());
			} else if (this.currentSpace.hasItem((Item)foodItem)) {
				this.currentSpace.takeItem(foodItem.getName());
			}
		} else {
			System.out.println("That isn't eatable");
		}
	}
	
	/**
	 * Get the most powerful Lightsource in Inventory that is also powered on
	 * @return Most powerful powered Lightsource or null
	 */
	private Lightsource getMostPowerfulPoweredLight() {
		ArrayList<Item> lightsources = this.inventory.getItemsOfType("Lightsource");
		
		if (lightsources.size() >= 1) {
			Lightsource light = null;
			// Run through all the available lightsources
			for (Item item : lightsources) {
				if (light == null) {
					// Must be powered on, even if it's the first one
					if (((Lightsource)item).isPoweredOn()) {
						light = (Lightsource)item;
					}
				} else if (((Lightsource)item).getVisibilityRange() > light.getVisibilityRange()) {
					// In addition to providing more lighting than the previous powered lightsource
					//  this must also be powered on.
					if (((Lightsource)item).isPoweredOn()) {
						light = (Lightsource)item;
					}
				}
			}
			
			return light;
		} else {
			return null;
		}
	}

	/**
	 * Say what Player currently sees
	 */
	public void look() {
		
		// Get all lightsources from inventory
		ArrayList<Item> lightsources = this.inventory.getItemsOfType("Lightsource");
		
		if (lightsources.size() >= 1) {
			
			Lightsource maxLight = this.getMostPowerfulPoweredLight();
			
			// Light must be powered on
			if (maxLight != null) {
				// Check what you can see based on the given visibility range of the Lightsource
				this.lookNextSpaces();
				// Describe the items in the room
				this.lookLocationItems();
			} else {
				System.out.println("None of your lightsources are on. You cannot see anything.");
			}

		} else {
			System.out.println("You don't have any lightsources. You cannot see anything in the dark.");
		}
	}
	
	/**
	 * Say the items that the Player currently sees
	 */
	private void lookLocationItems() {
		HashMap<String,Item> roomItems = this.currentSpace.getItems();
		
		// List any possible items in current Space
		if (roomItems.size() < 1) {
			System.out.println("There are not items in here");
		} else {
			System.out.println("You see " + (roomItems.size() > 1 ? "some things " : "only one thing") + " in here: ");
			for (Entry<String, Item> entry : roomItems.entrySet()) {
				System.out.println(entry.getValue().getName());
			}
		}
	}
	
	/**
	 * List all the visible next spaces from the Player's current direction
	 * and based on their Lightsource.
	 */
	private void lookNextSpaces() {

		Lightsource light = this.getMostPowerfulPoweredLight();
		HashMap<String,Space> spaces = this.getVisibleSpaces();
		
		System.out.println("You look around...");
		
		if (light != null) {
			int deg = light.getVisibilityRange();
			
			if (deg < 90) {
				System.out.println("You have a poor lightsource. You cannot make out anything in the dark.");
			} else if (deg < 180) {
				System.out.println("Your " + light.getName() + " provides a narrow field of visibility...");
				System.out.println("You stand in " + this.currentSpace.toString());
			} else if (deg < 360) {
				System.out.println("Your " + light.getName() + " allows you to see right in front of you, and to the left and right of you...");
				System.out.println("You stand in " + this.currentSpace.toString());
			} else {
				System.out.println("Your " + light.getName() + " allows you to see everywhere around you...");
				System.out.println("You stand in " + this.currentSpace.toString());
			}
			
			this.listVisibleSpaces(spaces);
		} else {
			System.out.println("You don't see anything in the dark.");
		}
	}
	
	/**
	 * List visible spaces
	 * @param spaces HashMap of visible spaces
	 */
	private void listVisibleSpaces(HashMap<String,Space> spaces) {
		if (spaces.size() >= 1) {
			for (Entry<String, Space> entry : spaces.entrySet()) {
				Space space = entry.getValue();
				System.out.println("There's a route to the " + entry.getKey() + ", leading to " + space.getName());
			}
		} else {
			System.out.println("You cannot see any routes.");
		}
	}

	/**
	 * Get a ArrayList of currently visible next spaces
	 * @return List of currently visible next spaces
	 */
	public HashMap<String,Space> getVisibleSpaces() {
		HashMap<String,Space> visibleSpaces = new HashMap<String,Space>();
		Lightsource light = this.getMostPowerfulPoweredLight();
		
		if (light != null) {
			int deg = light.getVisibilityRange();
			
			if (deg >= 90 && deg < 180) {
				if (this.currentSpace.hasNextSpace(this.currentDirection)) {
					visibleSpaces.put(this.currentDirection, this.currentSpace.getNextSpace(this.currentDirection));
				}
			} else if (deg < 360) {
				switch (this.currentDirection) {
					case "north":
						if (this.currentSpace.hasNextSpace("east")) {
							visibleSpaces.put("east", this.currentSpace.getNextSpace("east"));
						}
						if (this.currentSpace.hasNextSpace("north")) {
							visibleSpaces.put("north", this.currentSpace.getNextSpace("north"));
						}
						if (this.currentSpace.hasNextSpace("west")) {
							visibleSpaces.put("west", this.currentSpace.getNextSpace("west"));
						}
						break;
					case "east":
						if (this.currentSpace.hasNextSpace("north")) {
							visibleSpaces.put("north", this.currentSpace.getNextSpace("north"));
						}
						if (this.currentSpace.hasNextSpace("east")) {
							visibleSpaces.put("east", this.currentSpace.getNextSpace("east"));
						}
						if (this.currentSpace.hasNextSpace("south")) {
							visibleSpaces.put("south", this.currentSpace.getNextSpace("south"));
						}
						break;
					case "south":
						if (this.currentSpace.hasNextSpace("east")) {
							visibleSpaces.put("east", this.currentSpace.getNextSpace("east"));
						}
						if (this.currentSpace.hasNextSpace("south")) {
							visibleSpaces.put("south", this.currentSpace.getNextSpace("south"));
						}
						if (this.currentSpace.hasNextSpace("west")) {
							visibleSpaces.put("west", this.currentSpace.getNextSpace("west"));
						}
						break;
					case "west":
						if (this.currentSpace.hasNextSpace("south")) {
							visibleSpaces.put("south", this.currentSpace.getNextSpace("south"));
						}
						if (this.currentSpace.hasNextSpace("west")) {
							visibleSpaces.put("west", this.currentSpace.getNextSpace("west"));
						}
						if (this.currentSpace.hasNextSpace("north")) {
							visibleSpaces.put("north", this.currentSpace.getNextSpace("north"));
						}
						break;
				}
			} else {
				// Run through the possible directions
				if (this.currentSpace.hasNextSpace("north")) {
					visibleSpaces.put("north", this.currentSpace.getNextSpace("north"));
				}
				if (this.currentSpace.hasNextSpace("east")) {
					visibleSpaces.put("east", this.currentSpace.getNextSpace("east"));
				}
				if (this.currentSpace.hasNextSpace("south")) {
					visibleSpaces.put("south", this.currentSpace.getNextSpace("south"));
				}
				if (this.currentSpace.hasNextSpace("west")) {
					visibleSpaces.put("west", this.currentSpace.getNextSpace("west"));
				}
			}
		}
		
		return visibleSpaces;
	}
	
	/**
	 * Check if Player is alive
	 * @return Is alive
	 */
	public boolean isAlive() {
		return this.alive;
	}
	
	// DEATH OBSERVERS

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void bombExplosion() {
		this.alive = false;
		this.gameState.setGameState(State.MENU_STATE);
		ThreadManager.clearAllThreads();
		System.out.println();
		System.out.println("Suddenly there's a huge explosion in the mines! It's the explosives you left armed!");
		System.out.println("The whole mine collapses on top of you!");
		System.out.println();
		System.out.println("You died alone in the mines. No one would find you body in years...");
		System.out.println("*************************************");
		System.out.println("**************GAME OVER**************");
		System.out.println("*************************************");
		System.out.println("A game by: Mikko Piuhola");
		System.out.println();
		System.out.println("Type anything to return to the main menu:");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void waterPipeBreak() {
		this.alive = false;
		this.gameState.setGameState(State.MENU_STATE);
		ThreadManager.clearAllThreads();
		System.out.println();
		System.out.println("You hear the water main bursting open!");
		System.out.println("You have to get out of here!");
		System.out.println("The water reaches you seconds later and fills the mine instantly.");
		System.out.println("You struggle for a few seconds but it's useless, there's no way out.");
		System.out.println("*************************************");
		System.out.println("**************GAME OVER**************");
		System.out.println("*************************************");
		System.out.println("A game by: Mikko Piuhola");
		System.out.println();
		System.out.println("Type anything to return to the main menu:");
	}
}
