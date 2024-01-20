package dev.rdh.minesweeper;

public class Game {
	private final Console console;

	private final Board board;
	private Config config;

	private boolean running = true;

	public Game(Console console, Difficulty difficulty) {
		this.console = console;
		this.board = new Board(difficulty);
		this.config = Config.load();
	}

	public void run() {
		console.clearScreen();
		while(running) {
			console.moveCursor(0, 0);
			console.print(board);

			if(board.isGameOver()) {
				showGameOverScreen();
				console.readChar();
				return;
			}

			char c = console.readChar();
			if(c == 'q') {
				running = false;
			} else if(c == config.reset) {
				board.regenerate();
			} else if(c == config.flag) {
				board.flag();
			} else if(c == config.reveal) {
				board.reveal();
			} else if("wasd".indexOf(c) >= 0 && config.useWASD) {
				board.handleMovement(c);
			} else if(c == config.chord) {
				board.chord();
			} else if(c == '\033') {
				if(console.readChar() == '[') {
					board.handleMovement(console.readChar());
				}
			}
		}
	}

	private void showGameOverScreen() {
		board.revealAll();
		console.moveCursor(0, 0);
		console.print(board);
		console.println("Game over!");
		if(board.isWon()) {
			console.println("You win!");
		} else if(board.isLost()) {
			console.println("You lost!");
		} else {
			throw new IllegalStateException("Game is neither lost or won, this should never happen!");
		}
	}
}
