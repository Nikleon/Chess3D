package com.teraleon.chess3d;

import com.teraleon.chess3d.game.Game;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;

public class GUIController {

	@FXML
	private BorderPane root;

	@FXML
	private Canvas canvas;

	@FXML
	private CheckMenuItem toggle3DButton;

	@FXML
	private MenuItem newGameButton, saveGameButton, closeButton;

	private Game game;

	@FXML
	private void initialize() {
		closeButton.setOnAction(evt -> {
			System.exit(0);
		});

		newGameButton.setOnAction(evt -> {
			root.setOnScroll(scrollEvt -> {
				game.dSlice((int) Math.signum(scrollEvt.getTextDeltaY()));
				this.drawGame(game);
			});
			
			game = new Game();
			this.drawGame(game);
		});
	}

	private void drawGame(Game g) {
		g.draw(canvas.getGraphicsContext2D(), toggle3DButton.isSelected());
	}

	public Canvas getCanvas() {
		return canvas;
	}

}
