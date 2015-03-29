package dungeonadventure.items;
import java.util.ArrayList;

import dungeonadventure.ThreadManager;
import dungeonadventure.observers.BatteryEmptyObserver;
import dungeonadventure.properties.Threadable;

/**
 * A battery. Can be used in item's that take a battery
 * @author Mikko Piuhola
 */
public class Battery extends Item implements Runnable,Threadable {

	private static final long serialVersionUID = 8420063717946301302L;
	
	private float currentCharge;
	private float maxCharge;
	transient private Thread usingThread;
	private boolean running;
	
	private ArrayList<BatteryEmptyObserver> batteryEmptyObservers;
	
	/**
	 * Constructor. Create a standard Battery.
	 */
	public Battery() {
		this("Battery", "standard AA battery");
	}
	
	/**
	 * Constructor. Create a Battery with a name and description. Standard charge.
	 * @param name Name of the Battery
	 * @param description Description of the Battery
	 */
	public Battery(String name, String description) {
		this(name, description, 180.0f); // TODO Update to actual amount
	}
	
	/**
	 * Constructor. Create a Battery with a name, description and a maximum charge.
	 * @param name Name of the Battery
	 * @param description Description of the Battery
	 * @param maxCharge A maximum charge level. Standard is 100.0f.
	 */
	public Battery(String name, String description, float maxCharge) {
		super(name, description);
		this.maxCharge = maxCharge;
		this.currentCharge = this.maxCharge;
		this.running = false;
		this.batteryEmptyObservers = new ArrayList<BatteryEmptyObserver>();
	}
	
	/**
	 * Start Battery thread to consume charge every 5000 ms
	 */
	public void run() {
		while (this.running && this.currentCharge > 0.0f) {
			try {
				Thread.sleep(1000);
				this.consume();
			} catch (InterruptedException e) {
				// Do nothing
			}
		}
		
		if (this.currentCharge <= 0.0f) {
			for(BatteryEmptyObserver obs : this.batteryEmptyObservers) {
				obs.batteryEmpty();
			}
		}
	}
	
	/**
	 * Add an empty battery observer to Battery
	 * @param obs Object that should observe Battery's "empty battery"
	 */
	public void addObserver(BatteryEmptyObserver obs) {
		this.batteryEmptyObservers.add(obs);
	}
	
	/**
	 * Start using Battery
	 */
	public void start() {
		this.running = true;
		this.usingThread = new Thread(this);
		this.usingThread.start();
		if (!ThreadManager.hasThread(this)) {
			ThreadManager.addThread(this);
		}
	}
	
	/**
	 * Interrupt usingThread.
	 * Used in saving.
	 */
	public void interrupt() {
		this.running = false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isRunning() {
		return this.usingThread != null && !this.usingThread.isInterrupted();
	}

	/**
	 * Stop using Battery
	 */
	public void stop() {
		this.running = false;
		ThreadManager.removeThread(this);
	}
	
	/**
	 * Consume battery by 1 unit of charge
	 */
	private void consume() {
		if (this.currentCharge >= 1.0f) {
			this.currentCharge -= 1.0f;
		} else {
			System.out.println("Not enough battery charge to use");
			this.interrupt();
		}
	}
	
	/**
	 * Charge battery to full charge
	 * @return Is battery fully charged
	 */
	public boolean charge() {
		if (this.currentCharge != this.maxCharge) {
			this.currentCharge = this.maxCharge;
		} else {
			System.out.println("Battery is already fully charged!");
			return false;
		}
		
		return this.currentCharge == this.maxCharge;
	}
	
	/**
	 * Does battery have charge left
	 * @return Has charge
	 */
	public boolean hasCharge() {		
		return this.currentCharge > 0.0f;
	}

}
