package dungeonadventure.properties;

/**
 * Interface for objects with playback-capabilities
 * @author Mikko Piuhola
 */
public interface Playable {
	/**
	 * Start playback
	 * @return Is object playing
	 */
	public boolean play();
	
	/**
	 * Stop playback
	 * @return Is object stopped
	 */
	public boolean stop();
	
	/**
	 * Next track/channel etc.
	 * @return Current channel
	 */
	public int next();
	
	/**
	 * Prev track/channel etc.
	 * @return Current channel
	 */
	public int prev();
}
