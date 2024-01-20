package dev.rdh.minesweeper;

public class Minesweeper {
	public static void main(String[] args) {
		if(System.getProperty("os.name").toLowerCase().contains("wind")) {
			System.err.println("This game is not supported on Windows!");
			return;
		}

		try(Console console = new Console()) {
			console.echo(false);
			console.clearScreen();
			console.hideCursor();

			SelectionModal main = SelectionModal.of("Welcome to Minesweeper!\nChoose an option:", "Play", "Controls", "Exit");

			final Difficulty[] diffs = Difficulty.values();
			SelectionModal diffSelection = SelectionModal.of("Choose a difficulty:", diffs);
			while(true) {
				int mainSelection = main.displayOn(console);
				if(mainSelection == 2) {
					break;
				} else if(mainSelection == 1) {
					configureControls(console);
					continue;
				}
				Difficulty diff = diffs[diffSelection.displayOn(console)];
				new Game(console, diff).run();
			}
		} catch(Exception e) {
			e.printStackTrace(System.err);
		}
	}

	private static void configureControls(Console console) {
		console.clearScreen();

		Config config = Config.load();
		while(true) {
			SelectionModal controls = SelectionModal.of("Choose a control to change:",
					"Use WASD: " + (config.useWASD ? "Yes" : "No"), "Chord: " + key(config.chord), "Flag: " + key(config.flag), "Reveal: " + key(config.reveal), "Reset: " + key(config.reset), "Reset Controls to Defaults", "Back");
			int selection = controls.displayOn(console);
			if(selection == 5) {
				config = new Config();
			} else if(selection == 6) {
				break;
			}

			if(selection == 0) {
				config.useWASD = !config.useWASD;
			} else if(selection == 1) {
				console.print("Enter a new chord key: _");
				config.chord = console.readChar();
			} else if(selection == 2) {
				console.print("Enter a new flag key: _");
				config.flag = console.readChar();
			} else if(selection == 3) {
				console.print("Enter a new reveal key: _");
				config.reveal = console.readChar();
			} else if(selection == 4) {
				console.print("Enter a new reset key: _");
				config.reset = console.readChar();
			}
		}

		config.save();
	}

	private static String key(char c) {
		return c == ' ' ? "Space" : String.valueOf(c);
	}
}
