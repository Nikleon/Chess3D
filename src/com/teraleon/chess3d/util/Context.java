package com.teraleon.chess3d.util;

import com.teraleon.chess3d.game.Board;

public class Context {

	public final Board board;
	public final Coord coord;
	public final int turn;

	public Context(Board b, Coord c, int t) {
		this.board = b;
		this.coord = c;
		this.turn = t;
	}

}
