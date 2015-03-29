package dungeonadventure.items;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import dungeonadventure.properties.Inspectable;
import dungeonadventure.properties.Nameable;

/**
 * An item. Has a name and a description. Must be extended to create actual Items.
 * @author Mikko Piuhola
 */
public abstract class Item implements Nameable,Inspectable,Serializable {

	private static final long serialVersionUID = -7274245499404852584L;
	
	private String name;
	private String description;
	private String[] aliases;
	
	/**
	 * Constructor. Create a new Item with a name and description.
	 * @param name Name of the Item
	 * @param description Description of the Item
	 */
	public Item(String name, String description) {
		this(name, description, new String[]{name});
	}
	
	/**
	 * Constructor. Create a new Item with a name, description and aliases
	 * @param name Name of the Item
	 * @param description Description of the Item
	 * @param aliases Item's possible aliases
	 */
	public Item(String name, String description, String[] aliases) {
		this.name = name;
		this.description = description;
		
		// Set all aliases as lowercase
		List<String> a = (List<String>) Arrays.asList(aliases);
		for(int i=0; i < a.size(); i++) {
		  a.set(i, a.get(i).toLowerCase());
		}

		this.aliases = a.toArray(new String[a.size()]);
	}
	
	// NAMEABLE
	
	/**
	 * Get item's name
	 * @return Item's name
	 */
	public String getName() {
		return this.name;
	}
	
	// INSPECTABLE
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void inspect() {
		System.out.println(this.description);
	}
	
	/**
	 * Get Item's description
	 */
	@Override
	public String toString() {
		return this.description;
	}
	
	/**
	 * Get Item's aliases
	 * @return Array of alias-strings for Item
	 */
	public String[] getAliases() {
		return this.aliases;
	}
}
