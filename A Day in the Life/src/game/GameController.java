//
// Theo Laanstra 2018, 300153944
//

package game;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class GameController {

	// A list of the available commands
	private enum COM {
		HELP, SAVE, LOAD, NEW, SCORE, QUIT, EXIT, MOVE, GO, BACK, USE, LOOK, TAKE, GRAB, GET, DROP, INVENTORY, ITEMS, YES, TELEPORT
	}

	// Some game-state variables to control the flow of the game, and limit access
	// to different commands. inGame also simplifies LOAD and NEW when not in a
	// game.
	private boolean programRunning = true;
	private boolean inGame = false;
	private boolean advanceTurn;
	private boolean gameFinished = false;
	private boolean gameWon = false;

	// Scanner for input
	private Scanner s = new Scanner(System.in);

	// GameLoader and GameSaver. They do exactly what you'd expect.
	private GameLoader gl = new GameLoader();
	private GameSaver gs;

	// The master list of locations
	private ArrayList<Location> locations;

	// The Player's location history
	private ArrayList<String> locationHistory;

	// The actual player
	private Player player;

	void begin() {

		// Welcome message
		System.out.println("\nWelcome to 'A Day in the Life...'");
		System.out.println("Your objective is to sucessfully navigate the hurdles and ");
		System.out.println("challenges of everyday life on your way to work.");
		System.out.println("Keep your head on straight, remember the basics, and you'll be fine.");
		System.out.println("\nTo begin, load a new or a previous save.");
		System.out.println("\nAt any time you can type HELP for a list of commands.");
		System.out.println("\nGood Luck!");

		// This is actually the meat and potatoes of the game.
		// This will run over and over until the exit command changes programRunning to
		// false.
		do {

			// Play round has some more specific logic to control when a round, or turn has
			// been completed.
			playRound();

			if (gameFinished) {

				// Some appropriate End of game messages based on whether or not the game was
				// "Won"
				if (gameWon) {

					System.out.println("Congratulations!");

				} else {

					System.out.println("\nGame Over...");
					System.out.println();
					System.out.println("Returning to main menu.");

					gameFinished = false;
					gameWon = false;

				}

			}

		} while (programRunning);

		// Makes the game exit on a key-press after the game is over.
		@SuppressWarnings("unused")
		String temp = s.nextLine();

	}

	// This takes the data that the GameLoader has read from the file, and applies
	// it to the appropriate variables in the controller. It then indicates that
	// there is now an active game which allows access to more commands, as well as
	// indicating that a turn has been completed.
	private void applyLoad() {

		locations = gl.loadedLocations();
		locationHistory = gl.loadedLocationHistory();
		player = gl.loadedPlayer();

		inGame = true;
		gameFinished = false;
		gameWon = false;
		advanceTurn = true;

	}

	// This is repeatedly gets commands and displays appropriate output. The current
	// situation is never described until the program knows that a game is in
	// session and it is the beginning of a new turn. It also repeatedly gets
	// commands until one of them triggers a turn
	// advancement. Only a move/go command, or a back command can trigger a new
	// turn. This prevents describeSituation() being called after commands that
	// don't significantly alter the situation.
	private void playRound() {

		// Resets advanceTurn as a new turn has begun and it should not advance to a new
		// one until a command has indicated that that should happen.
		advanceTurn = false;

		String input;

		// Only describes the situation if a game is in progress.
		if (inGame) {

			System.out.println("\n=====================================\n");

			describeSituation();

		}

		// This is what gets commands over and over until it is indicated that a new
		// turn should begin.

		s = new Scanner(System.in);

		while (!advanceTurn) {

			System.out.print("\nCommand: ");

			try {
				input = s.nextLine();
			} catch (NoSuchElementException e) {

				System.out.println("Use the EXIT command to exit the game.");
				return;

			}

			System.out.println();

			// This takes the input and decides what command was entered (if any) and deals
			// with it appropriately.
			parseCommand(input);

		}

	}

	// The message that appears at the beginning of every turn. It shows the
	// description of the player's current location.
	private void describeSituation() {

		System.out.println(player.location().getDescription());

	}

	// Interprets commands. Actions do not have to be completely entered, but
	// objects (except for directions) do. Eg. INVENTORY == in | REMOTE != rem
	private void parseCommand(String input) {

		// Catches if no command was entered, and informs the user where to find a list
		// of commands. Then it gets out of parseCommand() and back to playRound() where
		// another command will be retrieved.
		if (input == null || input.trim().equals("")) {

			System.out.println("No command entered. Type HELP for a list of commands.");
			return;

		}

		// Splits the command up into two parts, delineated by the first whitespace
		// found.
		String[] command = input.toUpperCase().trim().split(" ", 2);

		// In this command format, the action to be carried out is entered first, so it
		// will be the first word in a command.
		String action = command[0];

		// Object is the modifier to the action. Some commands require an object.
		String object = null;

		// To avoid exceptions, only assign object to the second set of words if more
		// than one word was entered as a command.
		if (command.length > 1) {
			object = command[1];
		}

		// QUIT or EXIT command
		if (COM.EXIT.name().startsWith(action) || COM.QUIT.name().startsWith(action)) {

			// Gives the user a chance to back out of terminating the program.
			if (inGame)
				System.out.println("You will lose any unsaved progress. Proceed? YES or NO");
			else
				System.out.println("Are you sure? YES or NO");

			// Grabs input
			input = s.nextLine().toUpperCase();

			// If the user really wants to quit, programRunning is changed to false and a
			// turn is advanced so that the controlling loops up above are exited and the
			// program terminates.
			if (!(input == null || input.trim().isEmpty()) && COM.YES.name().startsWith(input)) {

				// Sets the control variables in such a way as to guide the program to
				// termination
				advanceTurn = true;
				programRunning = false;
				return;

			}

		}

		// Displays list of commands
		else if (COM.HELP.name().startsWith(action))
			showHelp();

		// Loads a game from a previous save file. There is only one save file at the
		// moment.
		else if (COM.LOAD.name().startsWith(action)) {

			// If there is an ongoing game, the user is prompted for confirmation, otherwise
			// the loading begins immediately.
			if (inGame) {

				System.out.println("You will lose any unsaved progress. Proceed? YES or NO");

				input = s.nextLine().toUpperCase();

				if (COM.YES.name().startsWith(input)) {

					// Resets the GameLoader object to a new GameLoader.
					gl = null;
					gl = new GameLoader();

					// gl.beginLoad() returns true if the loading process is successful, and false
					// if there was a problem. Error messages are handled within that method.
					// The loaded data is only applied to this GameController if the loading process
					// is successful.
					if (gl.beginLoad("savedGame.xml")) {

						applyLoad();

						return;

					}

				}

			} else {

				// Same process as above minus the confirmation
				if (gl.beginLoad("savedGame.xml")) {

					applyLoad();

					return;

				}

			}

			// Inform the player that the load was unsuccessful.
			System.out.println("Load cancelled");

		}

		// This behaves identically to the method above, but it loads from the default
		// startup.xml file (a new game).
		else if (COM.NEW.name().startsWith(action)) {

			if (inGame) {

				System.out.println("You will lose any unsaved progress. Proceed? YES or NO");

				input = s.nextLine().toUpperCase();

				if (COM.YES.name().startsWith(input)) {

					gl = null;
					gl = new GameLoader();

					if (gl.beginLoad("startup.xml")) {

						applyLoad();

						return;

					}

				}

			} else {

				if (gl.beginLoad("startup.xml")) {

					applyLoad();

					return;

				}

			}

			System.out.println("Load cancelled");

		}

		// The commands handled above are available at any time, while the following
		// commands are only accessible while playing a game.
		else if (inGame) {

			// Saves current game to "savedGame.xml"
			if (COM.SAVE.name().startsWith(action)) {

				gs = new GameSaver();

				gs.save(locations, locationHistory, player);

			}

			// Informs the player about the games scoring, or lack thereof
			else if (COM.SCORE.name().startsWith(action)) {

				System.out.println("This game has no score.");
				System.out.println("The point is to make it through the day");
				System.out.println("without making a fool of yourself.");
				System.out.println();
				System.out.println("Oh an maybe you'll discover some secrets along the way.");
				System.out.println();
				System.out.println("Just maybe :)");

			}

			// Attempts to move the player in the indicated direction.
			else if (COM.MOVE.name().startsWith(action) || COM.GO.name().startsWith(action))
				move(object);

			// Moves the player to their previous location (if possible)
			else if (COM.BACK.name().startsWith(action))
				moveBack();

			// Attempts to use the indicated object.
			else if (COM.USE.name().startsWith(action))
				use(object);

			// Describes the players surroundings
			else if (COM.LOOK.name().startsWith(action))
				look();

			// Attempts to take the indicated object
			else if (COM.TAKE.name().startsWith(action) || COM.GRAB.name().startsWith(action)
					|| COM.GET.name().startsWith(action))
				take(object);

			// Attempts to drop the indicated object
			else if (COM.DROP.name().startsWith(action))
				drop(object);

			// Displays the player's currently carried items.
			else if (COM.INVENTORY.name().startsWith(action) || COM.ITEMS.name().startsWith(action))
				player.describeInventory();

			// Teleports the player to a location id (Hidden command for development use or
			// for cheating if you find it by accident)
			else if (COM.TELEPORT.name().startsWith(action)) {

				Location temp = getLocationById(object);

				if (temp != null) {

					System.out.println("Teleporting to " + object);
					player.setLocation(temp);
					advanceTurn = true;

				}

			} else
				System.out.println("Command Unknown");

		}

		else
			System.out.println("Command Unknown");

	}

	// Moves the player. It also tests if a game-over has been triggered and if so,
	// ends the game appropriately.
	private void move(String direction) {

		// Gets location id in the indicated direction at current location
		String tempID = player.location().locationIdAtDirection(direction);

		// "none" is returned as the id when there is no location in this direction.
		// The game indicates this and returns to the regular command cycle.
		if (tempID == null || tempID.equals("none")) {

			System.out.println("No location in indicated direction, or invalid direction.\n"
					+ "Type HELP to see a list of valid directions.");
			return;

		}

		// Temporary location based on the location ID found previously
		Location tempLoc = getLocationById(tempID);

		// If no such location was found, display an error.
		if (tempLoc == null) {

			System.out.println("Invalid Location");
			return;

		}

		if (tempLoc.isTriggered(player.inventory())) {

			return;

		} else {

			// Gives some Command feedback
			System.out.println("Player moved " + direction);
			
			// Adds the location ID of the current location to the top of locationHistory.
			locationHistory.add(player.location().getLocationId());

			// Then sets the players location to the desired location.
			player.setLocation(tempLoc);

			advanceTurn = true;

		}

	}

	// Moves the player
	private void moveBack() {

		int lastIndex = locationHistory.size() - 1;

		if (locationHistory.isEmpty()) {

			System.out.println("You have no location history");
			return;

		}

		Location tempLoc = getLocationById(locationHistory.get(lastIndex));

		if (tempLoc == null) {

			System.out.println("Invalid Location");
			return;

		}

		if (!player.location().connectsWith(tempLoc.getLocationId())) {

			System.out.println("You cannon return to this location.");
			return;

		}

		System.out.println("Returned to previous location");

		player.setLocation(tempLoc);

		locationHistory.remove(lastIndex);

		advanceTurn = true;

		if (player.location().isTriggered(player.inventory())) {
			inGame = false;
			gameFinished = true;

		}

	}

	private void use(String object) {

		if (object == null) {

			System.out.println("Proper usage is \"use [ITEM NAME]\"");
			return;

		}

		for (Item temp : player.getItems()) {

			if (temp.name().toUpperCase().equals(object.toUpperCase())) {

				System.out.println(temp.use());
				break;

			}

		}

		// System.out.println(object + " used");

	}

	private void look() {

		player.location().describe();

	}

	private void take(String object) {

		if (object == null) {

			System.out.println("Proper usage is \"take [ITEM NAME]\"");
			return;

		}

		for (int i = 0; i < player.location().getItems().size(); i++) {

			if (player.location().getItems().get(i).name().toUpperCase().equals(object.toUpperCase())) {

				if (player.addItem(player.location().getItems().get(i))) {

					player.location().removeItem(player.location().getItems().get(i));
					System.out.println(object + " taken");
					return;

				}

				System.out.println("Out of inventory space.\nDrop some items to make room.");
				return;
			}

		}

		System.out.println("Object " + object + " not found in current location");

	}

	private void drop(String object) {

		Item item = null;

		if (object == null) {

			System.out.println("Proper usage is \"drop [ITEM NAME]\"");
			return;

		}

		for (Item temp : player.inventory().items) {

			if (temp.name().toUpperCase().equals(object)) {

				item = temp;

			}

		}

		if (item != null) {

			System.out.println(object + " dropped");

			player.removeItem(item);

			if (player.location().getLocationId().equals("officeLobby"))
				getLocationById("officeSecurity").addItem(item);
			else
				player.location().addItem(item);

		} else {

			System.out.println("Item " + object + " not found");

		}

	}

	private Location getLocationById(String id) {

		for (Location temp : locations) {

			if (temp.getLocationId().equalsIgnoreCase(id)) {

				return temp;

			}

		}

		System.out.println("Location with id '" + id + "' not found");

		return null;

	}

	private void showHelp() {

		String helpText = " HELP --------------------- Displays this menu\n"
				+ " LOAD --------------------- Loads the game from a file\n"
				+ " NEW ---------------------- Loads a new game.\n"
				+ " QUIT/EXIT ---------------- Quits the game and returns to the main menu. Remember to save first!\n"
				+ "\n The folowing commands are only available in-game\n\n"
				+ " SAVE --------------------- Saves the game to a file\n"
				+ " MOVE/GO [direction] ------ Attempt to move in the indicated direction\n"
				+ " BACK --------------------- Return to previous location (if possible)\n"
				+ " USE [item] --------------- Attempts to use the specified object\n"
				+ " LOOK --------------------- Describes the area you are in. Use this to find items\n"
				+ " TAKE/GRAB [item] --------- Attempts to take specified item from area\n"
				+ " DROP [item] -------------- Attempts to drop specified item into area\n"
				+ " INVENTORY/ITEMS ---------- Shows the items you are currently carrying\n"
				+ " SCORE -------------------- Displays your score\n"
				+ "\n Possible directions are 'FORWARD', 'BACKWARD', 'LEFT', 'RIGHT' or 'NORTH', 'SOUTH', 'EAST', 'WEST'\n"
				+ " All directions are relative (based on the direction you are currently facing)";

		System.out.println(helpText);

		System.out.println("\nPress Enter to continue");
		@SuppressWarnings("unused")
		String temp = s.nextLine();
		temp = null;

	}

}
