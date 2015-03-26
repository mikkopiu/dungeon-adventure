import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


/**
 * A game controller. Starts the game and runs the game loop.
 * @author Mikko Piuhola
 */
public class GameController {

	private CommandInterpreter ci;
	private GameEngine engine;
	private GameState gameState;
	
	/**
	 * Constructor with a save game file
	 */
	public GameController() {
		
		this.gameState = new GameState();
		this.ci = new CommandInterpreter();
		this.engine = new GameEngine();
	}
	
	/**
	 * Initialize GameController.
	 * Starts the GameEngine and game loop
	 */
	public void init() {
		// Start your engines
		this.gameLoop();
	}
	
	/**
	 * Game loop
	 */
	public void gameLoop() {
		
        while(this.gameState.getGameState() != State.EXIT_STATE) {
        	
        	switch(this.gameState.getGameState()) {
        	
	        	case MENU_STATE:
	        		this.drawMenu();
	        		break;
	        	case SAVE_STATE:
	        		System.out.println("Give a name for your save:");
	        		
	        		String fileOutput = this.ci.scanCommand();
	        		
	        		// Game saving
					try (FileOutputStream fs = new FileOutputStream(fileOutput + ".bin")) {
						
						ObjectOutputStream os = new ObjectOutputStream(fs);
						
						os.writeObject(this.gameState);
						os.writeObject(this.engine);
						
						// Save threads so we are able to restart them on load
						os.writeObject(ThreadManager.getAllThreads());
						os.close();
						
						// Return to game
						System.out.println("Saved game successfully");
						System.out.println("Returning to the game...");
						this.gameState.setGameState(State.RUN_STATE);
						
					} catch (FileNotFoundException e) {
						System.err.println("Save file not found");
						e.printStackTrace();
					} catch (IOException e) {
						System.err.println("Save file inaccessible");
						e.printStackTrace();
					}
	        		break;
	        	case LOAD_STATE:
	        		
	        		System.out.println("Give save's name:");
	        		
	        		String fileInput = this.ci.scanCommand();
	        		
	        		// Attempt to load save-game from file
	    			try (FileInputStream fi = new FileInputStream(fileInput + ".bin")) {
	    				
	    				// Start off the input streams
	    				ObjectInputStream os = new ObjectInputStream(fi);
	    				
	    				// Read GameState and GameEngine from file
	    				this.gameState = (GameState)os.readObject();
	    				this.engine = (GameEngine)os.readObject();
	    				
	    				// Read threadable objects from file
	    				//  and add them back to the ThreadManager
	    				ThreadManager.clearAllThreads(); // Clear any residue threads first
	    				@SuppressWarnings("unchecked")
						ArrayList<Threadable> threadList = (ArrayList<Threadable>)os.readObject();
	    				for (Threadable obj : threadList) {
	    					ThreadManager.addThread(obj);
	    				}
	    				
	    				// Close the object-stream
	    				os.close();
	    				
	    				// Begin game
	    				System.out.println("Loaded game");
	    				this.gameState.setGameState(State.RUN_STATE);
	    				
	    			} catch (FileNotFoundException e) {
						System.err.println("Save file not found");
						this.gameState.setGameState(State.MENU_STATE);
					} catch (IOException e) {
						System.err.println("Save file inaccessible");
						this.gameState.setGameState(State.MENU_STATE);
					} catch (ClassNotFoundException e) {
						System.err.println("Something wrong in the save file");
						this.gameState.setGameState(State.MENU_STATE);
					}
	    			
	    			break;
	    		case RUN_STATE:
	    			try {
	    				// Restart any pre-existing threads
	    				ThreadManager.restartAllThreads();

	    				String input = "";
	    				while (this.gameState.getGameState() == State.RUN_STATE) {
	    					
	    					// Scan for user commands
	    					input = this.ci.scanCommand();
	    					
	    					// Catch Player lose and win situations (the state
	    					//  has changed before the player has made any
	    					//  inputs, but we can't stop scanning so we have to
	    					//  catch it here).
	    					if (!this.engine.isPlayerAlive() || this.gameState.getGameState() == State.MENU_STATE) {
	    						System.out.println("Returning to the menu...");
	    						break;
	    					}
	    					
	    					// Catch exit, save and menu commands
	    					if (input.compareTo("exit") == 0 ||
	    							input.compareTo("quit") == 0) {
	    						this.gameState.setGameState(State.EXIT_STATE);
	    					} else if (input.compareTo("save") == 0) {
	    						this.gameState.setGameState(State.SAVE_STATE);
	    					} else if (input.compareTo("menu") == 0) {
	    						this.gameState.setGameState(State.MENU_STATE);
	    					} else {
	    						// Any other user actions (actual game input)
	    						Action a = this.ci.interpretCommand(input, this.engine.getAvailableItems());
	    						this.engine.executeCommand(a);
	    					}
	    				}
	    				
	    				// "Pause" all threads (Objects keep state, threads are restarted on return)
	    				ThreadManager.stopAllThreads();
	    				
	    			} catch (Exception e) {
	    				System.err.println("I don't understand that");
	    				gameLoop();
	    			}
	    			
	    			break;
	    		case EXIT_STATE:
	    		default:
	    			break;
    		}
        }
    	
    	// Exit
    	System.err.println("Exiting...");
		ThreadManager.clearAllThreads();
	}
	
	/**
	 * Draw main menu
	 */
	public void drawMenu() {
		
		System.out.println("======================Menu======================");
		System.out.println("Type the number to select:");
		System.out.println();
		System.out.println("[1] Start new game");
		System.out.println("[2] Load saved game");
		System.out.println("[3] Exit game");
		System.out.println("================================================");
		
		String input = "";
		
		while (this.gameState.getGameState() == State.MENU_STATE) {
			
			input = this.ci.scanCommand();
			
			switch (input) {
				case "1":
					System.out.println("Starting new game...");
					ThreadManager.clearAllThreads();
					this.engine.init(this.gameState);
					this.gameState.setGameState(State.RUN_STATE);
					break;
				case "2":
					this.gameState.setGameState(State.LOAD_STATE);
					break;
				case "3":
					this.gameState.setGameState(State.EXIT_STATE);
					break;
				default:
					System.out.println("Please choose an item from the list");
					break;
			}
		}
	}
}
