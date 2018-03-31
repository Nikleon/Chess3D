package com.teraleon.chess3d.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.teraleon.chess3d.game.pieces.Piece;
import com.teraleon.chess3d.util.Coord;
import com.teraleon.chess3d.util.Move;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Game {

	private static final Color BACKGROUND_FILL = Color.BLACK;

	// Game state
	private Board board;
	private int turn;

	// Visual state
	private int slice;
	private Coord clickedCoord;
	private Map<Coord, Move> displayedMoves;

	/**
	 * Constructs a new default game.
	 */
	public Game() {
		board = new Board();
		turn = 1;

		slice = 0;
		clickedCoord = null;
		displayedMoves = new HashMap<>();
	}

	/**
	 * Construct a {@link Game} from a given save {@link File}.
	 * 
	 * @param file
	 *            the save file
	 * @throws FileNotFoundException
	 *             if save file is not found
	 * @throws ReflectiveOperationException
	 *             if errors occur in loading the custom piece files
	 */
	public Game(File file) throws FileNotFoundException, ReflectiveOperationException {
		this();
		Scanner in = new Scanner(file);
		turn = in.nextInt();
		in.nextLine();
		while (in.hasNextLine()) {
			String line[] = in.nextLine().split(", ");
			Coord coord = new Coord(Integer.parseInt("" + line[0].charAt(1)), Integer.parseInt("" + line[0].charAt(3)),
					Integer.parseInt("" + line[0].charAt(5)));
			Side side = (line[1].charAt(0) == 'W') ? Side.WHITE : Side.BLACK;

			Class<?> clazz = Class.forName("com.teraleon.chess3d.game.pieces." + line[2]);
			List<Integer> params = new ArrayList<>();
			List<Class<Integer>> types = new ArrayList<>();
			params.add(Integer.parseInt(line[3]));
			types.add(Integer.class);
			if (line.length == 5)
				for (String paramStr : line[4].split(" ")) {
					params.add(Integer.parseInt(paramStr));
					types.add(Integer.class);
				}
			Constructor<?> constructor = clazz.getConstructor(types.toArray(new Class<?>[0]));
			Piece piece = (Piece) constructor.newInstance(params.toArray());

			board.setPiece(coord, piece, side);
		}

		in.close();
	}

	/**
	 * Saves the game state to a given file.
	 * 
	 * @param file
	 *            the output file
	 * @throws FileNotFoundException
	 *             if the output file is not found and cannot be created
	 */
	public void save(File file) throws FileNotFoundException {
		PrintWriter out = new PrintWriter(file);
		out.println(turn);
		board.getPrimaryPieces().forEach((c, p) -> {
			out.format("(%d,%d,%d), W, %s, %s%s\n", c.x, c.y, c.z, p.getClass().getSimpleName(), p.getMoveCount(),
					p.getParameters());
		});
		board.getSecondaryPieces().forEach((c, p) -> {
			out.format("(%d,%d,%d), B, %s, %s%s\n", c.x, c.y, c.z, p.getClass().getSimpleName(), p.getMoveCount(),
					p.getParameters());
		});
		out.close();
	}

	/**
	 * Draws the game on a {@link Canvas} (either in 2D or 3D).
	 * 
	 * @param gc
	 *            the canvas' {@link GraphicsContext}
	 * @param is3D
	 *            true if should be rendered in 3D, false otherwise
	 */
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
			displayedMoves.keySet().forEach(target -> {
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

	/**
	 * Attempt to change the visible slice number by a given amount.
	 * 
	 * @param ds
	 *            the number of slices to move by (including negatives)
	 */
	public void dSlice(int ds) {
		slice = Math.min(Math.max(0, slice + ds), 7);
	}

	/**
	 * Method to execute the respective game events when the canvas is clicked.
	 * 
	 * @param mx
	 *            the pixel X-coord wrt the canvas
	 * @param my
	 *            the pixel Y-coord wrt the canvas
	 * @param w
	 *            the pixel width of the canvas
	 * @param h
	 *            the pixel height of the canvas
	 * @param right
	 *            true if the click was a secondary mouse button, false otherwise
	 */
	public void handleClick(double mx, double my, double w, double h, boolean right) {

		if (right) {
			displayedMoves.clear();
			return;
		}

		int x = (int) (8 * mx / w);
		int y = (int) (8 * my / h);

		Coord coord = Coord.of(x, y, slice);

		if (displayedMoves.containsKey(coord)) {
			board.getPieceAt(clickedCoord, turn).incrementMoveCount();
			Move move = displayedMoves.get(coord);
			move.getAction().accept(new Context(clickedCoord), move);

			turn++;
			displayedMoves.clear();
			return;
		}

		displayedMoves.clear();

		Piece p = board.getPieceAt(coord, turn);
		if (p == null)
			return;

		clickedCoord = coord;

		board.getValidMoves(new Context(clickedCoord), p).forEach(move -> {
			displayedMoves.put(coord.add(move.getOffset()), move);
		});

	}

	/**
	 * Represents the player/color a game entity belongs to.
	 */
	public static enum Side {
		WHITE(1, 0, 0), BLACK(-1, 0, 0);

		/**
		 * The default X-axis direction of this {@link Side}
		 */
		public final int dx;

		/**
		 * The default Y-axis direction of this {@link Side}
		 */
		public final int dy;

		/**
		 * The default Z-axis direction of this {@link Side}
		 */
		public final int dz;

		Side(int dx, int dy, int dz) {
			this.dx = dx;
			this.dy = dy;
			this.dz = dz;
		}

		/**
		 * Returns a {@link Coord} representing one step in the side's default
		 * direction.
		 * 
		 * @return
		 */
		public Coord getDefaultStep() {
			return Coord.of(dx, dy, dz);
		}

		/**
		 * Returns the {@link Side} which makes its move on a given turn.
		 * 
		 * @param turn
		 *            the turn number
		 * @return the active side
		 */
		public static Side fromTurn(int turn) {
			return (turn % 2 == 1) ? WHITE : BLACK;
		}

	}

	/**
	 * Represents the {@link Game} object as seen from a specific tile's
	 * perspective.
	 */
	public class Context {

		private final Coord pos;

		/**
		 * Constructs a {@link Context} wrt a given reference tile.
		 * 
		 * @param position
		 *            the position of the reference tile
		 */
		public Context(Coord position) {
			this.pos = position;
		}

		/**
		 * Returns the {@link Board} object for this game.
		 * 
		 * @return the board
		 */
		public Board getBoard() {
			return board;
		}

		/**
		 * Returns the current game turn (starting from 1).
		 * 
		 * @return the turn number
		 */
		public int getTurn() {
			return turn;
		}

		/**
		 * Returns the {@link Side} which makes the next move.
		 * 
		 * @return the active side
		 */
		public Side getActiveSide() {
			return Side.fromTurn(turn);
		}

		/**
		 * Returns the {@link Side} which owns the piece on the local tile.
		 * 
		 * @return the side (null if empty)
		 */
		public Side getLocalSide() {
			return board.getSide(pos);
		}

		/**
		 * Returns the (x,y,z)-coordinate of the local tile.
		 * 
		 * @return the local coord
		 */
		public Coord getPos() {
			return pos;
		}

		/**
		 * Returns the (x,y,z)-coordinate of the destination tile at a given offset from
		 * the local tile.
		 * 
		 * @param offset
		 *            the offset
		 * @return the destination coord
		 */
		public Coord getTarget(Coord offset) {
			return pos.add(offset);
		}

	}

}
