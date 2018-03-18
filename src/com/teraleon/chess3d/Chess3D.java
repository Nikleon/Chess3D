package com.teraleon.chess3d;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Chess3D extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader rootLoader = new FXMLLoader(this.getClass().getResource("gui.fxml"));
		BorderPane root = rootLoader.<BorderPane>load();
		GUIController rootController = rootLoader.<GUIController>getController();
		rootController.init(primaryStage);

		primaryStage.setScene(new Scene(root));
		primaryStage.show();
	}

	public static void main(String[] args) {
		Application.launch();
	}

}
