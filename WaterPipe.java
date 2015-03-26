import java.io.Serializable;
import java.util.ArrayList;

/**
 * Water pipe. Starts breaking on spawn, and letting this break results in game over
 * @author Mikko Piuhola
 */
public class WaterPipe extends Item implements Runnable,Threadable,Serializable,Timeable {

	private static final long serialVersionUID = 3003347718982415282L;
	
	private float timeToBreak;
	private boolean broken;
	transient private Thread breakingThread;
	private ArrayList<WaterPipeBreakObserver> breakingObservers;
	
	private final static float MAX_TIMETOBREAK = 480.0f; // TODO Change to actual amount

	/**
	 * Constructor. The game's main water pipe
	 */
	public WaterPipe() {
		super("Pipe", "That's the mine's main water pipe, that looks like it's about to burst! You should get out of here quickly, that thing will fill the whole mine with water in seconds.", new String[]{"pipe","waterpipe","watermain"});
		this.breakingObservers = new ArrayList<WaterPipeBreakObserver>();
		this.timeToBreak = MAX_TIMETOBREAK;
		
		this.broken = false;
		this.breakingThread = new Thread(this);
		this.breakingThread.start();
		ThreadManager.addThread(this);
	}
	
	/**
	 * Add an bomb explosion observer to Bomb
	 * @param obs WaterPipeBreakObserver that will observe when the bomb will explode
	 */
	public void addObserver(WaterPipeBreakObserver obs) {
		this.breakingObservers.add(obs);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {
		while (!this.broken && this.timeToBreak >= 1.0f) {
			try {
				Thread.sleep(1000);
				this.timeToBreak--;
				if (this.timeToBreak <= 0.0f) {
					this.broken = true;
				}
			} catch (InterruptedException e) {
				// Do nothing
			}
		}
		
		if (this.broken && this.timeToBreak <= 0.0f) {
			for (WaterPipeBreakObserver obs : this.breakingObservers) {
				obs.waterPipeBreak();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start() {
		this.broken = false;
		this.breakingThread = new Thread(this);
		this.breakingThread.start();
		if (!ThreadManager.hasThread(this)) {
			ThreadManager.addThread(this);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void interrupt() {
		this.broken = true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isRunning() {
		return this.breakingThread != null && !this.breakingThread.isInterrupted();
	}
	
	/**
	 * Inspect the bomb
	 */
	@Override
	public void inspect() {
		if (!this.broken) {
			System.out.println(this.toString());
		} else {
			System.out.println("It's the broken water main, you have to get out now! Quickly, before it fills the whole mine with water!");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void spendTime(float sec) {
		this.timeToBreak -= sec;
	}
}
