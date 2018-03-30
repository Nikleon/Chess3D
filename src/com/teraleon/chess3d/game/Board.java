package com.teraleon.chess3d.game;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import com.teraleon.chess3d.game.pieces.Piece;
import com.teraleon.chess3d.game.pieces.Piece.Side;
import com.teraleon.chess3d.util.Context;
import com.teraleon.chess3d.util.Coord;
import com.teraleon.chess3d.util.Move;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Board {
	
	private static final Color BOARD_PRIMARY = Color.WHITE;
	private static final Color BOARD_SECONDARY = Color.ALICEBLUE;
	
	private static final Color PIECE_PRIMARY = Color.WHITE;
	private static final Color PIECE_SECONDARY = Color.BLACK;
	
	private static final Color PIECE_PRIMARY_STROKE = Color.BLACK;
	private static final Color PIECE_SECONDARY_STROKE = Color.BLACK;
	
	private static final BiPredicate<Context, Move> BOUNDING_RULE = (context, move) -> {
		Coord target = context.coord.add(move.getOffset());
		return (0 <= target.x && target.x <= 7) && (0 <= target.y && target.y <= 7) && (0 <= target.z && target.z <= 7);
	};
	
	private Map<Coord, Piece> primaryPieces;
	private Map<Coord, Piece> secondaryPieces;
	
	public Board() {
		primaryPieces = new HashMap<>();
		secondaryPieces = new HashMap<>();
	}
	
	public void addPiece(Coord coord, Piece piece, Side side) {
		if (side == Side.WHITE)
			primaryPieces.put(coord, piece);
		else if (side == Side.BLACK)
			secondaryPieces.put(coord, piece);
	}
	
	public Piece removePiece(Coord coord, Side side) {
		if (side == Side.WHITE)
			return primaryPieces.remove(coord);
		else if (side == Side.BLACK)
			return secondaryPieces.remove(coord);
		return null;
	}
	
	public Piece getPieceAt(Coord coord) {
		for (Coord pos : primaryPieces.keySet())
			if (pos.equals(coord))
				return primaryPieces.get(pos);
		for (Coord pos : secondaryPieces.keySet())
			if (pos.equals(coord))
				return secondaryPieces.get(pos);
		return null;
	}
	
	public Piece getPieceAt(Coord coord, int turn) {
		if (turn % 2 == 1) {
			for (Coord pos : primaryPieces.keySet())
				if (pos.equals(coord))
					return primaryPieces.get(pos);
		} else {
			for (Coord pos : secondaryPieces.keySet())
				if (pos.equals(coord))
					return secondaryPieces.get(pos);
		}
		return null;
	}
	
	public Side getSide(Coord coord) {
		if (primaryPieces.containsKey(coord))
			return Side.WHITE;
		if (secondaryPieces.containsKey(coord))
			return Side.BLACK;
		return null;
	}
	
	public Set<Move> getValidMoves(Coord coord, Piece piece) {
		Context context = new Context(this, coord);
		
		Set<Move> pm = piece.getMoves();
		
		BiPredicate<Context, Move> filter = BOUNDING_RULE;
		return pm.stream().filter(move -> {
			return filter.and(move.getFilter()).test(context, move);
		}).collect(Collectors.toSet());
	}
	
	public void draw2D(GraphicsContext gc, double w, double h, Point2D c, int slice) {
		Point2D topLeft = c.subtract(w / 2, h / 2);
		
		gc.setFill(BOARD_PRIMARY);
		gc.fillRect(topLeft.getX(), topLeft.getY(), w, h);
		
		gc.setFill(BOARD_SECONDARY);
		
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++) {
				Point2D center = topLeft.add((2 * i + 1) * w / 16, (2 * j + 1) * h / 16);
				Coord coord = new Coord(i, j, slice);
				
				gc.setFill(BOARD_SECONDARY);
				if ((i + j + slice) % 2 == 1)
					gc.fillRect(center.getX() - w / 16, center.getY() - h / 16, w / 8, h / 8);
				
				Piece p = null;
				if ((p = primaryPieces.get(coord)) != null) {
					gc.setFill(PIECE_PRIMARY);
					gc.setStroke(PIECE_PRIMARY_STROKE);
					p.draw2D(gc, w / 16, h / 16, center);
				} else if ((p = secondaryPieces.get(coord)) != null) {
					gc.setFill(PIECE_SECONDARY);
					gc.setStroke(PIECE_SECONDARY_STROKE);
					p.draw2D(gc, w / 16, h / 16, center);
				}
			}
	}
	
	public Map<Coord, Piece> getPrimaryPieces() {
		return primaryPieces;
	}
	
	public Map<Coord, Piece> getSecondaryPieces() {
		return secondaryPieces;
	}
	
	public void draw3D(GraphicsContext gc) {
		// TODO
	}
}
