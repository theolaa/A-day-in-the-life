//
// Theo Laanstra 2018, 300153944
//

package game;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

//A class for loading game data from a file into the program.
public class GameLoader {

	// I threw some Thread.sleep() statements in here to make it look a little
	// cooler when the different status messages pop up one by one. Changing
	// this
	// int adjusts the interval to whatever is desired in milliseconds.
	static final int interval = 150;

	// I found it was easier to load all the lines of the save file into an
	// array
	// list and then manipulate them how I wanted.
	// This is the array list I load the ENTIRE save file into.
	private ArrayList<String> lines = new ArrayList<>();

	// An array list of the loaded locations.
	private ArrayList<Location> locations;

	// A Player object to hold the loaded player.
	private Player player;

	// An ArrayList of the location IDs of the player's previously visited
	// locations.
	private ArrayList<String> locationHistory;

	// The process the begins loads everything up into the "lines" ArrayList.
	boolean beginLoad(String fileName) {

		lines = new ArrayList<String>();

		// Creates a file object with the defined filename
		File file = new File(fileName);

		// Reserves space for a loader object.
		Scanner loader;

		// Try's initializing the loader with the file. If it's successful, it
		// loads
		// every line of text in the file and strips away
		// any preceding or trailing whitespace, ie. the formatting of the XML
		// file.
		try {

			loader = new Scanner(file);

			while (loader.hasNext()) {

				lines.add(loader.nextLine().trim());

			}

			loader.close();
			file = null;

			// for (String temp : lines) System.out.println(temp);

			// If the loading is unsuccessful, an error message is displayed and
			// the game
			// returns to the main menu.
		} catch (Exception e) {

			System.out.println("\nFile \"" + fileName + "\" Not Found");

			return false;

		}

		// Even if the file is loaded successfully, that doesn't mean it's the
		// file type
		// that holds correct game data.
		// If it is the correct file type, it will have the <!DOCTYPE GWML>
		// doctype
		// declaration.
		// This checks for that.

		if (lines.isEmpty()) {

			System.out.println("File " + fileName + " is empty");

			return false;

		}

		if (lines.get(0).equals("<!DOCTYPE GWML>")) {

			System.out.println("File OK!");

			lines.remove(0);
			lines.remove(0);
			lines.remove(lines.size() - 1);

			// Beginning Save Processing
			// System.out.println("Starting Save Processing...");
			processGameData();

			// If it is the correct file type, then the doctype declaration and
			// some
			// unnecessary structural tags are removed.
			// Finally the processing of the game data is initiated.
		} else if (!lines.get(0).equals("<!DOCTYPE GWML>")) {

			System.out.println("\nInvalid File: Improper DOCTYPE declaration");
			return false;

		} else {

			System.out.println("\nSomething went wrong :-(");
			return false;

		}

		return true;

	}

	// This does the actual data crunching to extract the various locations and
	// items stored in the file, as well as the player character.
	private void processGameData() {

		// System.out.println("Save Processing Started...");

		// ArrayLists of lines of data to be passed to more specific processing
		// methods.
		ArrayList<String> LText = new ArrayList<String>();
		ArrayList<String> playerText = new ArrayList<String>();
		locationHistory = new ArrayList<String>();

		// Marks the separation point between locations and the player as
		// determined by
		// the end of the locations section.
		int delineator = 0;

		// Adds all locations to the location text array list
		for (int i = 1; !lines.get(i).equals("</locations>"); i++) {

			delineator = i;

			LText.add(lines.get(i));

		}

		try {
			Thread.sleep(interval);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}

		System.out.println("Location data gathered...");

		// Adds all player data to the player text array list
		for (int i = delineator + 3; !lines.get(i).equals("</player>"); i++) {

			delineator = i;

			playerText.add(lines.get(i));

		}

		try {
			Thread.sleep(interval);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}

		System.out.println("Player data gathered...");

		// Gets the location IDs within the <history> tag and saves them to
		// locationHistory.
		for (int i = delineator + 1; i < lines.size(); i++) {

			if (lines.get(i).equals("<history>")) {

				while (!lines.get(++i).equals("</history>"))
					locationHistory.add(lines.get(i));

			}

		}

		try {
			Thread.sleep(interval);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}

		System.out.println("Location History data gathered...");

		// Since I'm done with it, this can go in the trash.
		lines = null;

		// Sends off the lines of location data to be processed into Location
		// objects,
		// then assigns them to the ArrayList of Locations. Finally it indicates
		// when
		// the process is done.
		locations = processLocations(LText);

		try {
			Thread.sleep(interval);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}

		System.out.println("Locations processed...");
		LText = null;

		// Sends off the player to be processed into a Player object, and
		// indicates when
		// the process is done.
		// System.out.println("Starting Player Processing...");
		player = processPlayer(playerText);

		try {
			Thread.sleep(interval);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}

		System.out.println("Player processed...");
		playerText = null;

		try {
			Thread.sleep(interval);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}

		System.out.println("Location History processed...");

	}

