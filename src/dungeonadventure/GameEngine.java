package dungeonadventure;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map.Entry;

import dungeonadventure.items.Bomb;
import dungeonadventure.items.CD;
import dungeonadventure.items.CDPlayer;
import dungeonadventure.items.Door;
import dungeonadventure.items.Flashlight;
import dungeonadventure.items.Food;
import dungeonadventure.items.Item;
import dungeonadventure.items.Lantern;
import dungeonadventure.items.WaterPipe;
import dungeonadventure.items.resources.Track;
import dungeonadventure.properties.Chargeable;
import dungeonadventure.properties.Eatable;
import dungeonadventure.properties.Holdable;
import dungeonadventure.properties.Hostable;
import dungeonadventure.properties.Inspectable;
import dungeonadventure.properties.Installable;
import dungeonadventure.properties.Openable;
import dungeonadventure.properties.Playable;
import dungeonadventure.properties.Powerable;

/**
 * Game engine. Contains game logic and manages game state.
 * @author Mikko Piuhola
 */
public class GameEngine implements Serializable {

	private static final long serialVersionUID = -6751690169252762972L;
	
	private Player currentPlayer;
	private GameState gameState;
	
	/**
	 * Constructor
	 */
	public GameEngine() {
		this.currentPlayer = null; // We initialize the Player on init
		this.gameState = null;
	}
	
	/**
	 * Game initialization
	 * Initializes commands and locations
	 * @param gameState GameState-object
	 */
	public void init(GameState gameState) {
		
		this.gameState = gameState;
		
		// START STORY
		try {
			System.out.println("You slowly regain your consciousness. It's pitch black.");
			System.out.println("Suddenly you remember: there was an explosion.");
			System.out.println("The tunnel was being dug further south.");
			System.out.println("And out of nowhere, there had been a series of explosions and parts of the mine had collapsed.");
			System.out.println("\nAs you come up to sit, you hear a dripping sound in the west. Hope that isn't the water main.");
			System.out.println("But suddenly you clear up as you hear a ticking sound in the south, down in the tunnels.");
			System.out.println("You had been laying down explosives in the tunnel and had just come up to get some food before the explosion.");
			System.out.println("Did some of the explosives not explode?");
			System.out.println("\nIn a state of panic, you fumble around and feel something next to you. Something metallic.");
			System.out.println("Could it be your flashlight?");
			System.out.println();
			System.out.println("*** Psst: saying help could prove to be useful ***");
		} catch (Exception e) {
			// Do nothing
		}
		
		// SPACES
		Space mineShaft,tunnel,supplies,controlRoom,diningArea,lockers,exit;
		mineShaft = new Space("Mineshaft","It's the mineshaft's starting point, where you woke up after the initial explosion.\nThere was a route outside but it got blocked in the explosion.");
		tunnel = new Space("Tunnel","This is the main tunnel for mining, where you were setting up explosives to dig further south.\nRight now the walls look like they are barely holding, you shouldn't stay here long.");
		supplies = new Space("Supplies","The supplies room. There seems to be a flashlight charging station in the corner. You should charge your flashlight if it's running low.");
		controlRoom = new Space("Controlroom","Here is where all the electricity and water supplies are connected to the outer world. The pipe on the wall is the water main.");
		diningArea = new Space("Dining-area","This place used to be your dining-area but right now it's just a bunch of rubble and random junk.\nThere's no one in here either. Were you and Peter the only ones in the mine during the explosion?\nYour memory is still a bit fuzzy.");
		lockers = new Space("Lockers","Not much is left of the lockers either. On a positive note: no bodies in here either.");
		exit = new Space("Exit", "The sign said exit, but this place doesn't look familiar.\nYou've never been here before. But, there is a promising looking hatch.");
		
		// NEXT/ADJACENT SPACES
		mineShaft.addNextSpace("east", diningArea);
		mineShaft.addNextSpace("south", tunnel);
		mineShaft.addNextSpace("west", supplies);
		tunnel.addNextSpace("north", mineShaft);
		tunnel.addNextSpace("west", supplies);
		supplies.addNextSpace("north", controlRoom);
		supplies.addNextSpace("east", mineShaft);
		supplies.addNextSpace("south", tunnel);
		controlRoom.addNextSpace("south", supplies);
		diningArea.addNextSpace("north", lockers);
		diningArea.addNextSpace("west", mineShaft);
		lockers.addNextSpace("north", exit);
		lockers.addNextSpace("south", diningArea);
		exit.addNextSpace("south", lockers);
		
		// ITEMS
		// CD Setup (just for fun, used as a mechanic for the Player to waste time)
		CD cd = new CD("St. Anger", "Metallica");
		cd.addTrack(new Track("Metallica","Frantic",5,50));
		cd.addTrack(new Track("Metallica","St. Anger",7,21));
		cd.addTrack(new Track("Metallica","Some Kind of Monster",8,26));
		cd.addTrack(new Track("Metallica","Dirty Window",5,25));
		cd.addTrack(new Track("Metallica","Invisible Kid",8,30));
		cd.addTrack(new Track("Metallica","My World",5,46));
		cd.addTrack(new Track("Metallica","Shoot Me Again",7,10));
		cd.addTrack(new Track("Metallica","Sweet Amber",5,27));
		cd.addTrack(new Track("Metallica","The Unnamed Feeling",7,8));
		cd.addTrack(new Track("Metallica","Purify",5,14));
		cd.addTrack(new Track("Metallica","All Within My Hands",8,48));
		
		// Room items
		mineShaft.addItem("Flashlight", new Flashlight());
		tunnel.addItem("Lantern", new Lantern());
		diningArea.addItem("Pizza", new Food("Pizza", "A delicious pepperoni pizza"));
		diningArea.addItem("CD", cd);
		lockers.addItem("CD-Player", new CDPlayer());
		exit.addItem("Hatch", new Door("Hatch","Could this be the emergency exit hatch? It doesn't look like it's locked -- maybe try opening it.", true));
		
		// CHARGING STATION (for Flashlight)
		supplies.setHasChargingStation(true);
		
		// OBSTACLES
		Bomb bomb = new Bomb();
		tunnel.addItem("Bomb", bomb);
		WaterPipe pipe = new WaterPipe();
		controlRoom.addItem("Pipe", pipe);
		
		// PLAYER
		this.currentPlayer = new Player(this.gameState, mineShaft); // Player must have a starting Space
		bomb.addObserver(this.currentPlayer);
		pipe.addObserver(this.currentPlayer);
	}
	
