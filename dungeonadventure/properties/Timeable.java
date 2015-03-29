package dungeonadventure.properties;

/**
 * Interface for timeable objects, namely Bomb and WaterPipe.
 * Used to fast-forward their time in case the Player does something
 * that takes time.
 * @author Mikko Piuhola
 */
public interface Timeable {
	/**
	 * Fast-forward Timeable's timer by given amount
	 * @param sec Seconds to fast-forward by
	 */
	public void spendTime(float sec);
}
