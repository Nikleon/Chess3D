package com.teraleon.chess3d.game;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import com.teraleon.chess3d.game.pieces.Piece;
import com.teraleon.chess3d.game.pieces.Piece.Side;
import com.teraleon.chess3d.util.Coord;
import com.teraleon.chess3d.util.Move;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Game {
	
	private static final Color BACKGROUND_FILL = Color.BLACK;
	
	private Board board;
	
	private int turn;
	
	private int slice;
	
	private Set<Move> displayedMoves;
	
	private Coord clickedCoord;
	
	public Game() {
		board = new Board();
		turn = 1;
		slice = 0;
		clickedCoord = null;
		displayedMoves = new HashSet<>();
	}
	
	public Game(File file) throws IOException, ReflectiveOperationException {
		this();
		Scanner in = new Scanner(file);
		turn = in.nextInt();
		in.nextLine();
		while (in.hasNextLine()) {
			String line[] = in.nextLine().split(", ");
			Coord coord = new Coord(Integer.parseInt("" + line[0].charAt(1)), Integer.parseInt("" + line[0].charAt(3)), Integer.parseInt("" + line[0].charAt(5)));
			Side side = (line[1].charAt(0) == 'W') ? Side.WHITE : Side.BLACK;
			
			Class<?> clazz = Class.forName("com.teraleon.chess3d.game.pieces." + line[2]);
			Constructor<?> constructor = clazz.getConstructor();
			Piece piece = (Piece) constructor.newInstance();
			
			board.addPiece(coord, piece, side);
		}
		
		in.close();
	}
	
	public void save(File file) throws IOException {
		PrintWriter out = new PrintWriter(file);
		out.println(turn);
		board.getPrimaryPieces().forEach((c, p) -> {
			out.format("(%d,%d,%d), W, %s\n", c.x, c.y, c.z, p.getClass().getSimpleName());
		});
		board.getSecondaryPieces().forEach((c, p) -> {
			out.format("(%d,%d,%d), B, %s\n", c.x, c.y, c.z, p.getClass().getSimpleName());
		});
		out.close();
	}
	
	public void draw(GraphicsContext gc, boolean is3D) {
		double w = gc.getCanvas().getWidth();
		double h = gc.getCanvas().getHeight();
		
		gc.setFill(BACKGROUND_FILL);
		gc.fillRect(0, 0, w, h);
		
		if (is3D)
			board.draw3D(gc);
		else {
			board.draw2D(gc, w, h, new Point2D(w / 2, h / 2), slice);
			
			gc.setStroke(Color.RED);
			displayedMoves.forEach(move -> {
				Coord target = clickedCoord.add(move.getOffset());
				if (target.z != slice)
					return;
				gc.strokeRect(target.x * w / 8, target.y * h / 8, w / 8, h / 8);
			});
			
			gc.setStroke(Color.BLACK);
			gc.strokeText("Slice: " + (slice + 1), 10, h - 10);
			
			if (turn % 2 == 0)
				gc.strokeText("Turn: " + turn + ", B", 7 * w / 8 + 10, h - 10);
			else
				gc.strokeText("Turn: " + turn + ", W", 7 * w / 8 + 10, h - 10);
		}
	}
	
	public void dSlice(int dS) {
		slice = Math.max(Math.min(slice + dS, 7), 0);
	}
	
	public void handleClick(double mx, double my, double w, double h) {
		int x = (int) (8 * mx / w);
		int y = (int) (8 * my / h);
		
		Coord coord = Coord.of(x, y, slice);
		Piece p = board.getPieceAt(coord, turn);
		
		if (p == null) {
			displayedMoves.clear();
			return;
		}
		
		clickedCoord = coord;
		
		displayedMoves.addAll(board.getValidMoves(coord, p));
		
	}
	
}
