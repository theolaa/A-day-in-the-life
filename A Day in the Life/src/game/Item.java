//
// Theo Laanstra 2018, 300153944
//

package game;

public class Item {

	private String name = "Default Name", description = "Default Description", useText = "Default Usage";

	private int mass = 10, type = 0, value;

	public Item(String n, String d, String u, int m, int t, int v) {

		name = n;

		description = d;

		useText = u;

		mass = m;

		type = t;

		value = v;

		//outAll();

	}

	String name() {

		return name.toUpperCase();

	}

	String describe() {

		return description;

	}

	String use() {

		return useText;

	}

	String getType() {

		if (type == 0) return "GENERIC";
		if (type == 1) return "KEY";
		if (type == 2) return "INTERACTABLE";
		else return "GENERIC";


	}

	int getMass() {

		return mass;

	}

	int getValue() {

		return value;

	}

	void outAll() {

		System.out.println("Name: " + name);
		System.out.println("Description: " + description);
		System.out.println("Use Text: " + useText);
		System.out.println("Mass: " + mass);
		System.out.println("Type: " + type);
		System.out.println("Value: " + value);

	}

}