	/**
	 * Execute a user command
	 * @param a User input command
	 */
	@SuppressWarnings("incomplete-switch")
	public void executeCommand(Action a) {
		switch (a.getType()) {
			case TYPE_DIRECTIONAL:
				movePlayer(a);
				break;
			case TYPE_TURNING:
				switch (a) {
					case ACTION_LEFT: {
						System.out.println("You turned left");
						this.currentPlayer.turnLeft();
						break;
					}
					case ACTION_RIGHT: {
						System.out.println("You turned right");
						this.currentPlayer.turnRight();
						break;
					}
				}
				break;
			case TYPE_HASDIRECTOBJECT:
				
				// Action is directed towards an object (Item)
				// Find out the specific action
				switch (a) {
					case ACTION_PICKUP: {
						this.pickupItem(a);
						break;
					}
					case ACTION_INSPECT: {
						this.inspectItem(a);
						break;
					}
					case ACTION_DROP: {
						this.dropItem(a);
						break;
					}
					case ACTION_EAT: {
						this.eatItem(a);
						break;
					}
					case ACTION_OPEN: {
						this.openItem(a);
						break;
					}
					case ACTION_CLOSE: {
						this.closeItem(a);
						break;
					}
					case ACTION_POWER_ON: {
						this.powerOnItem(a);
						break;
					}
					case ACTION_POWER_OFF: {
						this.powerOffItem(a);
						break;
					}
					case ACTION_CHARGE: {
						this.chargeItem(a);
						break;
					}
					case ACTION_PLAY:
					case ACTION_STOP:
					case ACTION_SKIP:
					case ACTION_PREV:
						this.playbackItem(a);
						break;
				}
				
				break;
			case TYPE_HASINDIRECTOBJECT:
				switch (a) {
					case ACTION_PUT: {
						this.putItem(a);
						break;
					}
					case ACTION_TAKE: {
						this.takeItem(a);
						break;
					}
				}
				break;
			case TYPE_HASNOOBJECT:
				switch (a) {
					case ACTION_LOOK:
						// Describe what the Player sees
						this.currentPlayer.look();
						break;
					case ACTION_VIEW_ITEMS:
						
						// Only list the items if there actually are any
						if (this.currentPlayer.getInventory().getItemList().size() >= 1) {
							this.currentPlayer.printInventoryList();
						} else {
							System.out.println("You don't have any items in your inventory.");
						}

						break;
					case ACTION_HELP:
						this.help(); // Print out the help-menu
						break;
				}
				
				break;
			case TYPE_UNKNOWN:
				switch (a) {
					case ACTION_QUIT:
						// Don't print anything
						break;
					case ACTION_PASS:
						// Don't do anything
						break;
					case ACTION_ERROR:
						System.out.println("I don't understand what you're trying to do.\nIf you're trying to do something with an item, make sure you have it near or with you.");
						break;
					case ACTION_UNKNOWN:
						System.out.println("I don't understand what you're trying to do");
						break;
				}
				break;
			default:
				System.out.println("I don't understand what you're trying to do");
				break;
		}
	}
	
