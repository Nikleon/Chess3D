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

	// Note: both directions are added in getMoves()
	private static final Coord ADVANCE = Coord.of(1, 0, 0);
	private static final BiPredicate<Context, Move> ADVANCE_RULE = (context, move) -> {
		Coord offset = move.getOffset();
		if (context.board.getSide(context.coord) == Side.WHITE) {
			if (offset.x < 0)
				return false;
		} else {
			if (offset.x > 0)
				return false;
		}

		return (context.board.getPieceAt(context.coord.add(offset)) == null);
	};
	private static final BiConsumer<Context, Move> ADVANCE_ACTION = (context, move) -> {
		Coord offset = move.getOffset();
		Side side = context.board.getSide(context.coord);
		Pawn p = (Pawn) context.board.removePiece(context.coord, side);
		context.board.setPiece(context.coord.add(offset), p, side);
	};

	private static final Coord DOUBLE_ADVANCE = Coord.of(2, 0, 0);
	private static final BiPredicate<Context, Move> DOUBLE_ADVANCE_RULE = (context, move) -> {
		Coord offset = move.getOffset();
		if (context.board.getSide(context.coord) == Side.WHITE) {
			if (offset.x < 0 || context.coord.x != 1)
				return false;
			if (context.board.getPieceAt(context.coord.add(Coord.of(1, 0, 0))) != null)
				return false;
		} else {
			if (offset.x > 0 || context.coord.x != 6)
				return false;
			if (context.board.getPieceAt(context.coord.add(Coord.of(-1, 0, 0))) != null)
				return false;
		}

		return (context.board.getPieceAt(context.coord.add(offset)) == null);
	};
	private static final BiConsumer<Context, Move> DOUBLE_ADVANCE_ACTION = (context, move) -> {
		Coord offset = move.getOffset();
		Side side = context.board.getSide(context.coord);
		Pawn p = (Pawn) context.board.removePiece(context.coord, side);
		context.board.setPiece(context.coord.add(offset), p, side);
		p.setLastDouble(context.turn);
	};

	private static final Coord CAPTURES[] = { Coord.of(1, -1, 0), Coord.of(1, 1, 0), Coord.of(1, 0, -1),
			Coord.of(1, 0, 1) };
	private static final BiPredicate<Context, Move> CAPTURE_RULE = (context, move) -> {
		Side side = context.board.getSide(context.coord);
		Coord offset = move.getOffset();
		if (side == Side.WHITE) {
			if (offset.x < 0)
				return false;
		} else {
			if (offset.x > 0)
				return false;
		}

		Coord target = context.coord.add(offset);
		
		Coord doubleAdvanceTarget = Coord.of(context.coord.x, target.y, target.z);
		Piece DAPiece = context.board.getPieceAt(doubleAdvanceTarget, context.turn + 1);
		if (DAPiece instanceof Pawn) {
			Pawn pawn = (Pawn) DAPiece;
			return pawn.lastTurnWasDouble(context.turn);
		}
		
		return (context.board.getPieceAt(target) != null && context.board.getSide(target) != side);
	};
	private static final BiConsumer<Context, Move> CAPTURE_ACTION = (context, move) -> {
		Side side = context.board.getSide(context.coord);
		Side oppSide = (side == Side.WHITE) ? Side.BLACK : Side.WHITE;
		Coord target = context.coord.add(move.getOffset());

		Piece capturedPiece = context.board.removePiece(target, oppSide);
		if (capturedPiece == null)
			context.board.removePiece(Coord.of(context.coord.x, target.y, target.z), oppSide);

		Pawn p = (Pawn) context.board.removePiece(context.coord, side);
		context.board.setPiece(target, p, side);
	};
	
	private int lastDouble;
	
	public Pawn() {
		lastDouble = -2;
	}
	
	public boolean lastTurnWasDouble(int turn) {
		return turn - 1 == lastDouble;
	}
	
	public void setLastDouble(int turn) {
		this.lastDouble = turn;
	}

	@Override
	public Set<Move> getMoves() {
		Set<Move> moves = new HashSet<>();

		for (int dir : new int[] { -1, 1 }) {
			moves.add(new Move(ADVANCE.scale(dir, 1, 1), ADVANCE_RULE, ADVANCE_ACTION));

			moves.add(new Move(DOUBLE_ADVANCE.scale(dir, 1, 1), DOUBLE_ADVANCE_RULE, DOUBLE_ADVANCE_ACTION));

			for (Coord c : CAPTURES)
				moves.add(new Move(c.scale(dir, 1, 1), CAPTURE_RULE, CAPTURE_ACTION));
		}
		return moves;
	}

	@Override
	public void draw2D(GraphicsContext gc, double w, double h, Point2D c) {
		gc.fillOval(c.getX() - w / 2, c.getY() - h / 2, w, h);
		gc.strokeOval(c.getX() - w / 2, c.getY() - h / 2, w, h);
	}

}