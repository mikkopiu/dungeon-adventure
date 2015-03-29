package dungeonadventure.items;
import java.util.ArrayList;

import dungeonadventure.ThreadManager;
import dungeonadventure.observers.ExplodeObserver;
import dungeonadventure.properties.Powerable;
import dungeonadventure.properties.Threadable;
import dungeonadventure.properties.Timeable;

/**
 * Bomb. Starts timer for exploding on spawn. Explosion is a game losing situation.
 * Can be defused.
 * @author Mikko Piuhola
 */
public class Bomb extends Item implements Runnable,Powerable,Threadable,Timeable {

	private static final long serialVersionUID = 5148051826485246999L;
	
	private boolean armed;
	private float timeToExplode; // Seconds
	private ArrayList<ExplodeObserver> explodeObservers;
	transient private Thread armedThread;
	
	private final static float MAX_TIMETOEXPLODE = 180.0f; // TODO Update to actual amount

	/**
	 * Constructor for the game's main bomb
	 */
	public Bomb() {
		super("Bomb", "the bomb you set up do dig further towards south, and it's still ticking down! Defuse it quickly, the mine might not be able to take another explosion!", new String[]{"bomb","explosive","explosives","dynamite"});
		this.timeToExplode = MAX_TIMETOEXPLODE;
		this.explodeObservers = new ArrayList<ExplodeObserver>();
		
		// Arm the Bomb on spawn
		this.armed = true;
		this.armedThread = new Thread(this);
		this.armedThread.start();
		ThreadManager.addThread(this);
	}
	
	/**
	 * Add an bomb explosion observer to Bomb
	 * @param obs Object that will observe when the bomb will explode
	 */
	public void addObserver(ExplodeObserver obs) {
		this.explodeObservers.add(obs);
	}

	/**
	 * Bomb cannot be powered on.
	 * It automatically gets powered on on spawn and can only be defused.
	 * @return False to indicate forbidden method
	 */
	@Override
	public boolean powerOn() {
		System.out.println("You defused the bomb, you shouldn't re-arm it now. You have to find your way out.");
		return false;
	}

	/**
	 * Defuse bomb
	 * @return Is bomb disarmed
	 */
	@Override
	public boolean powerOff() {
		if (this.isPoweredOn()) {
			this.armed = false;
			System.out.println("You successfully defused the bomb");
		} else {
			System.out.println("You already defused the bomb");
		}

		return !this.armed;
	}

	/**
	 * Is bomb armed
	 * @return Bomb arming-state
	 */
	@Override
	public boolean isPoweredOn() {
		return this.armed;
	}

	/**
	 * Bomb is armed, notify on explosion
	 */
	@Override
	public void run() {
		while (this.isPoweredOn() && this.timeToExplode >= 1.0f) {
			try {
				Thread.sleep(1000);
				this.timeToExplode--;
			} catch (InterruptedException e) {
				// Do nothing
			}
		}
		
		if (this.isPoweredOn() && this.timeToExplode <= 0.0f) {
			for (ExplodeObserver obs : this.explodeObservers) {
				obs.bombExplosion();
				this.armed = false;
			}
		}
	}
	
	/**
	 * Inspect the bomb
	 */
	@Override
	public void inspect() {
		if (this.isPoweredOn()) {
			System.out.println("It's " + this.toString() + " that is about to explode! The timer on it reads: " + this.timeToExplode + "s.");
		} else {
			System.out.println("It's the bomb you defused. Still, better not stay around if you aren't quite sure of the job you did.");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start() {
		this.armed = true;
		this.armedThread = new Thread(this);
		this.armedThread.start();
		if (!ThreadManager.hasThread(this)) {
			ThreadManager.addThread(this);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void interrupt() {
		this.armed = false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isRunning() {
		return this.armedThread != null && !this.armedThread.isInterrupted();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void spendTime(float sec) {
		this.timeToExplode -= sec;
	}
}
