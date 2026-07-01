package org.unibl.etf.application;

import org.unibl.etf.utility.FxmlLoader;

import javafx.application.Application;
import javafx.stage.Stage;

public class PokretanjeAdministrator extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		FxmlLoader.load(getClass(), "/org/unibl/etf/view/Prijava.fxml", "Prijava");
	}

	public static void main(String[] args) {
		launch(args);
	}

}
