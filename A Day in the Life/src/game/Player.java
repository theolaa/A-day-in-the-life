//
// Theo Laanstra 2018, 300153944
//

package game;

import java.util.ArrayList;

public class Player {

	// Declares necessary variables
	private Inventory inventory;

	private Location location;

	// Player can be instantiated with just a location.
	public Player(Location l) {

		location = l;

		inventory = new Inventory();

	}

	// Or with a location and an inventory
	public Player(Location l, Inventory i) {

		location = l;

		inventory = i;

	}

	// Outputs which location the player is at, and which items they have.
	void describeSelf() {

		System.out.println("You are at this location: " + this.location.getName());
		System.out.println();
		System.out.println("You have these items: ");
		inventory.describeAll();

	}

	// Returns the player's current location
	Location location() {

		return this.location;

	}

	// Adds an item to the inventory. Returns false if there isn't enough room.
	boolean addItem(Item item) {

		if (inventory.addItem(item))
			return true;

		return false;

	}

	// Returns the items in the inventory.
	ArrayList<Item> getItems() {

		return inventory.items;

	}

	// Removes an item from the inventory.
	void removeItem(Item item) {

		inventory.removeItem(item);

	}

	// Sets the player's location to the one indicated.
	public void setLocation(Location l) {

		this.location = l;

	}

	// Describes the contents of the inventory.
	public void describeInventory() {

		inventory.describeAll();

	}

	// Returns the inventory itself.
	Inventory inventory() {

		return inventory;

	}

}
