package dungeonadventure.properties;

/**
 * Marker interface for Items that are installable into other objects
 * @author Mikko Piuhola
 */
public interface Installable {
	
	/**
	 * Is object currently installed somewhere
	 * @return Is currently installed
	 */
	public boolean isInstalled();
	
	/**
	 * Set installation state
	 * @param installed Is currently installed
	 */
	public void setIsInstalled(boolean installed);
}
