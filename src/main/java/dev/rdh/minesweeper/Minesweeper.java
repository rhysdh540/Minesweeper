package dev.rdh.minesweeper;

public class Minesweeper {
	public static void main(String[] args) {
		try(Console console = new Console()) {
			console.echo(false);
			console.clearScreen();
			console.hideCursor();

			SelectionModal goAgain = SelectionModal.of("Do you want to go again?", "Yes", "No");

			final Difficulty[] diffs = Difficulty.values();
			SelectionModal diffSelection = SelectionModal.of("Choose a difficulty:", diffs);
			do {
				Difficulty diff = diffs[diffSelection.displayOn(console)];
				new Game(console, diff).run();

			} while(goAgain.displayOn(console) != 1);
		} catch(Exception e) {
			e.printStackTrace(System.err);
		}
	}
}
