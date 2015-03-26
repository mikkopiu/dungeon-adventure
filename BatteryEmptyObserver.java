
/**
 * Interface for observing a Battery's "empty charge"-state.
 * @author Mikko Piuhola
 */
public interface BatteryEmptyObserver {
	
	/**
	 * React to battery empty observable event
	 */
	public void batteryEmpty();
}
