package org.unibl.etf.application;

import java.util.logging.Level;

import org.unibl.etf.utility.*;

import javafx.application.Application;
import javafx.stage.Stage;

public class PokretanjeTest extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		ConstantsUtil.ucitajKonstante();
		FileLogger.log(Level.SEVERE, "vukovic!", null);
		FxmlLoader.load(getClass(), "/org/unibl/etf/view/Prijava.fxml", "Prijava");
	}

	public static void main(String[] args) {
		launch(args);
	}

}
