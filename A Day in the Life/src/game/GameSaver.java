//
// Theo Laanstra 2018, 300153944
//

package game;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class GameSaver {

	private String fileName = "savedGame.xml";

	PrintWriter file;

	// An attempt at formatting. I represents the number of tabs to output on each
	// line.
	// Can be seen as the "nesting" layers.
	// Used by the tabinate() function.
	int i = 0;

	// Generates a new PrintWriter object when this object is instantiated.
	public GameSaver() {

		try {
			file = new PrintWriter(fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	// The meat and potatoes of the save function. It has the basic structure tags,
	// and then calls specific functions to do the rest, all while outputting the
	// required number of tabs to properly format the output file.
	void save(ArrayList<Location> locations, ArrayList<String> locationHistory, Player player) {

		file.println("<!DOCTYPE GWML>");

		tabinate(++i);

		file.println("<game>");

		tabinate(++i);

		file.println("<locations>");

		// Saves all the locations to the file.
		for (Location temp : locations)
			saveLocation(temp);

		tabinate(i);
		file.println("</locations>");

		tabinate(i);
		file.println("<player>");

		// Saves the player to the file
		savePlayer(player);

		tabinate(i);
		file.println("</player>");

		tabinate(i);
		file.println("<history>");

		i++;

		// Saves the location history to the file.
		for (String temp : locationHistory) {

			tabinate(i);
			file.println(temp);

		}

		tabinate(--i);
		file.println("</history>");

		file.println("</game>");

		// Outputs that the file was saved successfully.
		System.out.println("Game Saved to " + fileName);

		file.close();

	}

	void saveLocation(Location location) {

		tabinate(++i);
		file.println("<location>");

		tabinate(i);
		file.println("<name>");

		// Saves the location name.
		tabinate(++i);
		file.println(location.getName());

		tabinate(--i);
		file.println("</name>");

		tabinate(i);
		file.println("<description>");

		// Saves the location description
		tabinate(++i);
		file.println(location.getDescription());

		tabinate(--i);
		file.println("</description>");

		tabinate(i);
		file.println("<id>");

		// Saves the location ID
		tabinate(++i);
		file.println(location.getLocationId());

		tabinate(--i);
		file.println("</id>");

		tabinate(i);
		file.println("<items>");

		// Saves the location's items
		for (Item temp : location.getItems())
			saveItem(temp);

		tabinate(--i);
		file.println("</items>");

		tabinate(i);
		file.println("<connects>");

		i++;

		// Saves the location IDs of the locations that at location connects with.
		for (String temp : location.getConnections()) {

			tabinate(i);
			file.println(temp);

		}

		tabinate(--i);
		file.println("</connects>");

		tabinate(i);
		file.println("<triggers>");

		// Saves all of a location's Triggers.
		for (Trigger temp : location.getTriggers())
			saveTrigger(temp);

		tabinate(--i);
		file.println("</triggers>");

		tabinate(--i);
		file.println("</location>");

	}

	// Saves the Player Object.
	void savePlayer(Player player) {

		tabinate(++i);
		file.println("<location>");

		// Saves the ID of the player's current location.
		tabinate(++i);
		file.println(player.location().getLocationId());

		tabinate(--i);
		file.println("</location>");

		tabinate(i);
		file.println("<items>");

		// Saves all of the player's items.
		for (Item temp : player.inventory().items)
			saveItem(temp);

		tabinate(--i);
		file.println("</items>");

	}

	// Saves an individual item. In order, It's name, description, use text, mass,
	// type, and value.
	void saveItem(Item item) {

		int type = 0;

		// Converts the type from its text form to its save-file integer form.
		if (item.getType().equals("GENERIC"))
			type = 0;
		if (item.getType().equals("KEY"))
			type = 1;
		if (item.getType().equals("INTERACTABLE"))
			type = 2;

		tabinate(++i);
		file.println("<item>");

		tabinate(++i);
		file.println("<name>");

		tabinate(++i);
		file.println(item.name());

		tabinate(--i);
		file.println("</name>");

		tabinate(i);
		file.println("<description>");

		tabinate(++i);
		file.println(item.describe());

		tabinate(--i);
		file.println("</description>");

		tabinate(i);
		file.println("<use>");

		tabinate(++i);
		file.println(item.use());

		tabinate(--i);
		file.println("</use>");

		tabinate(i);
		file.println("<mass>");

		tabinate(++i);
		file.println(item.getMass());

		tabinate(--i);
		file.println("</mass>");

		tabinate(i);
		file.println("<type>");

		tabinate(++i);
		file.println(type);

		tabinate(--i);
		file.println("</type>");

		tabinate(i);
		file.println("<value>");

		tabinate(++i);
		file.println(item.getValue());

		tabinate(--i);
		file.println("</value>");

		tabinate(--i);
		file.println("</item>");

	}

	// Saves an individual Trigger.
	void saveTrigger(Trigger trigger) {

		String mustHave = (trigger.mustHave) ? "MUST_HAVE" : "MUST_NOT_HAVE";

		tabinate(++i);
		file.println("<trigger>");

		tabinate(++i);
		file.println(mustHave);

		tabinate(i);
		file.println(trigger.triggerItem);

		tabinate(i);
		file.println(trigger.actionText);

		tabinate(--i);
		file.println("</trigger>");

	}

	// prints i tab characters
	void tabinate(int n) {

		for (int i = 0; i < n; i++)

			file.print("\t");

	}

}