	/**
	 * Get Player's and current Space's inventories
	 * @return Combined inventory
	 */
	public HashMap<String, Item> getAvailableItems() {
		HashMap<String, Item> items = new HashMap<String, Item>();
		
		HashMap<String, Item> playerItems = this.currentPlayer.getInventory().getItemList();
		for(String key : playerItems.keySet()) {
			items.put(key, playerItems.get(key));
		}
		
		HashMap<String, Item> spaceItems = this.currentPlayer.getCurrentSpace().getItems();
		for(String key : spaceItems.keySet()) {
			items.put(key, spaceItems.get(key));
		}
		
		return items;
	}
	
	/**
	 * Check Player's health
	 * @return Is Player alive
	 */
	public boolean isPlayerAlive() {
		return this.currentPlayer.isAlive();
	}
	
	// PRIVATE
	
	/**
	 * Item charge action
	 * @param a ACTION_CHARGE
	 */
	private void chargeItem(Action a) {
		Item item = a.getDirectObject();
		
		if (this.currentPlayer.hasItem(item)) {
			if (item instanceof Chargeable) {
				if (this.currentPlayer.getCurrentSpace().hasCharging()) {
					if (((Chargeable)item).isInfinitelyChargeable()) {
						((Chargeable)item).charge();
					} else if (!((Chargeable)item).wasCharged()) {
						((Chargeable)item).charge();
					} else {
						System.out.println("You already charged the " + item.getName() + " once. It cannot be recharged again.");
					}
				} else {
					System.out.println("The charging station is not in here");
				}
			} else {
				System.out.println("You cannot charge that");
			}
		} else {
			System.out.println("You don't have that item in your inventory");
		}
	}

	/**
	 * Item power off action
	 * @param a ACTION_POWER_OFF
	 */
	private void powerOffItem(Action a) {
		Item item = a.getDirectObject();
		
		if (this.currentPlayer.hasItem(item) ||
				this.currentPlayer.getCurrentSpace().hasItem(item)) {
			if (item instanceof Powerable) {
				((Powerable)item).powerOff();
			} else {
				System.out.println("Cannot power on this item");
			}
		} else {
			System.out.println("That item isn't in here");
		}
	}

	/**
	 * Power item on action
	 * @param a ACTION_POWER_ON
	 */
	private void powerOnItem(Action a) {
		Item item = a.getDirectObject();
		
		if (this.currentPlayer.hasItem(item) ||
				this.currentPlayer.getCurrentSpace().hasItem(item)) {
			if (item instanceof Powerable) {
				((Powerable)item).powerOn();
			} else {
				System.out.println("Cannot power on this item");
			}
		} else {
			System.out.println("That item isn't in here");
		}
	}

	/**
	 * Item close action
	 * @param a ACTION_CLOSE
	 */
	private void closeItem(Action a) {
		Item item = a.getDirectObject();
		
		// Can't open something that isn't here
		if (this.currentPlayer.hasItem(item) ||
				this.currentPlayer.getCurrentSpace().hasItem(item)) {
			// Item must be Openable
			if (item instanceof Openable) {
				((Openable) item).close();
			} else {
				System.out.println("Cannot close this item");
			}
		} else {
			System.out.println("That item isn't in here");
		}
	}

	/**
	 * Item open action
	 * @param a ACTION_OPEN
	 */
	private void openItem(Action a) {
		Item item = a.getDirectObject();
		
		// Can't open something that isn't here
		if (this.currentPlayer.hasItem(item) ||
				this.currentPlayer.getCurrentSpace().hasItem(item)) {
			// Item must be Openable
			if (item instanceof Openable) {
				
				// Exit hatch grants victory on open
				if (((Openable)item).winOnOpen()) {
					((Openable)item).open();
					this.win();
				} else {
					((Openable) item).open();
				}
			} else {
				System.out.println("Cannot open this item");
			}
		} else {
			System.out.println("That item isn't in here");
		}
	}

