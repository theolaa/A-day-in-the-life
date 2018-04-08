//
// Theo Laanstra 2018, 300153944
//

package game;

import java.util.ArrayList;

public class Location {

	private String name, description, id;

	private ArrayList<String> connections;

	private ArrayList<Item> items;

	private ArrayList<Trigger> triggers;

	public Location(String tempName, String tempDescription, String tempID, ArrayList<String> tempConnections,
			ArrayList<Item> tempItems, ArrayList<Trigger> tempTriggers) {

		if (isEmptyString(tempName)) {

			name = "No Name Assigned";

		} else {

			name = tempName.toUpperCase();

		}

		if (isEmptyString(tempDescription)) {

			description = "No description Assigned";

		} else {

			description = tempDescription.substring(0, tempDescription.length() - 1);

		}

		if (isEmptyString(tempID)) {

			id = "No ID Assigned";

		} else {

			id = tempID;

		}

		connections = tempConnections;

		items = tempItems;

		triggers = tempTriggers;

	}

	ArrayList<Item> getItems() {

		return items;

	}

	void addItem(Item temp) {

		items.add(temp);

	}

	void removeItem(Item temp) {

		items.remove(temp);

	}

	String getLocationId() {

		return id;

	}

	String getName() {

		return name.toUpperCase();

	}

	String getDescription() {

		return description;

	}

	void describe() {

		System.out.println(description);

		System.out.println("\nItems: \n");

		if (items.isEmpty()) {

			System.out.println("\tNone");

		} else {

			for (Item temp : items) {

				System.out.println(temp.describe());

			}

		}

	}

	void outputFullDescription() {

		System.out.println();
		System.out.println(name + " (" + id + ")");
		System.out.println();
		System.out.println(description);

		System.out.println("Items: \n");

		if (items.isEmpty()) {

			System.out.println("\tNone");

		} else {

			for (Item temp : items) {

				System.out.println("\t" + temp.name() + ": " + temp.describe() + "\n" + temp.use());

			}

		}

		System.out.println("\nConnects to these IDs: \n");

		if (connections.isEmpty()) {

			System.out.println("\tNone");

		} else {

			for (String temp : connections) {

				System.out.print("\t" + temp + "\n");

			}

		}

		System.out.println();

	}

	private boolean isEmptyString(String input) {

		try {

			if (input.trim().equals(""))
				return true;
			else
				return false;

		} catch (NullPointerException e) {

			return true;

		}

	}

	boolean connectsWith(String id) {

		for (String temp : connections) {

			if (temp.equals(id.trim()))
				return true;

		}

		return false;

	}

	boolean isTriggered(Inventory items) {

		for (Trigger temp : triggers) {

			if (temp.triggered(items)) {

				if (!temp.triggerItem.equals("NONE")) {

					System.out.println(temp.actionText);
					return true;

				}

			}

		}

		return false;

	}

	ArrayList<String> getConnections() {

		return connections;

	}

	ArrayList<Trigger> getTriggers() {

		return triggers;

	}

	String locationIdAtDirection(String direction) {

		if (direction == null)
			return null;

		final String dirs[] = { "NORTH", "FORWARDS", "SOUTH", "BACKWARDS", "EAST", "RIGHT", "WEST", "LEFT" };

		String returnID;

		int directionInt = 0;

		if (dirs[0].startsWith(direction) || dirs[1].startsWith(direction))
			directionInt = 0;

		else if (dirs[2].startsWith(direction) || dirs[3].startsWith(direction))
			directionInt = 2;

		else if (dirs[4].startsWith(direction) || dirs[5].startsWith(direction))
			directionInt = 1;

		else if (dirs[6].startsWith(direction) || dirs[7].startsWith(direction))
			directionInt = 3;

		else
			directionInt = -1;

		if (directionInt == -1)
			return null;

		returnID = connections.get(directionInt);

		return returnID;

	}

}
