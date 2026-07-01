package org.unibl.etf.controller;

import java.io.*;
import java.util.*;
import java.util.logging.Level;

import org.json.*;
import org.unibl.etf.model.DokumentDTO;
import org.unibl.etf.rest.record.EvidencijaServis;
import org.unibl.etf.utility.FileLogger;
import org.unibl.etf.utility.PropertiesUtil;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class CarinskiDokumenti {

	@FXML
	private Button btnIzlaz;

	@FXML
	private ListView<String> lvListaTerminala;

	@FXML
	private ListView<DokumentDTO> lvListaDokumentovanihProlazaka;

	@FXML
	private TextArea taDokumenti;

	private Map<String, List<DokumentDTO>> mapaDokumentovanihProlazaka = new HashMap<>();

	@FXML
	public void initialize() {
		popunjavanjeMapeDokumentovanihProlazaka();
		prikaziTerminale();
	}

	@FXML
	void izadji(ActionEvent event) {
		Stage stage = (Stage) btnIzlaz.getScene().getWindow();
		stage.close();
	}

	private void popunjavanjeMapeDokumentovanihProlazaka() {
		String separator = PropertiesUtil.vratiSvojstvo("SEPARATOR", String.class);
		try {
			JSONArray jsonarray = EvidencijaServis.evidentiraniDokumenti();
			for (int i = 0; i < jsonarray.length(); i++) {
				JSONObject jsonObject = jsonarray.getJSONObject(i);
				String id = jsonObject.getString("id");
				String idTerminal = id.split(separator)[0];
				String idProlaz = id.split(separator)[1];
				String vrijemeEvidencije = id.split(separator)[2];
				String datum = vrijemeEvidencije.split("T")[0].replace("_", ".");
				String vrijemeR = vrijemeEvidencije.split("T")[1].replace("_", ":");
				String vrijeme = datum + " " + vrijemeR;
				String idOsoba = jsonObject.getString("idOsoba");
				List<String> dokumenti = new ArrayList<String>();
				JSONArray dokumentiArray = (JSONArray) jsonObject.get("odabraniFajlovi");
				for (int j = 0; j < dokumentiArray.length(); j++)
					dokumenti.add(new File(dokumentiArray.getString(j)).getName());
				String info = kreirajInfo(idTerminal, idProlaz, vrijeme, dokumenti);
				DokumentDTO dokument = new DokumentDTO(idOsoba, info);
				if (mapaDokumentovanihProlazaka.containsKey(idTerminal)) {
					mapaDokumentovanihProlazaka.get(idTerminal).add(dokument);
				} else {
					List<DokumentDTO> listaDokumenata = new ArrayList<DokumentDTO>();
					listaDokumenata.add(dokument);
					mapaDokumentovanihProlazaka.put(idTerminal, listaDokumenata);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			FileLogger.log(Level.SEVERE, "Greska u radu sa JSON!", e);
		}
	}

	private void prikaziTerminale() {
		for (String idTerminal : mapaDokumentovanihProlazaka.keySet())
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
					taDokumenti.setText(idTerminal);
					lvListaDokumentovanihProlazaka.getItems().clear();
					ucitajDetalje(idTerminal);
				} else
					taDokumenti.setText("");
			}
		});
	}

	private void ucitajDetalje(String idTerminal) {
		lvListaDokumentovanihProlazaka.getItems().addAll(mapaDokumentovanihProlazaka.get(idTerminal));
		prikaziDetalje(null);
		lvListaDokumentovanihProlazaka.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> prikaziDetalje(newValue));
	}

	private void prikaziDetalje(DokumentDTO dokument) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (dokument != null)
					taDokumenti.setText(dokument.getInfo());
				else
					taDokumenti.setText("");
			}
		});
	}

	private String kreirajInfo(String idTerminal, String idProlaz, String vrijeme, List<String> dokumenti) {
		StringBuilder sb = new StringBuilder();
		sb.append("Terminal: " + idTerminal + "\n");
		sb.append("Prolaz: " + idProlaz + "\n");
		sb.append("Vrijeme: " + vrijeme + "\n");
		sb.append("Prilozeni dokumenti:\n");
		dokumenti.forEach(d -> sb.append("\t" + d + "\n"));
		return sb.toString();
	}

}
