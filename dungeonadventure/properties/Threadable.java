package dungeonadventure.properties;

/**
 * Extending interface Runnable for threadable objects
 * @author Mikko Piuhola
 */
public interface Threadable {
	/**
	 * Start/Restart thread
	 */
	public void start();
	
	/**
	 * Interrupt thread
	 */
	public void interrupt();
	
	/**
	 * Check if thread is running
	 * @return Is running
	 */
	public boolean isRunning();
}
