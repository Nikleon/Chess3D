package com.teraleon.chess3d.game;

import java.util.HashMap;
import java.util.Map;

import com.teraleon.chess3d.game.pieces.Piece;
import com.teraleon.chess3d.util.Coord;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Board {

	private static final Color BOARD_PRIMARY = Color.WHITE;
	private static final Color BOARD_SECONDARY = Color.ALICEBLUE;

	private Map<Coord, Piece> primaryPieces;
	private Map<Coord, Piece> secondaryPieces;

	public Board() {
		primaryPieces = new HashMap<>();
		secondaryPieces = new HashMap<>();
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
	
	public void draw2D(GraphicsContext gc, double w, double h, Point2D c, int slice) {
		Point2D topLeft = c.subtract(w / 2, h / 2);

		gc.setFill(BOARD_PRIMARY);
		gc.fillRect(topLeft.getX(), topLeft.getY(), w, h);

		gc.setFill(BOARD_SECONDARY);
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++)
				if ((i + j + slice) % 2 == 1)
					gc.fillRect(topLeft.getX() + i * w / 8, topLeft.getX() + j * h / 8, w / 8, h / 8);

	}

	public void draw3D(GraphicsContext gc) {
		// TODO
	}
}