	/**
	 * Item eat action
	 * @param a ACTION_EAT
	 */
	private void eatItem(Action a) {
		Item item = a.getDirectObject();
		
		// Eatable item must either be in Player's Inventory
		//  or in the current Space.
		if (this.currentPlayer.hasItem(item) ||
				this.currentPlayer.getCurrentSpace().hasItem(item)) {
			// Item must be Eatable
			if (item instanceof Eatable) {
				this.currentPlayer.eat((Food)item);
			} else {
				System.out.println("What are you doing?! You can't eat that!");
			}
		} else {
			System.out.println("You can't eat something that isn't in here or with you");
		}
	}

	/**
	 * Item drop action
	 * @param a ACTION_DROP
	 */
	private void dropItem(Action a) {
		Item item = a.getDirectObject();
		
		// Can't drop Item if Player doesn't have it in their Inventory
		if (this.currentPlayer.hasItem(item)) {
			
			// Some Items might not be droppable
			if (item instanceof Holdable) {
				System.out.println("Dropped " + item.getName());
				
				// Item is implicitly added to current Space
				this.currentPlayer.dropItem(item);
			} else {
				System.out.println("You cannot drop this item");
			}
		} else {
			System.out.println("You don't have that item");
		}
	}

	/**
	 * Item inspect action
	 * @param a ACTION_INSPECT
	 */
	private void inspectItem(Action a) {
		Item item = a.getDirectObject();
		
		// Only items in Inventory or current Space can be inspected
		if (this.currentPlayer.hasItem(item) ||
				this.currentPlayer.getCurrentSpace().hasItem(item)) {
			if (item instanceof Inspectable) {
				((Inspectable)item).inspect();
			} else {
				System.out.println("You cannot inspect this item");
			}
		} else {
			System.out.println("That item isn't in here");
		}
	}

	/**
	 * Item pickup action
	 * @param a ACTION_PICKUP
	 */
	private void pickupItem(Action a) {
		Item item = a.getDirectObject();
		
		// Don't allow picking up items that don't exist in
		//  Player's current location.
		if (this.currentPlayer.getCurrentSpace().hasItem(item)) {
			// Can't pickup non-holdable Items (e.g. CDShelf)
			if (item instanceof Holdable) {
				System.out.println("Picked up "+ item.getName());
				Item tempItem = this.currentPlayer.getCurrentSpace().takeItem(item.getName());
				this.currentPlayer.pickupItem(tempItem);
			} else {
				System.out.println("You can't pick up this item");
			}
		} else if (this.currentPlayer.hasItem(item)) {
			System.out.println("You already have that in your inventory");
		} else {
			System.out.println("That item isn't in here");
		}
	}

	/**
	 * Item playback action
	 * @param a Playback-related Action
	 */
	private void playbackItem(Action a) {
		Item item = a.getDirectObject();
		
		// Either the Player or the Space must have the item
		if (this.currentPlayer.hasItem(item) ||
				this.currentPlayer.getCurrentSpace().hasItem(item)) {
			// And it must support playback
			if (item instanceof Playable) {
				switch (a) {
					case ACTION_PLAY:
						((Playable)item).play();
						break;
					case ACTION_STOP:
						((Playable)item).stop();
						break;
					case ACTION_SKIP:
						((Playable)item).next();
						break;
					case ACTION_PREV:
						((Playable)item).prev();
						break;
					default:
						System.out.println("Unknow playback command");
						break;
				}
			} else {
				System.out.println(item.getName() + " doesn't support playback. You do know it's a " + item.getName() + ", right?");
			}
		} else {
			System.out.println("You don't have that item in your inventory");
		}
	}

	/**
	 * Taking action (taking Installable out of Hostable)
	 * @param a ActionTake
	 */
	private void takeItem(Action a) {
		if (a == Action.ACTION_TAKE) {
			Item contents = a.getDirectObject();
			Item container = a.getInDirectObject();
			
			// The container must be Hostable (otherwise there can't be
			//  anything inside of it, e.g. nothing to take).
			if (!(container instanceof Hostable)) {
				System.out.println(container.getName() + " cannot hold any items, so there's nothing to take");
				
			} else {
				// Check that the container's installedItem is the same as the
				//  contents.
				if(((Hostable)container).installedItem() == (Installable)contents) {
					boolean ok = ((Hostable)container).uninstall((Installable)contents);
					if (!ok) {
						System.out.println("Failed to remove item");
					} else {
						((Installable)contents).setIsInstalled(false);
						System.out.println("Removed " + contents.getName());
					}
				}	
				else {
					System.out.println("That item is not inside this " + container.getName());
				}
			}
		}
	}

