
/**
 * Interface for powerable objects. Can be powered ON/OFF.
 * @author Mikko Piuhola
 */
public interface Powerable {
	/**
	 * Power object ON
	 * @return Was powered on successfully
	 */
	public boolean powerOn();
	
	/**
	 * Power object OFF
	 * @return Was powered down successfully
	 */
	public boolean powerOff();
	
	/**
	 * Check object's power state
	 * @return Is object powered on
	 */
	public boolean isPoweredOn();
}
