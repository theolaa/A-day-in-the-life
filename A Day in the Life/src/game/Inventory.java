//
// Theo Laanstra 2018, 300153944
//

package game;

import java.util.ArrayList;

public class Inventory {

	ArrayList<Item> items = new ArrayList<Item>();

	// Maximum Inventory Slots, Current Slots used.
	// One unit of mass = one inventory slot used.
	int maxSize = 8, currentSize = 0;

	// Removes an item, and subtracts its mass from the current carry weight.
	void removeItem(Item item) {

		items.remove(item);
		currentSize -= item.getMass();

	}

	// Attempts to add an item to the inventory. If there is not enough room for it,
	// do not allow the item to be added.
	boolean addItem(Item item) {

		if (item.getMass() + currentSize <= maxSize) {

			items.add(item);
			currentSize += item.getMass();
			return true;

		}

		return false;

	}

	// Adds multiple Items at once. Only used for adding all items to the inventory
	// during loading.
	void batchAdd(ArrayList<Item> newItems) {

		for (Item temp : newItems) {

			items.add(temp);
			currentSize += temp.getMass();

		}

	}

	// Describes the inventory.
	void describeAll() {

		// Currently used and Max available inventory slots.
		System.out.println("Space: " + currentSize + " slot(s) of " + maxSize + "\n");

		// Outputs the description of all items in inventory.
		System.out.println("You have these items: \n");

		if (items.isEmpty()) {

			System.out.println("No Items");
			return;

		}

		for (Item temp : items)
			System.out.println("-" + temp.describe());

	}

}
