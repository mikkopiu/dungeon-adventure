package dungeonadventure;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import dungeonadventure.items.Item;

/**
 * Inventory. Can contain Items (up to 10 at a time).
 * @author Mikko Piuhola
 */
public class Inventory implements Serializable {

	private static final long serialVersionUID = -8877074895886372762L;
	
	private HashMap<String,Item> itemList;
	private final int INVENTORY_LIMIT = 10; // Static inventory size, could be expanded
	
	/**
	 * Constructor
	 */
	public Inventory() {
		this.itemList = new HashMap<String,Item>();
	}
	
	/**
	 * Add item to inventory
	 * @param item Item to add
	 * @return Was item successfully added
	 */
	public boolean addItem(Item item) {
		
		boolean addSuccess;
		
		if (this.itemList.size() < this.INVENTORY_LIMIT) {
			if (this.itemList.get(item.getName().toLowerCase()) == null) {
				this.itemList.put(item.getName().toLowerCase(), item);
				
				if (this.itemList.size() == this.INVENTORY_LIMIT) {
					System.out.println("Inventory is now full");
				}
				
				addSuccess = true;
			} else {
				System.out.println(item.getName()
						+ " is already in the inventory!");
				addSuccess = false;
			}
		} else {
			System.out.println("Inventory full!");
			addSuccess = false;
		}
		
		return addSuccess;
	}
	
	/**
	 * Remove item from inventory
	 * @param itemName Name of item to remove
	 * @return Was item successfully removed
	 */
	public boolean removeItem(String itemName) {
		
		boolean removeSuccess;
		
		if (this.itemList.get(itemName.toLowerCase()) != null) {
			this.itemList.remove(itemName.toLowerCase());
			removeSuccess = true;
		} else {
			System.out.println("No '" + itemName + "' exists in inventory");
			removeSuccess = false;
		}
		
		if (this.itemList.size() == 0) {
			System.out.println("Inventory is now empty");
		}
		
		return removeSuccess;
	}
	
	/**
	 * Get item by name
	 * @param itemName Name of the item to get
	 * @return Found item
	 */
	public Item getItem(String itemName) {
		Item foundItem = this.itemList.get(itemName.toLowerCase());
		return foundItem;
	}
	
	/**
	 * Get list of items in inventory as a HashMap
	 * @return HashMap of items in inventory
	 */
	public HashMap<String,Item> getItemList() {
		return this.itemList;
	}
	
	/**
	 * Print list of items in inventory
	 */
	public void printInventory() {
		for (String key : this.itemList.keySet()) {
			Item currentItem = this.itemList.get(key);
			System.out.println("You have a " + currentItem.getName());
		}
	}
	
	/**
	 * Check for specific type of Items
	 * @param str Type name
	 * @return Such Item exists in Inventory
	 */
	public boolean hasItemOfType(String str) {
		try {
			for (Entry<String, Item> entry : this.itemList.entrySet()) {
				Item item = entry.getValue();
				if (Class.forName(str).isInstance(item)) {
					return true;
				}
			}
		} catch(ClassNotFoundException e) {
			// No reaction
		}
		
		return false;
	}
	
	/**
	 * Get all items of specific type
	 * @param str Type's name
	 * @return ArrayList of items of the given type
	 */
	public ArrayList<Item> getItemsOfType(String str) {
		ArrayList<Item> items = new ArrayList<Item>();
		
		try {
			for (Entry<String, Item> entry : this.itemList.entrySet()) {
				Item item = entry.getValue();
				if (Class.forName(str).isInstance(item)) {
					items.add(item);
				}
			}
		} catch(ClassNotFoundException e) {
			// No reaction
		}
		
		return items;
	}
}
