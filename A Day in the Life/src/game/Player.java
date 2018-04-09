//
// Theo Laanstra 2018, 300153944
//

package game;

import java.util.ArrayList;

public class Player {

	private Inventory inventory;

	private Location location;

	private double wallet = 0;

	public Player(Location l) {

		location = l;

		inventory = new Inventory();

	}

	public Player(Location l, Inventory i) {

		location = l;

		inventory = i;

	}

	void describeSelf() {

		System.out.println("You are at this location: " + this.location.getName());
		System.out.println();
		System.out.println("You have these items: ");
		inventory.describeAll();

	}

	Location location() {

		return this.location;

	}

	boolean addItem(Item item) {

		if (inventory.addItem(item)) return true;

		return false;

	}

	ArrayList<Item> getItems() {

		return inventory.items;

	}

	void removeItem(Item item) {

		inventory.removeItem(item);

	}

	double getWalletBalance() {

		return wallet;

	}

	boolean pay(double amount) {

		if (amount > wallet) {

			System.out.println("Insufficiant funds");
			return false;

		} else {

			wallet -= amount;
			return true;

		}

	}

	void sell(double amount) {

		wallet += amount;

	}

	public void setLocation(Location l) {

		this.location = l;

	}

	public void describeInventory() {

		inventory.describeAll();

	}

	Inventory inventory() {

		return inventory;

	}

}
