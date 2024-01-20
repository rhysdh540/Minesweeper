package dev.rdh.minesweeper;

public class Game {
	private final Console console;

	private final Board board;

	private boolean running = true;

	public Game(Console console, Difficulty difficulty) {
		this.console = console;
		this.board = new Board(difficulty);
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
			switch(c) {
				case 'q' -> running = false;
				case 'r' -> board.regenerate();
				case 'f' -> board.flag();
				case ' ' -> board.reveal();
				case 'w', 'a', 's', 'd' -> board.handleMovement(c);
				case 'c' -> board.chord();
				case '\033' -> {
					if(console.readChar() == '[') {
						board.handleMovement(console.readChar());
					}
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
