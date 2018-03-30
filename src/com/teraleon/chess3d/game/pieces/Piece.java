package com.teraleon.chess3d.game.pieces;

import java.util.Set;

import com.teraleon.chess3d.util.Move;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

public abstract class Piece {

	public abstract Set<Move> getMoves();

	public abstract void draw2D(GraphicsContext gc, double w, double h, Point2D c);

}