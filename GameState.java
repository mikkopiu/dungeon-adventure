import java.io.Serializable;

/**
 * GameState manages the game's state through the enum State
 * @author Mikko Piuhola
 */
public class GameState implements Serializable {

	private static final long serialVersionUID = 1467628712693571625L;
	
	private State currentState;
	
	/**
	 * Constructor for GameState wrapper
	 */
	public GameState() {
		// Start off in the menu
		currentState = State.MENU_STATE;
	}
	
	/**
	 * Get current game state
	 * @return Current game state
	 */
	public State getGameState() {
		return this.currentState;
	}
	
	/**
	 * Set current game state
	 * @param state New game state
	 */
	public void setGameState(State state) {
		this.currentState = state;
	}
}