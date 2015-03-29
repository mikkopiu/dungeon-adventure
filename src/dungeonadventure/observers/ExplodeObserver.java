package dungeonadventure.observers;

/**
 * Observers of Bomb's explosion. Explosion is a game losing event.
 * @author Mikko Piuhola
 */
public interface ExplodeObserver {
	
	/**
	 * React to bomb's explosion
	 */
	public void bombExplosion();
}
