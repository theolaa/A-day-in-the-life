//
// Theo Laanstra 2018, 300153944
//

package game;

public class Game {

	// Where it all starts
	public static void main(String[] args) {

		// Creates a new GameController. GameController controls every aspect of the
		// game.
		GameController gc = new GameController();

		// Initiates the GameController to begin accepting commands
		gc.begin();

	}

}