	/**
	 * Putting action (inserting Installables into Hostables)
	 * @param a ActionPut
	 */
	private void putItem(Action a) {
		if (a == Action.ACTION_PUT) {
			Item itemToPut = a.getDirectObject();
			Item itemToPutInto = a.getInDirectObject();
			
			// Can't insert something that isn't in Player Inventory
			if (!this.currentPlayer.hasItem(itemToPut)) {
				System.out.println("You don't have that in your inventory");
				
			} else if (itemToPutInto == null) { // Target cannot be null
				System.out.println("You must give a target to put that into");
				
			} else if (!this.currentPlayer.hasItem(itemToPutInto)) {
				// The host-item must be in Player's inventory
				// TODO Implement an improved solution where Items can be inserted into Items in Spaces
				System.out.println("You can't put " + itemToPut.getName() + " into something that you don't have in your inventory");
				
			} else if (!(itemToPutInto instanceof Hostable) ||
					!(itemToPut instanceof Installable)) {
				// Both Items need to implement the correct methods
				System.out.println("You cannot put " + itemToPut.getName() + " into " + itemToPutInto.getName());
				
			} else {
				// Cannot insert something that is already inserted somewhere else
				if (!((Installable)itemToPut).isInstalled()) {
					// Attempt to put Item into Item
					boolean ok = this.currentPlayer.putItemInItem((Installable)itemToPut, (Hostable)itemToPutInto);
					if (!ok) {
						System.out.println("Failed to insert item");
					} else {
						((Installable)itemToPut).setIsInstalled(true);
						System.out.println("Inserted " + itemToPut.getName());
					}
				} else {
					// Find where the Installable is already inserted
					Hostable existingHost = null;
					for (Entry<String, Item> entry : this.getAvailableItems().entrySet()) {
						if (entry.getValue() instanceof Hostable) {
							Hostable host = (Hostable)entry.getValue();
							if (host.installedItem() == (Installable)itemToPut) {
								existingHost = host;
								break;
							}
						}
					}
					if (existingHost != null) {
						System.out.println(itemToPut.getName() + " is already inserted into " + ((Item) existingHost).getName());
					} else {
						System.out.println(itemToPut.getName() + " is already inserted into something else");
					}
				}
			}
		}
	}
	
	/**
	 * Move Player based on Action
	 * @param a Directional Action
	 */
	private void movePlayer(Action a) {
		System.out.println("You start moving...");
		this.currentPlayer.move(a);
	}
	
	/**
	 * Print out the help menu
	 */
	private void help() {
		System.out.println(" ---- Help ---- ");
		
		System.out.println("To view your current items, type \"Inventory\"");
		System.out.println("To travel in a direction like north, type \"Go North\"");
		System.out.println("Or for a specific currently visible place, type \"GoTo <name>\"");
		System.out.println("And to check what you see, type \"look\"");
		System.out.println("Some things can be inspected, type \"Inspect <item>\"");
		System.out.println("You can pick up items, type \"Take <item>\", e.g. \"Take Banana\"");
		System.out.println("And you can use them, type \"<item> <action>\", e.g. \"CD-Player Open\"");
		System.out.println("You can power on a powerable item by typing \"<item> On\"");
		System.out.println("To save you progress, type \"save\" and to quit, type \"quit\"");
		System.out.println("The rest you can find out yourself...");
		
		System.out.println(" -- Help END -- ");
	}
	
	/**
	 * Game win
	 */
	private void win() {
		try {
			this.gameState.setGameState(State.MENU_STATE);
			ThreadManager.clearAllThreads();
			System.out.println("You are blinded by a bright light as you open the hatch");
			System.out.println("Was this the way out?");
			System.out.print(".");
			Thread.sleep(1000);
			System.out.print(".");
			Thread.sleep(1000);
			System.out.print(".\n");
			Thread.sleep(1000);
			System.out.println("An audible sigh leaves you, as you slowly regain your vision and see the rescue team heading towards you.");
			Thread.sleep(3000);
			System.out.println("*************************************");
			Thread.sleep(500);
			System.out.println("***************THE END***************");
			Thread.sleep(500);
			System.out.println("*************************************");
			Thread.sleep(1000);
			System.out.println("A game by: Mikko Piuhola");
			Thread.sleep(1000);
			System.out.println();
			Thread.sleep(3000);
		} catch (Exception e) {
			System.out.println("Is the hatch broken? Or did you just break the game?");
			System.out.println("Type anything to return to the main menu:");
		}
	}
}
