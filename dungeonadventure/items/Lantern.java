package dungeonadventure.items;

import dungeonadventure.observers.BatteryEmptyObserver;
import dungeonadventure.properties.Holdable;


/**
 * A lantern. Can be powered ON/OFF. Uses petroleum for power.
 * @author Mikko Piuhola
 */
public class Lantern extends Lightsource implements BatteryEmptyObserver,Holdable {

	private static final long serialVersionUID = 2281527806534393078L;
	
	private Battery currentBattery; // Just a name for our "petroleum" to be consistent with flashlight
	
	/**
	 * Constructor. Create a standard Lantern.
	 */
	public Lantern() {
		this("Lantern", "a rusty old lantern with some petroleum left in it");
	}
	
	/**
	 * Constructor. Create a Lantern with a name and description.
	 * @param name Name of the Lantern
	 * @param description Description of the Lantern
	 */
	public Lantern(String name, String description) {
		super(name, description, 180); // Lantern's visibility range is 180 degrees
		this.currentBattery = new Battery("Petroleum", "some 30 year old petroleum", 240.0f); // TODO IMPLEMENT DIFFERENTLY FOR INDIRECTOBJECT ACTIONS, UPDATE TO ACTUAL AMOUNT
		this.currentBattery.addObserver(this);
	}
	
	// POWERABLE
	
	/**
	 * Power on the the lantern
	 * @return Is lantern powered on
	 */
	@Override
	public boolean powerOn() {
		if (this.hasBattery()) {
			if (this.currentBattery.hasCharge()) {
				super.powerOn();
				this.currentBattery.start();
			} else {
				System.out.println("You don't have enought petroleum to fire up the lantern");
				this.setPowerOn(false);
			}
		} else {
			System.out.println("There's no petroleum in the " + this.getName());
			this.setPowerOn(false);
		}
		
		return this.isPoweredOn();
	}
	
	/**
	 * Turn lantern OFF
	 * @return Is lantern powered off
	 */
	@Override
	public boolean powerOff() {
		if (this.hasBattery()) {
			super.powerOff();
			this.currentBattery.interrupt();
		} else {
			System.out.println("There's no petroleum in the " + this.getName());
			this.setPowerOn(false);
		}
		
		return !this.isPoweredOn();
	}
	
	/**
	 * Is a battery inserted
	 * @return Battery inserted
	 */
	public boolean hasBattery() {		
		return this.currentBattery != null;
	}
	
	// BATTERYEMPTYOBSERVER

	@Override
	public void batteryEmpty() {
		this.powerOff();
		System.out.println("The lantern ran out of petroleum. It must have only had a few drops of petroleum left.");
	}
}
