
/**
 * A lightsource that can be powered on. Must extend to create an actual Class.
 * @author Mikko Piuhola
 */
public abstract class Lightsource extends Item implements Powerable {

	private static final long serialVersionUID = -1238407248256801842L;
	
	public boolean powerState;
	private int visibilityRange;
	
	/**
	 * Constructor. Create a new Lightsource with a name and description.
	 * @param name Name of the Lightsource
	 * @param description Description of the Lightsource
	 */
	public Lightsource(String name, String description) {
		this(name, description, 90); // Default visibility is 90 degrees
	}
	
	/**
	 * Constructor. Create a new Lightsource with a name, description and
	 * a visibility (in degrees).
	 * @param name Name of the Lightsource
	 * @param description Description of the Lightsource
	 * @param visibility Visibility in degrees (e.g. 90 (out of 360))
	 */
	public Lightsource(String name, String description, int visibility) {
		super(name, description);
		this.powerState = false;
		this.visibilityRange = visibility <= 360 ? visibility : 360;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean powerOn() {
		
		System.out.println("Turning " + this.getName() + " ON");
		if (!this.isPoweredOn()) {
			this.setPowerOn(true);
		} else {
			System.out.println(this.getName() + " is already ON!");
		}
		
		return this.isPoweredOn();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean powerOff() {
		
		System.out.println("Turning " + this.getName() + " OFF");
		if (this.isPoweredOn()) {
			this.setPowerOn(false);
		} else {
			System.out.println(this.getName() + " is already OFF!");
		}
		
		return !this.isPoweredOn();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPoweredOn() {
		return this.powerState;
	}
	
	/**
	 * Change power state
	 * @param powerOn Set power on (true if on, false if off)
	 * @return Success
	 */
	protected boolean setPowerOn(boolean powerOn) {
		this.powerState = powerOn;		
		return this.powerState == powerOn;
	}
	
	/**
	 * Get Lightsource's visibility range in degrees
	 * @return Visibility range in degrees
	 */
	public int getVisibilityRange() {
		return this.visibilityRange;
	}
}
