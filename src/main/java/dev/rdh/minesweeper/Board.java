package dev.rdh.minesweeper;

import lombok.Getter;

import java.util.concurrent.ThreadLocalRandom;

public class Board {
	private int x, y;

	@Getter
	private final int width, height;

	private final Cell[][] cells;

	@Getter
	private final Difficulty difficulty;

	@Getter
	private boolean lost, won;

	private int safeSquaresOpened = 0;

	private boolean firstRevealed = false;

	public Board(Difficulty difficulty) {
		this.width = difficulty.getWidth();
		this.height = difficulty.getHeight();
		this.difficulty = difficulty;
		this.cells = new Cell[width][height];

		reset();
		x = width / 2;
		y = height / 2;
	}

	private void reset() {
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				boolean wasFlagged = cells[x][y] != null && cells[x][y].isFlagged();
				cells[x][y] = new Cell(false);
				if(wasFlagged) {
					cells[x][y].flag();
				}
			}
		}
	}

	public void regenerate() {
		reset();
		ThreadLocalRandom random = ThreadLocalRandom.current();
		int mines = difficulty.getNumMines();
		for(int i = 0; i < mines; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			if(cells[x][y].isMine()) {
				i--;
				continue;
			}

			cells[x][y] = new Cell(true);
		}

		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				int adjacentMines = 0;
				for(int x1 = x - 1; x1 <= x + 1; x1++) {
					for(int y1 = y - 1; y1 <= y + 1; y1++) {
						if(x1 < 0 || x1 >= width || y1 < 0 || y1 >= height) {
							continue;
						}

						if(cells[x1][y1].isMine()) {
							adjacentMines++;
						}
					}
				}

				cells[x][y].setAdjacentMines(adjacentMines);
			}
		}
	}

	@Override
	public String toString() {
		if(safeSquaresOpened == width * height - difficulty.getNumMines()) {
			won = true;
		}

		StringBuilder sb = new StringBuilder();
		sb.append("Minesweeper (").append(difficulty).append("):\n");
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				boolean isSelection = this.x == x && this.y == y && !isGameOver();

				sb.append(cells[x][y].toString(isSelection)).append(' ');
			}

			sb.append("\n");
		}

		return sb.toString();
	}

	public void handleMovement(char move) {
		switch(move) {
			case 'A' -> {
				if(y > 0) y--;
			}
			case 'B' -> {
				if(y < height - 1) y++;
			}
			case 'C' -> {
				if(x < width - 1) x++;
			}
			case 'D' -> {
				if(x > 0) x--;
			}
		}
	}

	public boolean isGameOver() {
		return lost || won;
	}

	private boolean shouldFloodReveal() {
		Cell cell = cells[x][y];
		return !cell.isRevealed() && !cell.isMine() && cell.getAdjacentMines() <= 0;
	}

	public void reveal() {
		Cell cell = cells[x][y];
		if(cell.isFlagged()) return;

		if(!firstRevealed) {
			firstRevealed = true;
			do {
				// guarantees that the first time user reveals it will flood
				regenerate();
			} while(!shouldFloodReveal());
			floodReveal(x, y);
			return;
		}

		if(cell.isRevealed()) return;

		if(cell.isMine()) {
			cell.reveal();
			lost = true;
			return;
		}

		if(cell.getAdjacentMines() <= 0) {
			floodReveal(x, y);
		}

		cell.reveal();
		safeSquaresOpened++;
	}

	private void floodReveal(int x, int y) {
		if(x < 0 || x >= width || y < 0 || y >= height) {
			return;
		}

		Cell cell = cells[x][y];

		if(cell.isRevealed() || cell.isFlagged()) {
			return;
		}

		cell.reveal();
		safeSquaresOpened++;

		if(cell.getAdjacentMines() > 0) {
			return;
		}

		for(int x1 = x - 1; x1 <= x + 1; x1++) {
			for(int y1 = y - 1; y1 <= y + 1; y1++) {
				floodReveal(x1, y1);
			}
		}
	}

	public void revealAll() {
		if(!isGameOver()) {
			throw new IllegalStateException("Game is not over yet");
		}

		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				cells[x][y].reveal();
			}
		}
	}

	public void flag() {
		Cell cell = cells[x][y];
		if(cell.isRevealed()) return;

		cell.flag();
	}
}