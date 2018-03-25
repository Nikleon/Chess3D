package com.teraleon.chess3d;

import java.io.File;
import java.io.IOException;

import com.teraleon.chess3d.game.Game;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class GUIController {

	@FXML
	private BorderPane root;

	@FXML
	private Canvas canvas;

	@FXML
	private CheckMenuItem toggle3DButton;

	@FXML
	private MenuItem newGameButton, saveGameButton, closeButton, openGameButton;

	private Game game;

	public void initHandlers(Stage primaryStage) {
		closeButton.setOnAction(evt -> {
			System.exit(0);
		});

		newGameButton.setOnAction(evt -> {
			game = new Game();
			this.drawGame(game);
		});

		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Chess3D Files", "*.chs"),
				new ExtensionFilter("All Files", "*.*"));

		openGameButton.setOnAction(evt -> {
			fileChooser.setTitle("Open");
			File file = fileChooser.showOpenDialog(primaryStage);
			if (file != null)
				try {
					game = new Game(file);
					this.drawGame(game);
				} catch (IOException | ReflectiveOperationException e) {
					e.printStackTrace();
				}
		});

		saveGameButton.setOnAction(evt -> {
			fileChooser.setTitle("Save");
			File file = fileChooser.showSaveDialog(primaryStage);
			if (file != null)
				try {
					game.save(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
		});

		root.setOnScroll(scrollEvt -> {
			if (game == null)
				return;
			game.dSlice((int) Math.signum(scrollEvt.getDeltaY()));
			this.drawGame(game);
		});

		primaryStage.getScene().setOnKeyPressed(evt -> {
			if (game == null)
				return;

			switch (evt.getCode()) {
			case UP:
				game.dSlice(1);
				break;
			case DOWN:
				game.dSlice(-1);
				break;
			default:
				break;
			}

			this.drawGame(game);
		});

		canvas.setOnMouseClicked(evt -> {
			if (game == null)
				return;
		});
	}

	private void drawGame(Game g) {
		g.draw(canvas.getGraphicsContext2D(), toggle3DButton.isSelected());
	}

	public Canvas getCanvas() {
		return canvas;
	}

}
