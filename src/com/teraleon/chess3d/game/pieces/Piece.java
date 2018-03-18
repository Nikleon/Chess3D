package com.teraleon.chess3d.game.pieces;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

public abstract class Piece {
	
	public enum Side{WHITE, BLACK}

	public abstract void draw2D(GraphicsContext gc, double w, double h, Point2D c);

}