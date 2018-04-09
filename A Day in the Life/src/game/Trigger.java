package game;

public class Trigger {

	// The item that must me possessed or NOT possessed for the trigger to activate.
	String triggerItem;

	// The text to be displayed if the trigger is activated
	String actionText;

	// The boolean to switch between Must have item and Must not have item mode.
	boolean mustHave;

	// Must be instantiated with all fields.
	public Trigger(String itemName, String text, boolean required) {

		triggerItem = itemName;
		actionText = text;
		mustHave = required;

	}

	// Returns true if the trigger condition is met.
	boolean triggered(Inventory inv) {

		// The condition is me when the player does not possess the trigger item
		if (mustHave) {

			for (Item temp : inv.items) {

				if (temp.name().toUpperCase().equals(triggerItem)) {

					return false;

				}

			}

			return true;

		} else {

			// The condition is me when the player possesses the trigger item
			for (Item temp : inv.items) {

				if (temp.name().toUpperCase().equals(triggerItem)) {

					return true;

				}

			}

			return false;

		}

	}

}