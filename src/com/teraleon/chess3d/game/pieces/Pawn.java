package com.teraleon.chess3d.game.pieces;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

import com.teraleon.chess3d.game.Game.Context;
import com.teraleon.chess3d.game.Game.Side;
import com.teraleon.chess3d.util.Coord;
import com.teraleon.chess3d.util.Move;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

public class Pawn extends Piece {

	/* ADVANCE MOVE */
	private static final Coord ADVANCE = Coord.of(1, 0, 0);
	private static final BiPredicate<Context, Move> ADVANCE_RULE = (ctx, move) -> {
		// If traveling against the piece's default moving direction, not a valid move
		if (ctx.getLocalSide().dx * move.getOffset().x < 0)
			return false;

		// Valid move iff target tile is empty
		return (ctx.getBoard().getPieceAt(ctx.getTarget(move.getOffset())) == null);
	};
	private static final BiConsumer<Context, Move> ADVANCE_ACTION = (ctx, move) -> {
		Side side = ctx.getLocalSide();

		// Remove pawn from local tile
		Pawn pawn = (Pawn) ctx.getBoard().removePiece(ctx.getPos(), side);

		// Place pawn on advance tile
		ctx.getBoard().setPiece(ctx.getTarget(move.getOffset()), pawn, side);
	};

	/* DOUBLE ADVANCE MOVE */
	private static final Coord DOUBLE_ADVANCE = Coord.of(2, 0, 0);
	private static final BiPredicate<Context, Move> DOUBLE_ADVANCE_RULE = (ctx, move) -> {
		Side side = ctx.getLocalSide();

		// If traveling against the piece's default moving direction, not a valid move
		if (side.dx * move.getOffset().x < 0)
			return false;

		// If not on side's respective second rank, double advance is not valid
		if (side == Side.WHITE) {
			if (ctx.getPos().x != 1)
				return false;
		} else {
			if (ctx.getPos().x != 6)
				return false;
		}

		// If there is a piece blocking the way, not a valid move
		Coord interTile = ctx.getTarget(side.getDefaultStep());
		if (ctx.getBoard().getPieceAt(interTile) != null)
			return false;

		// Valid move iff target tile is empty
		return (ctx.getBoard().getPieceAt(ctx.getTarget(move.getOffset())) == null);
	};
	private static final BiConsumer<Context, Move> DOUBLE_ADVANCE_ACTION = (ctx, move) -> {
		Side side = ctx.getLocalSide();

		// Remove pawn from local tile
		Pawn pawn = (Pawn) ctx.getBoard().removePiece(ctx.getPos(), side);

		// Place pawn on double advance tile
		ctx.getBoard().setPiece(ctx.getTarget(move.getOffset()), pawn, side);

		// Update turn number of most recent double advance
		pawn.setLastDouble(ctx.getTurn());
	};

	/* CAPTURE MOVES */
	private static final Coord CAPTURES[] = { Coord.of(1, -1, 0), Coord.of(1, 1, 0), Coord.of(1, 0, -1),
			Coord.of(1, 0, 1) };
	private static final BiPredicate<Context, Move> CAPTURE_RULE = (ctx, move) -> {
		Coord target = ctx.getTarget(move.getOffset());

		// If traveling against the piece's default moving direction, not a valid move
		if (ctx.getLocalSide().dx * move.getOffset().x < 0)
			return false;

		// If there is an adjacent enemy pawn on same file, check if it double advanced
		// last turn
		Coord daTarget = Coord.of(ctx.getPos().x, target.y, target.z);
		Piece daPiece = ctx.getBoard().getPieceAt(daTarget, (ctx.getTurn() + 1) % 2);
		if (daPiece instanceof Pawn) {
			Pawn pawn = (Pawn) daPiece;
			// Iff it did, capture is possible
			return pawn.lastTurnWasDouble(ctx.getTurn());
		}

		// Valid move iff target tile contains a piece and that piece is an enemy
		return (ctx.getBoard().getPieceAt(target) != null && ctx.getBoard().getSide(target) != ctx.getLocalSide());
	};
	private static final BiConsumer<Context, Move> CAPTURE_ACTION = (ctx, move) -> {
		Side side = ctx.getLocalSide();
		Side oppSide = (side == Side.WHITE) ? Side.BLACK : Side.WHITE;
		Coord target = ctx.getTarget(move.getOffset());

		// Remove captured piece from board
		Piece capturedPiece = ctx.getBoard().removePiece(target, oppSide);
		// If there was no piece at capture target, it is an en passant
		if (capturedPiece == null)
			ctx.getBoard().removePiece(Coord.of(ctx.getPos().x, target.y, target.z), oppSide);

		// Remove pawn from local tile
		Pawn p = (Pawn) ctx.getBoard().removePiece(ctx.getPos(), side);

		// Place pawn on capture tile
		ctx.getBoard().setPiece(target, p, side);
	};

	private int lastDouble;

	public Pawn(Integer moveCount) {
		super(moveCount);
		lastDouble = -1;
	}

	public Pawn(Integer moveCount, Integer lastDA) {
		super(moveCount);
		this.lastDouble = lastDA;
	}

	@Override
	public String getParameters() {
		return ", " + this.getMoveCount();
	}

	@Override
	public Set<Move> getMoves() {
		Set<Move> moves = new HashSet<>();

		// Add moves wrt both black and white pawns - will be filtered later
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

	private boolean lastTurnWasDouble(int turn) {
		return turn - 1 == lastDouble;
	}

	private void setLastDouble(int turn) {
		this.lastDouble = turn;
	}

}