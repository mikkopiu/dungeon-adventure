package dungeonadventure.items;

import dungeonadventure.properties.Eatable;
import dungeonadventure.properties.Holdable;


/**
 * Food item. Can be eaten.
 * @author Mikko Piuhola
 */
public class Food extends Item implements Eatable,Holdable {

	private static final long serialVersionUID = -8609824388929030524L;

	/**
	 * Create a default food item
	 * Default Food has name Food and description "some food"
	 */
	public Food() {
		this("Food", "some food");
	}
	
	/**
	 * Create a food item with a name and a description
	 * @param name Name for Food
	 * @param description Description of Food
	 */
	public Food(String name, String description) {
		super(name, description);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void eat() {
		System.out.println("You ate " + this.toString().toLowerCase());
	}

}
