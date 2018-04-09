//
// Theo Laanstra 2018, 300153944
//

package game;

import java.util.ArrayList;

public class Location {

	// Declares the different variables that the Location Object needs.
	private String name, description, id;

	private ArrayList<String> connections;

	private ArrayList<Item> items;

	private ArrayList<Trigger> triggers;

	// Assigns appropriate values to the different variables, or default values if
	// none are given.
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

	// Returns all the items in the location.
	ArrayList<Item> getItems() {

		return items;

	}

	// Adds an item. No need to check for inventory space here. A location can have
	// unlimited items.
	void addItem(Item temp) {

		items.add(temp);

	}

	// Removes an item from the location.
	void removeItem(Item temp) {

		items.remove(temp);

	}

	// Returns the id of the location
	String getLocationId() {

		return id;

	}

	// Returns the name of the location
	String getName() {

		return name.toUpperCase();

	}

	// Returns the description of the location
	String getDescription() {

		return description;

	}

	// Outputs a nicely formatted description, including any items that the location
	// might contain.
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

	// Outputs every bit of information about a location and its items.
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

	// Utility function to check if a string is empty.
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

	// Returns true if the id passed to the method is found in the locations
	// connections.
	boolean connectsWith(String id) {

		for (String temp : connections) {

			if (temp.equals(id.trim()))
				return true;

		}

		return false;

	}

	// Returns true if one of the location's triggers has been activated, based on
	// what items the player has. It also outputs the trigger text.
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

	// Returns the connections of the location
	ArrayList<String> getConnections() {

		return connections;

	}

	// Returns the triggers of a location.
	ArrayList<Trigger> getTriggers() {

		return triggers;

	}

	// Returns a location id based on which direction was entered by the player.
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

		// Returns the id at the indicated direction
		returnID = connections.get(directionInt);

		return returnID;

	}

}
