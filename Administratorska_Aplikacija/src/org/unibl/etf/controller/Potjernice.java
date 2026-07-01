package org.unibl.etf.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.unibl.etf.model.ProlazakDTO;
import org.unibl.etf.rest.record.EvidencijaServis;
import org.unibl.etf.utility.FileLogger;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class Potjernice {

	@FXML
	private Button btnIzlaz;

	@FXML
	private ListView<ProlazakDTO> lvListaProcesiranihProlazaka;

	@FXML
	private TextArea taInformacije;

	@FXML
	private ListView<String> lvListaTerminala;

	private Map<String, List<ProlazakDTO>> mapaProcesiranihProlazaka = new HashMap<>();

	@FXML
	public void initialize() {
		popunjavanjeMapeProcesiranihProlazaka();
		prikaziTerminale();
	}

	@FXML
	void izadji(ActionEvent event) {
		Stage stage = (Stage) btnIzlaz.getScene().getWindow();
		stage.close();
	}

	private void popunjavanjeMapeProcesiranihProlazaka() {
		try {
			JSONArray jsonarray = EvidencijaServis.evidentiraniPrestupnici();
			for (int i = 0; i < jsonarray.length(); i++) {
				JSONObject jsonObject = jsonarray.getJSONObject(i);
				String id = jsonObject.getString("id");
				String idTerminal = id.split("#")[0];
				String idOsoba = jsonObject.getString("idOsoba");
				String primljeneInformacije = jsonObject.getString("informacije");
				String informacije = obradaInformacija(id, primljeneInformacije);
				ProlazakDTO prolazak = new ProlazakDTO(id, idOsoba, informacije);
				if (mapaProcesiranihProlazaka.containsKey(idTerminal)) {
					mapaProcesiranihProlazaka.get(idTerminal).add(prolazak);
				} else {
					List<ProlazakDTO> listaDokumenata = new ArrayList<>();
					listaDokumenata.add(prolazak);
					mapaProcesiranihProlazaka.put(idTerminal, listaDokumenata);
				}
			}
		} catch (JSONException | IOException e) {
			FileLogger.log(Level.SEVERE, "Greska u radu sa JSON!", e);
		}
	}

	private void prikaziTerminale() {
		for (String idTerminal : mapaProcesiranihProlazaka.keySet())
			lvListaTerminala.getItems().add(idTerminal);
		prikaziDokumentovaneProlaske(null);
		lvListaTerminala.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> prikaziDokumentovaneProlaske(newValue));
	}

	private void prikaziDokumentovaneProlaske(String idTerminal) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (idTerminal != null) {
					taInformacije.setText(idTerminal);
					lvListaProcesiranihProlazaka.getItems().clear();
					ucitajDetalje(idTerminal);
				} else
					taInformacije.setText("");
			}
		});
	}

	private void ucitajDetalje(String idTerminal) {
		lvListaProcesiranihProlazaka.getItems().addAll(mapaProcesiranihProlazaka.get(idTerminal));
		prikaziDetalje(null);
		lvListaProcesiranihProlazaka.getSelectionModel().selectedItemProperty()
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

	private String obradaInformacija(String id, String primljeneInformacije) {
		StringBuilder sb = new StringBuilder();
		sb.append("Terminal: " + id.split("#")[0] + "\n");
		sb.append("Prolaz: " + id.split("#")[1] + "\n");
		String[] info = primljeneInformacije.split("\n");
		for (int i = 1; i < info.length; i++)
			sb.append(info[i] + "\n");
		return sb.toString();
	}

}