	private ArrayList<Location> processLocations(ArrayList<String> input) {

		// System.out.println("Starting Location Processing...");

		// Creates an ArrayList of Locations to return to the GameController,
		// as well as an ArrayList to hold the text data for each location to be
		// passed
		// to processLocation();
		ArrayList<Location> finalLocations = new ArrayList<Location>();
		ArrayList<String> locationText = new ArrayList<String>();

		// This marks where each location ends (and implies where the next
		// begins) for
		// purposes of splitting up the block of location data and passing
		// single
		// locations to processLocation()
		int delin = 0;

		// Here iterate through the block of location data in text form and pass
		// off
		// single locations to be processed.
		for (int i = delin; i < input.size(); i++) {

			delin = i;

			if (input.get(i) != null && input.get(i).equals("</location>")) {

				// System.out.println("Found end of a location in file");
				// When the end of a location is found, it gets processed into a
				// Location object
				// here
				finalLocations.add(processLocation(locationText));
				locationText.clear();
				// System.out.println("Location Processed...");
				continue;

			}

			// System.out.println("Processing..." + input.get(i));

			locationText.add(input.get(i));

		}

		// The final ArrayList of Locations
		locationText = null;
		return finalLocations;

	}

	// Here is where a block of location data is processed into a Location
	// object.
	private Location processLocation(ArrayList<String> input) {

		// System.out.println("Processing a single location...");

		// Sets some default values so the Location can still be created if
		// there is a
		// minor error.
		String name = "Default Name", description = "Default Description", id = "Default ID";

		ArrayList<String> connections = new ArrayList<String>();

		ArrayList<Item> items = new ArrayList<Item>();

		ArrayList<Trigger> triggers = new ArrayList<Trigger>();

		// Chews through the structure tags and extracts the name, then chews
		// through
		// the trailing structure tags to get to the description.
		input.remove(0);
		input.remove(0);

		name = input.get(0);
		// System.out.println("Location name processed...");

		input.remove(0);
		input.remove(0);
		input.remove(0);

		// Gets the description and removes each line as it is added to the
		// description
		// string.
		while (!input.get(0).equals("</description>")) {

			if (description.equals("Default Description"))
				description = "";

			description += " " + input.get(0) + "\n";
			input.remove(0);

		}

		// System.out.println("Location description processed...");

		// Strips away some more structure tags and extracts the location ID,
		// then
		// strips
		// away some more structure tags.
		input.remove(0);
		input.remove(0);

		id = input.get(0);
		// System.out.println("Location ID processed...");

		input.remove(0);
		input.remove(0);

		// Now I'm left with the Items in a location, and the other locations it
		// can
		// connect to.
		// Since the connections are after the items in the file, and are easier
		// to
		// read, I'll work backwards and add them directly to the connections
		// ArrayList
		// here, leaving nothing but the Items in the "input" ArrayList to be
		// passed to
		// processItems().
		int delin = 0;

		// Finds the beginning of the connects element of a location
		for (int i = 0; i < input.size(); i++) {

			delin = i;

			if (input.get(i).equals("<connects>"))
				break;

		}

		// If <connects> was found, add them to the connections ArrayList
		if (delin != input.size() - 1) {

			input.remove(delin);

			while (!input.get(delin).equals("</connects>")) {

				connections.add(input.get(delin));
				input.remove(delin);

			}

			input.remove(delin);
		}

		// The remaining input is just the items of a location and its triggers, so now
		// it can be passed off for processing.
		items = processItems(input);

		input.remove(0);

		input.remove(input.size() - 1);

		// Gets the triggers of a location
		while (!input.isEmpty()) {

			input.remove(0);

			while (!input.get(0).equals("</trigger>")) {

				String mustHave = input.get(0);
				input.remove(0);

				String item = input.get(0);
				input.remove(0);

				String actionText = "";

				// For getting multiple lines of Action Text
				while (!input.get(0).equals("</trigger>")) {

					actionText += "\n" + input.get(0);
					input.remove(0);

				}

				// Sets the mustHave boolean according to what was enterd in that field in the
				// file.
				boolean mustHaveBool = (mustHave.equals("MUST_HAVE")) ? true : false;

				// Initializes a new trigger item, and adds it to the triggers ArrayList
				Trigger temp = new Trigger(item, actionText, mustHaveBool);

				triggers.add(temp);

			}

			input.remove(0);

		}

		// Location processing is complete, so now we can create a new location with all
		// the loaded fields, and return it.
		return new Location(name, description, id, connections, items, triggers);

	}

