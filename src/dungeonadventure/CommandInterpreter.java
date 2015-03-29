package dungeonadventure;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import dungeonadventure.items.Item;

/**
 * Command interpreter.
 * Used to prompt the user for input and validate it against given commands.
 * @author Mikko Piuhola
 */
public class CommandInterpreter {

	private Scanner scanner;
	
	/**
	 * Create a new scanner for System.in input
	 */
	public CommandInterpreter() {
		this.scanner = new Scanner(System.in);
	}
	
	/**
	 * Scan for user input. Only return when there's at least some input.
	 * @return User input string
	 */
	public String scanCommand() {
		String userInput = "";
		while (userInput.length() < 1) {
			userInput = this.scanner.nextLine().toLowerCase();
		}
		
		return userInput;
	}
	
	/**
	 * Interpret user input as an Action
	 * @param input User input string
	 * @param items Available items
	 * @return Interpreted Action
	 */
	public Action interpretCommand(String input, HashMap<String, Item> items) {
		if (input.equals("")) {
			return Action.ACTION_PASS;
		} else {
			return getAction(input.toLowerCase().split(" "), items);
		}
	}
	
	/**
	 * Get string as an interpreted Action
	 * @param string User input string
	 * @param items Available items
	 * @return Interpreted Action
	 */
	private Action getAction(String[] string, HashMap<String, Item> items) throws IndexOutOfBoundsException {
		// Extra validation for empty strings
		if (string == null || string.length == 0) {
			return Action.ACTION_PASS;
		}

		// We don't need to care about "go" etc. just the direction,
		//  so just remove the extra command
		if (string[0].compareTo("go") == 0 ||
				string[0].compareTo("move") == 0 ||
				string[0].compareTo("travel") == 0 ||
				string[0].compareTo("proceed") == 0 ||
				string[0].compareTo("turn") == 0) {

			String[] command = Arrays.copyOfRange(string, 1, string.length);
			return getAction(command, items);
		}

		// If the user wants to go to a specific, visible location
		//  they can say the Space's name, e.g. "goto room2".
		if (string[0].compareTo("goto") == 0) {
			Action action = Action.ACTION_GO_LOCATION;
			action.setLocation(string[1]); // Save the Space's name
			return action;
		}

		// At this point input can be a direction ("north"), general ("help"),
		//  direct-object ("battery take"), indirect-object ("insert cd in cdplayer") or
		//  some other command.
		String s = string[0];
		Action action = null;
		for (Action a : Action.values()) {
			// and their aliases
			for (String alias : a.getAliases()) {
				// Break out of both loops when a match is found
				if (s.compareTo(alias) == 0) {
					action = a;
					break;
				}
			}
			
			if (action != null) {
				break;
			}
		}
		
		// Sometimes user might give the target object first (e.g. "flashlight on")
		for (String key : items.keySet()) {				
			if (s.compareTo(key) == 0) {
				if (string.length > 1) {
					// Move Item to index 1
					List<String> tempArr = new ArrayList<String>(Arrays.asList(string));
					tempArr.set(0, tempArr.get(1));
					tempArr.set(1, s);
					
					// And re-run interpreter
					return getAction(tempArr.toArray(new String[tempArr.size()]), items);
				} else {
					System.out.println("What do you want to with that?");
					return Action.ACTION_PASS; // Action passed
				}
			}
		}

		// Given command didn't match any known commands
		if (action == null) {
			return Action.ACTION_ERROR;
		}
		
		// If it did, we need to find the type
		switch (action.getType()) {
			case TYPE_DIRECTIONAL:
				return action; // No further action necessary
			case TYPE_TURNING:
				return action; // No further action necessary
			case TYPE_HASDIRECTOBJECT:
	
				// Test if there's some direct object
				// E.g. "eat pizza"
				if(string.length > 1) {

					// Find the direct object in Player inventory
					String directObjectName = string[1];
					Item item = items.get(directObjectName);
					
					// Set item as the direct-object
					action.setDirectObject(item);
					
					// Item might be null here, but that is handled in engine
					return action;
				}
				else {
					System.out.println("What do you want to '" + string[0] + "'");
					return Action.ACTION_PASS; // Action passed
				}
			case TYPE_HASINDIRECTOBJECT:
				
				// Test if there's an indirect-object
				// E.g. "insert cd in cdplayer"
				if (string.length > 1) {
					
					String directObjectName = string[1];
					Item item = items.get(directObjectName);
					action.setDirectObject(item);
					
					if (string.length > 2) {
						String in = string[2];
						
						// Pass action if the 3rd word isn't "in" or "from"
						if (in.equals("in") || in.equals("into")|| in.equals("from")) {
							
							// Verify that a target is supplied
							if (string.length > 3) {
								String indirectObjectName = string[3];
								Item indirectItem = items.get(indirectObjectName);
								action.setIndirectObject(indirectItem);
								
								// Direct and indirect-objects might both be null here, handled in engine
								return action;	
							} else {
								System.out.println("You must give a target for your action");
								return Action.ACTION_ERROR;
							}
							
						} else {
							return Action.ACTION_PASS;
						}
					}
					
				} else {
					System.out.println("You must give a target to '" + string[0] + "'");
					return Action.ACTION_ERROR;
				}
				
				break;
			case TYPE_HASNOOBJECT:
				return action;
			case TYPE_UNKNOWN:
				if (string[0].equals("quit") || string[0].equals("exit")) {
					return Action.ACTION_QUIT;
				}
				return Action.ACTION_ERROR;
			default:
				System.out.println("Unknown command");
				break;
		}
		
		// Just pass the action if it didn't match anything
		return Action.ACTION_PASS;
	}
	
}
