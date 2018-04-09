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

	int i = 0;

	public GameSaver() {

		try {
			file = new PrintWriter(fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	void save(ArrayList<Location> locations, ArrayList<String> locationHistory, Player player) {

		file.println("<!DOCTYPE GWML>");

		tabinate(++i);

		file.println("<game>");

		tabinate(++i);

		file.println("<locations>");

		for (Location temp : locations)
			saveLocation(temp);

		tabinate(i);
		file.println("</locations>");

		tabinate(i);
		file.println("<player>");

		savePlayer(player);

		tabinate(i);
		file.println("</player>");

		tabinate(i);
		file.println("<history>");

		i++;

		for (String temp : locationHistory) {

			tabinate(i);
			file.println(temp);

		}

		tabinate(--i);
		file.println("</history>");

		file.println("</game>");

		System.out.println("Game Saved to " + fileName);

		file.close();

	}

	void saveLocation(Location location) {

		tabinate(++i);
		file.println("<location>");

		tabinate(i);
		file.println("<name>");

		tabinate(++i);
		file.println(location.getName());

		tabinate(--i);
		file.println("</name>");

		tabinate(i);
		file.println("<description>");

		tabinate(++i);
		file.println(location.getDescription());

		tabinate(--i);
		file.println("</description>");

		tabinate(i);
		file.println("<id>");

		tabinate(++i);
		file.println(location.getLocationId());

		tabinate(--i);
		file.println("</id>");

		tabinate(i);
		file.println("<items>");

		for (Item temp : location.getItems())
			saveItem(temp);

		tabinate(--i);
		file.println("</items>");

		tabinate(i);
		file.println("<connects>");

		i++;

		for (String temp : location.getConnections()) {

			tabinate(i);
			file.println(temp);

		}

		tabinate(--i);
		file.println("</connects>");

		tabinate(i);
		file.println("<triggers>");

		for (Trigger temp : location.getTriggers())
			saveTrigger(temp);

		tabinate(--i);
		file.println("</triggers>");

		tabinate(--i);
		file.println("</location>");

	}

	void savePlayer(Player player) {

		tabinate(++i);
		file.println("<location>");

		tabinate(++i);
		file.println(player.location().getLocationId());

		tabinate(--i);
		file.println("</location>");

		tabinate(i);
		file.println("<items>");

		for (Item temp : player.inventory().items)
			saveItem(temp);

		tabinate(--i);
		file.println("</items>");

	}

	void saveItem(Item item) {

		int type = 0;

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

	void tabinate(int n) {

		for (int i = 0; i < n; i++)

			file.print("\t");

	}

}
