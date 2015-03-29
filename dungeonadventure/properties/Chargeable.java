package dungeonadventure.properties;

/**
 * Interface for chargeable objects
 * @author Mikko Piuhola
 */
public interface Chargeable {
	
	/**
	 * Charge objects, object sets what happens exactly
	 */
	public void charge();
	
	/**
	 * Is the object infinitely re-chargeable
	 * @return Is infinitely re-chargeable
	 */
	public boolean isInfinitelyChargeable();
	
	/**
	 * Was the object re-charged already.
	 * Used for objects with only one allowed re-charge
	 * @return Was the object already re-charged
	 */
	public boolean wasCharged();
}
