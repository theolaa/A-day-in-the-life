//
// Theo Laanstra 2018, 300153944
//

package game;

import java.util.ArrayList;

public class Inventory {

	ArrayList<Item> items = new ArrayList<Item>();

	int maxSize = 8, currentSize = 0;

	void removeItem(Item item) {
		
		items.remove(item);
		currentSize -= item.getMass();

	}

	boolean addItem(Item item) {

		if (item.getMass() + currentSize <= maxSize) {
			
			items.add(item);
			currentSize += item.getMass();
			return true;

		}
		
		return false;

	}

	void batchAdd(ArrayList<Item> newItems) {

		for (Item temp : newItems) {

			items.add(temp);
			currentSize += temp.getMass();

		}

	}

	void describeAll() {

		System.out.println("Space: " + currentSize + " slot(s) of " + maxSize + "\n");

		System.out.println("You have these items: \n");
		
		if (items.isEmpty()) {
			
			System.out.println("No Items");
			return;
			
		}
		
		for (Item temp : items)
			System.out.println("-" + temp.describe());
		// System.out.println("\t" + temp.name() + ": " + temp.describe() + "\n" +
		// temp.use() + temp.);

	}

}
