package org.unibl.etf.controller;

import java.util.ArrayList;
import java.util.List;

import org.unibl.etf.model.*;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

public class PregledTerminal {

	@FXML
	private HBox hbKontejnerTabela;

	@FXML
	private TableView<KorisnikDTO> tvKorisnici;

	@FXML
	private TableColumn<KorisnikDTO, String> tcRedniBroj;

	@FXML
	private TableColumn<KorisnikDTO, String> tcTipProlaza;

	@FXML
	private TableColumn<KorisnikDTO, String> tcTipKontrole;

	@FXML
	private TableColumn<KorisnikDTO, String> tcImeKorisnika;

	@FXML
	private Button btnOk;

	private static TerminalDTO odabraniTerminal;
	private static ObservableList<KorisnikDTO> odabraniKorisnici;

	public void initData(TerminalDTO terminal) {
		odabraniTerminal = terminal;
		List<KorisnikDTO> korisnici = new ArrayList<KorisnikDTO>();
		for (ProlazDTO prolaz : terminal.getProlazi())
			for (KorisnikDTO korisnik : prolaz.getKorisnici())
				korisnici.add(korisnik);
		odabraniKorisnici = FXCollections.observableArrayList(korisnici);
		popuniTabelu();
	}

	@FXML
	void potvrdi(ActionEvent event) {
		Stage stage = (Stage) btnOk.getScene().getWindow();
		stage.close();
	}

	private void popuniTabelu() {
		tcRedniBroj.setCellValueFactory(new PropertyValueFactory<>("idProlaz"));
		tcTipProlaza
				.setCellValueFactory(new Callback<CellDataFeatures<KorisnikDTO, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<KorisnikDTO, String> p) {
						return new SimpleStringProperty(vratiTipProlaza(p.getValue().getIdProlaz()));
					}
				});
		tcTipKontrole.setCellValueFactory(new PropertyValueFactory<>("tipKontrole"));
		tcImeKorisnika.setCellValueFactory(new PropertyValueFactory<>("korisnickoIme"));
		tvKorisnici.setItems(odabraniKorisnici);
	}

	private String vratiTipProlaza(String idProlaz) {
		String tipProlaza = null;
		for (ProlazDTO prolaz : odabraniTerminal.getProlazi())
			if (prolaz.getId().equals(idProlaz)) {
				tipProlaza = prolaz.getTipProlaza();
				break;
			}
		return tipProlaza;
	}

}
