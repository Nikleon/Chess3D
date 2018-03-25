package com.teraleon.chess3d.game.pieces;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

import com.teraleon.chess3d.util.Context;
import com.teraleon.chess3d.util.Coord;
import com.teraleon.chess3d.util.Move;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

public class Pawn extends Piece {

	private static final Coord ADVANCE = Coord.of(1, 0, 0);
	private static final BiPredicate<Context, Move> ADVANCE_RULE = (context, move) -> {
		Coord offset = move.getOffset();
		if (context.board.getSide(context.coord) == Side.BLACK)
			offset = offset.scale(-1, 0, 0);

		if (context.board.getPieceAt(context.coord.add(offset)) != null)
			return false;

		return true;
	};
	private static final BiConsumer<Context, Move> ADVANCE_ACTION = (context, move) -> {
		Coord offset = move.getOffset();
		Side side = context.board.getSide(context.coord);
		if (side == Side.BLACK)
			offset = offset.scale(-1, 0, 0);

		Piece p = context.board.removePiece(context.coord, side);
		context.board.addPiece(context.coord.add(offset), p, side);
	};

	private static final Coord DOUBLE_ADVANCE = Coord.of(2, 0, 0);
	private static final BiPredicate<Context, Move> DOUBLE_ADVANCE_RULE = (context, move) -> {
		return false; // TODO
	};
	private static final BiConsumer<Context, Move> DOUBLE_ADVANCE_ACTION = (context, move) -> {
		// TODO
	};

	private static final Coord CAPTURES[] = { Coord.of(1, -1, 0), Coord.of(1, 1, 0), Coord.of(1, 0, -1),
			Coord.of(1, 0, 1) };
	private static final BiPredicate<Context, Move> CAPTURES_RULE = (context, move) -> {
		return false; // TODO
	};
	private static final BiConsumer<Context, Move> CAPTURE_ACTION = (context, move) -> {
		// TODO
	};

	@Override
	public Set<Move> getMoves() {
		Set<Move> moves = new HashSet<>();

		moves.add(new Move(ADVANCE, ADVANCE_RULE, ADVANCE_ACTION));

		moves.add(new Move(DOUBLE_ADVANCE, DOUBLE_ADVANCE_RULE, DOUBLE_ADVANCE_ACTION));

		for (Coord c : CAPTURES)
			moves.add(new Move(c, CAPTURES_RULE, CAPTURE_ACTION));

		return moves;
	}

	@Override
	public void draw2D(GraphicsContext gc, double w, double h, Point2D c) {
		gc.fillOval(c.getX() - w / 2, c.getY() - h / 2, w, h);
		gc.strokeOval(c.getX() - w / 2, c.getY() - h / 2, w, h);
	}

}