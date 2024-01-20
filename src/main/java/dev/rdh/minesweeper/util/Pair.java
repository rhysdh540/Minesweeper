package dev.rdh.minesweeper.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
public class Pair<F, S> {
	private F first;
	private S second;
}
