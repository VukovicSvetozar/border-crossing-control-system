package org.unibl.etf.application;

import org.unibl.etf.utility.*;

import javafx.application.Application;
import javafx.stage.Stage;

public class PokretanjeKlijent extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		ConstantsUtil.ucitajKonstante();
		FxmlLoader.load(getClass(), "/org/unibl/etf/view/Prijava.fxml", "Prijava");
	}

	public static void main(String[] args) {
		launch(args);
	}

}
