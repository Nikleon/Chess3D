package com.teraleon.chess3d.util;

import java.util.function.Predicate;

import com.teraleon.chess3d.game.Board;

public class Move {

	private final Coord offset;

	private final Predicate<Board> filter;

	public Move(Coord off, Predicate<Board> filt) {
		this.offset = off;
		this.filter = filt;
	}

	public Coord getOffset() {
		return offset;
	}

	public Predicate<Board> getFilter() {
		return filter;
	}

}
