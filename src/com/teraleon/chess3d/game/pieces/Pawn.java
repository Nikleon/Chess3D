package com.teraleon.chess3d.game.pieces;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

public class Pawn extends Piece {

	@Override
	public void draw2D(GraphicsContext gc, double w, double h, Point2D c) {
		gc.fillOval(c.getX() - w / 2, c.getY() - h / 2, w, h);
		gc.strokeOval(c.getX() - w / 2, c.getY() - h / 2, w, h);
	}

}