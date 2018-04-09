//
// Theo Laanstra 2018, 300153944
//

package game;

public class Item {

	// Some default values
	private String name = "Default Name", description = "Default Description", useText = "Default Usage";

	private int mass = 10, type = 0, value;

	public Item(String n, String d, String u, int m, int t, int v) {

		name = n;

		description = d;

		useText = u;

		mass = m;

		type = t;

		value = v;

	}

	// Gets the item's name
	String name() {

		return name.toUpperCase();

	}

	// Gets the item's description
	String describe() {

		return description;

	}

	// Gets the item's useage text
	String use() {

		return useText;

	}

	// Returns a human readable representation of the item type.
	String getType() {

		if (type == 0)
			return "GENERIC";
		if (type == 1)
			return "KEY";
		if (type == 2)
			return "INTERACTABLE";
		else
			return "GENERIC";

	}

	// Gets the mass of the item
	int getMass() {

		return mass;

	}

	// Gets the value of the item
	int getValue() {

		return value;

	}

	// Outputs all information about item. Not used in final game, just for
	// developing.
	void outAll() {

		System.out.println("Name: " + name);
		System.out.println("Description: " + description);
		System.out.println("Use Text: " + useText);
		System.out.println("Mass: " + mass);
		System.out.println("Type: " + type);
		System.out.println("Value: " + value);

	}

}