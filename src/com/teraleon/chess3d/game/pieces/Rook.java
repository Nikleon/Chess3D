package com.teraleon.chess3d.game.pieces;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

import com.teraleon.chess3d.game.Game.Context;
import com.teraleon.chess3d.util.Coord;
import com.teraleon.chess3d.util.Move;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

public class Rook extends Piece {

	private static final Coord[] MOVES = { Coord.of(1, 0, 0), Coord.of(0, 1, 0), Coord.of(0, 0, 1) };
	private static final BiPredicate<Context, Move> MOVE_RULE = (ctx, move) -> {
		return false;
	};
	private static final BiConsumer<Context, Move> MOVE_ACTION = (ctx, move) -> {
	};

	private static final BiPredicate<Context, Move> CAPTURE_RULE = (ctx, move) -> {
		return false;
	};
	private static final BiConsumer<Context, Move> CAPTURE_ACTION = (ctx, move) -> {
	};

	public Rook(Integer moveCount) {
		super(moveCount);
	}

	@Override
	public Set<Move> getMoves() {
		Set<Move> moves = new HashSet<>();

		// Add moves wrt both black and white pawns - will be filtered later
		for (int dir : new int[] { -1, 1 })
			for (int dist = 1; dist <= 7; dist++)
				for (Coord baseMove : MOVES) {
					Coord offset = baseMove.scale(dir * dist, dir * dist, dir * dist);
					moves.add(new Move(offset, MOVE_RULE, MOVE_ACTION));
					moves.add(new Move(offset, CAPTURE_RULE, CAPTURE_ACTION));
				}
		return moves;
	}

	@Override
	public void draw2D(GraphicsContext gc, double w, double h, Point2D c) {
		gc.fillRect(c.getX() - w / 2, c.getY() - h / 2, w, h);
		gc.strokeRect(c.getX() - w / 2, c.getY() - h / 2, w, h);
	}

}
