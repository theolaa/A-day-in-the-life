package game;

public class Trigger {

	String triggerItem;
	String actionText;
	boolean mustHave;

	public Trigger(String itemName, String text, boolean required) {

		triggerItem = itemName;
		actionText = text;
		mustHave = required;

	}

	// Returns true if the trigger condition is met. True indicates that the game
	// has been lost.

	boolean triggered(Inventory inv) {

		if (mustHave) {

			for (Item temp : inv.items) {

				if (temp.name().toUpperCase().equals(triggerItem)) {

					return false;

				}

			}

			return true;

		} else {

			for (Item temp : inv.items) {

				if (temp.name().toUpperCase().equals(triggerItem)) {

					return true;

				}

			}

			return false;

		}

	}

}