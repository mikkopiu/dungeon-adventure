package dungeonadventure.items;

import dungeonadventure.ThreadManager;
import dungeonadventure.observers.BatteryEmptyObserver;
import dungeonadventure.properties.Chargeable;
import dungeonadventure.properties.Holdable;


/**
 * A flashlight. Can be powered ON/OFF and takes a Battery.
 * @author Mikko Piuhola
 */
public class Flashlight extends Lightsource implements BatteryEmptyObserver,Holdable,Chargeable {

	private static final long serialVersionUID = 7054094504373510226L;

	private Battery currentBattery;
	protected boolean allowInfiniteCharging;
	protected boolean wasCharged;
	
	/**
	 * Constructor. Create a standard Flashlight.
	 */
	public Flashlight() {
		this("Flashlight", "Just a standard flashlight.");
	}

	/**
	 * Constructor. Create a Flashlight with a name and description.
	 * @param name Name for Flashlight
	 * @param description Description for Flashlight
	 */
	public Flashlight(String name, String description) {
		super(name, description);
		this.currentBattery = new Battery(); // TODO IMPLEMENT DIFFERENTLY FOR INDIRECTOBJECT ACTIONS
		this.currentBattery.addObserver(this);
		this.allowInfiniteCharging = false;
		this.wasCharged = false;
	}
	
	/**
	 * Turn flashlight ON
	 * @return Is flashlight powered on
	 */
	@Override
	public boolean powerOn() {
		if (this.hasBattery()) {
			if (this.currentBattery.hasCharge()) {
				super.powerOn();
				this.currentBattery.start();
			} else {
				System.out.println("You don't have enought charge in the battery");
				this.setPowerOn(false);
			}
		} else {
			System.out.println("There's no battery in the " + this.getName());
			this.setPowerOn(false);
		}
		
		return this.isPoweredOn();
	}
	
	/**
	 * Turn flashlight OFF
	 * @return Is flashlight powered off
	 */
	@Override
	public boolean powerOff() {
		if (this.hasBattery()) {
			super.powerOff();
			this.currentBattery.interrupt();
		} else {
			System.out.println("There's no battery in the " + this.getName());
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
	
//	// HOSTABLE
//	
//	@Override
//	public Item installedItem() {
//		return this.currentBattery;
//	}
//	
//	/**
//	 * Install battery to Flashlight
//	 * @param Battery to install
//	 * @return Success
//	 */
//	@Override
//	public boolean install(Item item) {
//		
//		// Item must of course be a Battery
//		if (item instanceof Battery) {
//			if (!this.hasBattery()) {
//				this.currentBattery = (Battery)item;
//				this.currentBattery.addObserver(this);
//				return true;
//			} else {
//				System.out.println(this.getName()
//						+ " already has a battery inserted!");
//				return false;
//			}
//		} else {
//			return false;
//		}
//	}
//
//	/**
//	 * Remove battery from flashlight
//	 * @return Success
//	 */
//	@Override
//	public boolean uninstall(Item item) {
//		if (this.hasBattery()) {
//			this.currentBattery = null;
//			return true;
//		} else {
//			System.out.println("No battery to remove!");
//			return false;
//		}
//	}
	
	// BATTERYEMPTYOBSERVER
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void batteryEmpty() {
		this.powerOff();
		System.out.println("The " + this.getName() + "'s battery ran out! There should be a charging station for flashlights somewhere in here...");
	}
	
	// CHARGEABLE

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void charge() {
		boolean charged = this.currentBattery.charge();
		if (charged) {
			System.out.println("The charging station says that this should take 30 seconds.");
			// Fast-forward all Timeable threads (i.e. waterpipe and bomb)
			ThreadManager.advanceTime(29); // 29s + 1000ms = 30s
			
			this.wasCharged = true;
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				
			}
			System.out.println("Successfully charged the " + this.getName());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isInfinitelyChargeable() {
		return this.allowInfiniteCharging;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean wasCharged() {
		return this.wasCharged;
	}
}
