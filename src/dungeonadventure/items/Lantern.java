package dungeonadventure.items;

import dungeonadventure.observers.BatteryEmptyObserver;
import dungeonadventure.properties.Holdable;


/**
 * A lantern. Can be powered ON/OFF. Uses petroleum for power.
 * @author Mikko Piuhola
 */
public class Lantern extends Lightsource implements BatteryEmptyObserver,Holdable {

	private static final long serialVersionUID = 2281527806534393078L;
	
	private Battery battery; // Just a name for our "petroleum" to be consistent with flashlight
	
	/**
	 * Create a standard Lantern.
	 */
	public Lantern() {
		this("Lantern", "a rusty old lantern with some petroleum left in it");
	}
	
	/**
	 * Create a Lantern with a name and description.
	 * @param name Name of the Lantern
	 * @param description Description of the Lantern
	 */
	public Lantern(String name, String description) {
		super(name, description, 180); // Lantern's visibility range is 180 degrees
		this.battery = new Battery("Petroleum", "some 30 year old petroleum", 240.0f); // TODO Implement differently for INDIRECTOBJECT Actions, Update to actual amount
		this.battery.addObserver(this);
	}
	
	// POWERABLE
	
	/**
	 * Power on the the lantern
	 * @return Is lantern powered on
	 */
	@Override
	public boolean powerOn() {
		if (this.hasBattery()) {
			if (this.battery.hasCharge()) {
				super.powerOn();
				this.battery.start();
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
			this.battery.interrupt();
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
		return this.battery != null;
	}
	
	// BATTERYEMPTYOBSERVER

	@Override
	public void batteryEmpty() {
		this.powerOff();
		System.out.println("The lantern ran out of petroleum. It must have only had a few drops of petroleum left.");
	}
}
