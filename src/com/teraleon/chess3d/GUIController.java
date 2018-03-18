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
	
	public void init(Stage primaryStage) {
		closeButton.setOnAction(evt -> {
			System.exit(0);
		});
		
		newGameButton.setOnAction(evt -> {
			game = new Game();
			this.drawGame(game);
		});
		
		openGameButton.setOnAction(evt -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open");
			fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Chess3D Files", "*.chs"), new ExtensionFilter("All Files", "*.*"));
			File file = fileChooser.showOpenDialog(primaryStage);
			if (file != null)
				try {
					game = new Game(file);
				} catch (IOException | ReflectiveOperationException e) {
					e.printStackTrace();
				}
			this.drawGame(game);
		});
		
		root.setOnScroll(scrollEvt -> {
			game.dSlice((int) Math.signum(scrollEvt.getDeltaY()));
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
