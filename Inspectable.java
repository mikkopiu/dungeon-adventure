
/**
 * Interface for inspectable Items, can be described and inspected
 * @author Mikko Piuhola
 */
public interface Inspectable {
	
	/**
	 * Print out the description of the object
	 */
	public void inspect();
	
	/**
	 * Get the object's description as a String
	 * @return Object's description
	 */
	public String toString();
}
