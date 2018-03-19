package com.teraleon.chess3d.game.pieces;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import com.teraleon.chess3d.game.Board;
import com.teraleon.chess3d.util.Coord;
import com.teraleon.chess3d.util.Move;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

public class Pawn extends Piece {

	private static final Coord ADVANCE = Coord.of(0, 0, 2);
	private static final Predicate<Board> ADVANCE_RULE = board -> {
		return true; // TODO
	};

	private static final Coord DOUBLE_ADVANCE = Coord.of(0, 0, 2);
	private static final Predicate<Board> DOUBLE_ADVANCE_RULE = board -> {
		return false; // TODO
	};

	private static final Coord CAPTURES[] = { Coord.of(-1, 0, 1), Coord.of(0, -1, 1), Coord.of(1, 0, 1),
			Coord.of(0, 1, 1) };
	private static final Predicate<Board> CAPTURES_RULE = board -> {
		return false; // TODO
	};

	@Override
	public Set<Move> getMoves() {
		Set<Move> moves = new HashSet<>();

		moves.add(new Move(ADVANCE, ADVANCE_RULE));

		moves.add(new Move(DOUBLE_ADVANCE, DOUBLE_ADVANCE_RULE));

		for (Coord c : CAPTURES)
			moves.add(new Move(c, CAPTURES_RULE));

		return moves;
	}

	@Override
	public void draw2D(GraphicsContext gc, double w, double h, Point2D c) {
		gc.fillOval(c.getX() - w / 2, c.getY() - h / 2, w, h);
		gc.strokeOval(c.getX() - w / 2, c.getY() - h / 2, w, h);
	}

}