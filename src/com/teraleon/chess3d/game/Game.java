package com.teraleon.chess3d.game;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Game {

	private static final Color BACKGROUND_FILL = Color.BLACK;

	private Board board;

	private int slice;

	public Game() {
		board = new Board();
		slice = 0;
	}

	public void draw(GraphicsContext gc, boolean is3D) {
		double w = gc.getCanvas().getWidth();
		double h = gc.getCanvas().getHeight();

		gc.setFill(BACKGROUND_FILL);
		gc.fillRect(0, 0, w, h);

		if (is3D)
			board.draw3D(gc);
		else
			board.draw2D(gc, w, h, new Point2D(w / 2, h / 2), slice);
	}

	public void dSlice(int dS) {
		slice = Math.max(Math.min(slice + dS, 7), 0);
	}

}
