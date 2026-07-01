package org.unibl.etf.controller;

import static org.unibl.etf.controller.Prijava.*;

import org.unibl.etf.model.ProlazakDTO;
import org.unibl.etf.model.TipKontrole;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class EvidencijaPutnika {

	@FXML
	private Button btnIzlaz;

	@FXML
	private TextArea taInformacije;

	@FXML
	private ListView<ProlazakDTO> lvListaProlazaka;

	@FXML
	void izadji(ActionEvent event) {
		Stage stage = (Stage) btnIzlaz.getScene().getWindow();
		stage.close();
	}

	@FXML
	public void initialize() {
		popunjavanjeListeProlazaka();
		prikaziProlaske();
	}

	private void popunjavanjeListeProlazaka() {
		
		if (tipKontrole == TipKontrole.POLICIJSKA)
			for (ProlazakDTO prolazak : GlavnaStranaPolicija.listaProlazaka)
				lvListaProlazaka.getItems().add(prolazak);
		else
			for (ProlazakDTO prolazak : GlavnaStranaCarina.listaProlazaka)
				lvListaProlazaka.getItems().add(prolazak);
	}

	private void prikaziProlaske() {
		prikaziDetalje(null);
		lvListaProlazaka.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> prikaziDetalje(newValue));
	}

	private void prikaziDetalje(ProlazakDTO prolazak) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (prolazak != null)
					taInformacije.setText(prolazak.getInformacije());
				else
					taInformacije.setText("");
			}
		});
	}

}
