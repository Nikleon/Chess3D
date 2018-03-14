package com.teraleon.chess3d;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuItem;

public class GUIController {

	@FXML
	private Canvas canvas;

	@FXML
	private CheckMenuItem toggle3DButton;

	@FXML
	private MenuItem newGameButton, saveGameButton, closeButton;

	@FXML
	private void initialize() {
		closeButton.setOnAction(evt -> {
			System.exit(0);
		});
	}

}
