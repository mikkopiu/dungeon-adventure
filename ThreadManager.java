import java.util.ArrayList;

/**
 * ThreadManager is used to manage all the game's secondary and tertiary threads.
 * Threads are cleared on exit, stopped when entering the menu and restarted
 * when the Player continues the game.
 * @author Mikko Piuhola
 */
public final class ThreadManager {
	
	private static ArrayList<Threadable> threads;
	
	/**
	 * Private constructor, makes this a "static" class.
	 */
	private ThreadManager() {
		threads = new ArrayList<Threadable>();
	}
	
	/**
	 * Get a cloned list of all the threadable objects
	 * @return ArrayList of all the threads
	 */
	public static ArrayList<Threadable> getAllThreads() {
		return threads;
	}
	
	/**
	 * Check if thread exists in threads-list
	 * @param obj Object to check for
	 * @return Thread exists in ThreadManager
	 */
	public static boolean hasThread(Threadable obj) {
		return threads.contains(obj);
	}
	
	/**
	 * Add new threadable object
	 * @param obj Threadable object to add
	 */
	public static void addThread(Threadable obj) {
		if (threads == null) threads = new ArrayList<Threadable>();
		if (obj != null && !hasThread(obj)) {
			threads.add(obj);
		}
	}
	
	/**
	 * Remove thread from list
	 * @param obj Threadable object to remove
	 */
	public static void removeThread(Threadable obj) {
		if (threads == null) threads = new ArrayList<Threadable>();
		if (threads.contains(obj)) {
			threads.remove(obj);
		}
	}
	
	/**
	 * Restart all threads
	 */
	public static void restartAllThreads() {
		if (threads == null) threads = new ArrayList<Threadable>();
		for (Threadable obj : threads) {
			if (!obj.isRunning()) {
				obj.start();
			}
		}
	}
	
	/**
	 * Stop all threads
	 */
	public static void stopAllThreads() {
		if (threads == null) threads = new ArrayList<Threadable>();
		for (Threadable obj : threads) {
			obj.interrupt();
		}
	}
	
	/**
	 * Stop and remove all threads
	 */
	public static void clearAllThreads() {
		if (threads == null) threads = new ArrayList<Threadable>();

		// We need to make a clone, because threads can't be removed when
		//  iterating over threads-list.
		@SuppressWarnings("unchecked")
		ArrayList<Threadable> tempThreads = (ArrayList<Threadable>) threads.clone();
		for (Threadable obj : tempThreads) {
			obj.interrupt();
			removeThread(obj);
		}
	}
	
	// FOR TIMEABLES
	
	/**
	 * Advance time on all timeable threads.
	 * Used when spending time in some other activity, e.g. charging flashlight
	 * @param amount Amount of time in seconds to advance Timeable objects
	 */
	public static void advanceTime(int amount) {
		if (threads == null) threads = new ArrayList<Threadable>();
		for (Threadable obj : threads) {
			if (obj instanceof Timeable) {
				((Timeable)obj).spendTime(amount);
			}
		}
	}
}
