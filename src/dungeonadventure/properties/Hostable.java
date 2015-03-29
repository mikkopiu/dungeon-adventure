package dungeonadventure.properties;

import dungeonadventure.items.Item;


/**
 * Interface for Items that can take in other Items
 * @author Mikko Piuhola
 */
public interface Hostable {
	
	/**
	 * Install object into this object
	 * @param obj Object to install
	 * @return Success
	 */
	public boolean install(Installable obj);
	
	/**
	 * Remove object from this object
	 * @param obj Object to remove
	 * @return Success
	 */
	public boolean uninstall(Installable obj);
	
	/**
	 * Get currently installed object or null
	 * @return Currently installed object or null
	 */
	public Item installedItem();
}