	// Here is where a block of player data is processed into a Player object.
	private Player processPlayer(ArrayList<String> input) {

		// Some temporary variables for creating the Player object.
		Location tempLocation;

		String tempLocID = "livingRoom";

		Inventory tempInventory = new Inventory();

		input.remove(0);

		// loads the location id of the player's current location as saved in the file.
		tempLocID = input.get(0);

		tempLocation = this.getLocationById(tempLocID);

		input.remove(0);
		input.remove(0);

		// Adds all the items in the players saved inventory to the temporary inventory.
		tempInventory.batchAdd(processItems(input));

		// Returns a new Player object
		return new Player(tempLocation, tempInventory);

	}

	// Processes an entire <items> element into an ArrayList of Items
	private ArrayList<Item> processItems(ArrayList<String> input) {

		// The final ArrayList of loaded Items
		ArrayList<Item> finalItems = new ArrayList<>();

		// The loaded text of a single item to be processed.
		ArrayList<String> itemText = new ArrayList<>();

		input.remove(0);

		// Gets an <item> element and sends it for processing until there are none left.
		while (!input.isEmpty() && !input.get(0).equals("</items>")) {

			itemText.add(input.get(0));

			if (input.get(0).equals("</item>")) {

				// System.out.println("Found end of an Item...");
				finalItems.add(processItem(itemText));
				itemText.clear();

			}

			input.remove(0);

		}

		input.remove(0);

		// Returns the final ArrayList of Items
		return finalItems;

	}

	// Returns a single item generated from an ArrayList of text
	private Item processItem(ArrayList<String> input) {

		// Sets some default values so an item can be returned even if there is an error
		// of sorts.
		String name = "Default Name", description = "Default Description", useText = "Default Usage Text";

		int mass = 1, type = 0, value = 1000;

		// Removes some structure tags and then gets the name of the item.
		input.remove(0);
		input.remove(input.size() - 1);
		input.remove(0);

		name = input.get(0);

		input.remove(0);
		input.remove(0);
		input.remove(0);

		if (description != null || !description.trim().equals("")) {

			// Makes sure that description is empty so that each newly loaded line can be
			// appended to it.
			description = "";

		}

		// Gets the entire description text
		while (!input.get(0).equals("</description>")) {

			description += " " + input.get(0);

			input.remove(0);

		}

		input.remove(0);
		input.remove(0);

		// Same process as for the description
		if (useText != null || !useText.trim().equals("")) {

			useText = "";

		}

		while (!input.get(0).equals("</use>")) {

			useText += input.get(0) + "\n\t";

			input.remove(0);

		}

		// Removes the \n\t from the last line so formatting in the program isn't weird.
		useText = useText.substring(0, useText.length() - 2);

		// Removes structure tags and gets Mass, Type, and Value, in that order.
		input.remove(0);
		input.remove(0);

		mass = Integer.parseInt(input.get(0));

		input.remove(0);
		input.remove(0);
		input.remove(0);

		type = Integer.parseInt(input.get(0));

		input.remove(0);
		input.remove(0);
		input.remove(0);

		value = Integer.parseInt(input.get(0));

		// Empties and un-sets the input for memory's sake.
		input.clear();
		input = null;

		// Returns a newly processed Item!
		return new Item(name, description, useText, mass, type, value);

	}

	// For returning the loaded locations back to the GameController.
	ArrayList<Location> loadedLocations() {

		if (locations == null)
			locations = new ArrayList<Location>();

		return locations;

	}

	// Returns the ID strings to the GameController where it can use the
	// getLocationById() method to get the actual previous locations.
	ArrayList<String> loadedLocationHistory() {

		if (locationHistory.isEmpty()) {

			locationHistory = null;

			return new ArrayList<String>();

		}

		return locationHistory;

	}

	// For returning the loaded player back to the GameController.
	Player loadedPlayer() {

		return player;

	}

	// Just a utility function for my own use within this class. Returns an item
	// with a matching Item ID.
	private Location getLocationById(String locationID) {

		if (locations == null || locations.isEmpty()) {

			System.out.println("\nNo locations to search through...");
			return null;

		}

		for (int i = 0; i < locations.size(); i++) {

			if (locations.get(i) == null)
				continue;

			if (locations.get(i).getLocationId().equals(locationID))

				return locations.get(i);

		}

		System.out.println("Location ID doesn't match any known location.");

		return null;

	}

}