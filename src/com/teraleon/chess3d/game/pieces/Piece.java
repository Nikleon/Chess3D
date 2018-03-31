package com.teraleon.chess3d.game.pieces;

import java.util.Set;

import com.teraleon.chess3d.util.Move;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

public abstract class Piece {

	private int moveCount;

	public Piece(Integer moveCount) {
		this.moveCount = moveCount;
	}

	public int getMoveCount() {
		return moveCount;
	}

	public void incrementMoveCount() {
		moveCount++;
	}

	public String getParameters() {
		return "";
	}

	public abstract Set<Move> getMoves();

	public abstract void draw2D(GraphicsContext gc, double w, double h, Point2D c);

}