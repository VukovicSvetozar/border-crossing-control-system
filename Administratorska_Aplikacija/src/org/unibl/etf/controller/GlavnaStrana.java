package org.unibl.etf.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import org.unibl.etf.dao.DAOFactory;
import org.unibl.etf.model.*;
import org.unibl.etf.soap.centralregistry.*;
import org.unibl.etf.utility.*;

public class GlavnaStrana {

	@FXML
	private Label lbIme;

	@FXML
	private Button btnOsobeSaPotjernice;

	@FXML
	private Button btnCarinskiDokumenti;

	@FXML
	private Button btnIzlaz;

	@FXML
	private Button btnDodajTerminal;

	@FXML
	private ImageView ivDodajTerminal;

	@FXML
	private Button btnIzmjeniTerminal;

	@FXML
	private ImageView ivIzmjeniTerminal;

	@FXML
	private Button btnUkloniTerminal;

	@FXML
	private ImageView ivUkloniTerminal;

	@FXML
	private HBox hbKontejnerTabela;

	@FXML
	private TextField tfPretraga;

	private static TableView<TerminalDTO> tvTerminali;
	private static TableColumn<TerminalDTO, String> tcId;
	private static TableColumn<TerminalDTO, String> tcNaziv;
	private static TableColumn<TerminalDTO, String> tcTipFajla;

	private static ObservableList<TerminalDTO> terminali;
	private TerminalDTO odabraniTerminal = new TerminalDTO();

	@FXML
	void initialize() {
		postaviImeAdministratora();
		ucitajTerminale();
		kreirajTabelu();
		popuniTabelu();
//		pretrazi();
		prikaziProlaze();
	}

	@FXML
	void preuzmiSpisakOsoba(ActionEvent event) {
		FxmlLoader.load(getClass(), "/org/unibl/etf/view/Potjernice.fxml", "Osobe sa potjernice");
	}

	@FXML
	void preuzmiDokumente(ActionEvent event) {
		FxmlLoader.load(getClass(), "/org/unibl/etf/view/CarinskiDokumenti.fxml", "Carinski dokumenti");
	}

	@FXML
	public void izadji(ActionEvent event) {
		Stage stage = (Stage) btnIzlaz.getScene().getWindow();
		stage.close();
	}

	@FXML
	void dodajTerminal(ActionEvent event) {
		FxmlLoader.load(getClass(), "/org/unibl/etf/view/DodajTerminal.fxml", "Dodaj terminal");
	}

	@FXML
	void izmjeniTerminal(ActionEvent event) {
		if (tvTerminali.getSelectionModel().getSelectedItem() == null) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Upozorenje");
			alert.setHeaderText("Niste odabrali ni jednu stavku!");
			alert.setContentText("Odaberite stavku.");
			alert.showAndWait();
		} else {
			odabraniTerminal = tvTerminali.getSelectionModel().getSelectedItem();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/unibl/etf/view/IzmjeniTerminal.fxml"));
			Stage stage = new Stage(StageStyle.UNDECORATED);
			try {
				stage.setScene(new Scene((Pane) loader.load()));
			} catch (IOException e) {
				FileLogger.log(Level.SEVERE, null, e);
			}
			IzmjeniTerminal controller = loader.<IzmjeniTerminal>getController();
			controller.initData(odabraniTerminal);
			stage.show();
		}
	}

	@FXML
	void ukloniTerminal(ActionEvent event) {
		if (tvTerminali.getSelectionModel().getSelectedItem() == null) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Upozorenje");
			alert.setHeaderText("Niste odabrali ni jednu stavku!");
			alert.setContentText("Odaberite stavku.");
			alert.showAndWait();
		} else {
			odabraniTerminal = tvTerminali.getSelectionModel().getSelectedItem();
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Dijalog potvrde");
			alert.setHeaderText("Zelite da uklonite odabrani terminal!");
			alert.setContentText("Da li ste sigurni?");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				CentralniRegistarSOAPServiceLocator loc = new CentralniRegistarSOAPServiceLocator();
				try {
					CentralniRegistarSOAP ser = loc.getCentralniRegistarSOAP();
					ser.obrisiTerminal(odabraniTerminal.getId());
				} catch (Exception e) {
					FileLogger.log(Level.SEVERE, "Greska u radu sa SOAP servisom!", e);
				}
				popuniTabelu();
				ObservableList<KredencijaliDTO> odabraniKredencijali = DAOFactory.getDAOFactory().getKredencijaliDAO()
						.kredencijaliNaTerminalu(odabraniTerminal.getId());
				for (KredencijaliDTO kredencijali : odabraniKredencijali)
					DAOFactory.getDAOFactory().getKredencijaliDAO().obrisiKredencijale(kredencijali);
			}
		}
	}

	@FXML
	void pretrazi(MouseEvent event) {
		pretrazi();
	}

	public void postaviImeAdministratora() {
		lbIme.setText(Prijava.imeAdministratora);
	}

	public static void ucitajTerminale() {
		CentralniRegistarSOAPServiceLocator loc = new CentralniRegistarSOAPServiceLocator();
		try {
			CentralniRegistarSOAP ser = loc.getCentralniRegistarSOAP();
			List<TerminalDTO> lista = Arrays.stream(ser.terminali()).collect(Collectors.toList());
			terminali = FXCollections.observableList(lista);
		} catch (Exception e) {
			FileLogger.log(Level.SEVERE, "Greska u radu sa SOAP servisom!", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void kreirajTabelu() {

		tvTerminali = new TableView<TerminalDTO>();
		tvTerminali.setId("tvTerminali");
		tvTerminali.setMaxHeight(310.0);
		tvTerminali.setPrefHeight(310.0);
		tvTerminali.setMinHeight(310.0);
		tvTerminali.setMaxWidth(550.0);
		tvTerminali.setPrefWidth(550.0);
		tvTerminali.setMinWidth(550.0);

		tcId = new TableColumn<TerminalDTO, String>();
		tcNaziv = new TableColumn<TerminalDTO, String>();
		tcTipFajla = new TableColumn<TerminalDTO, String>();

		tcId.setId("tcId");
		tcId.setEditable(false);
		tcId.setMaxWidth(170.0);
		tcId.setMinWidth(170.0);
		tcId.setPrefWidth(170.0);
		tcId.setText("id");

		tcNaziv.setId("tcNaziv");
		tcNaziv.setEditable(false);
		tcNaziv.setMaxWidth(188.0);
		tcNaziv.setMinWidth(188.0);
		tcNaziv.setPrefWidth(188.0);
		tcNaziv.setText("naziv");

		tcTipFajla.setId("tcTipFajla");
		tcTipFajla.setEditable(false);
		tcTipFajla.setMaxWidth(190.0);
		tcTipFajla.setMinWidth(190.0);
		tcTipFajla.setPrefWidth(190.0);
		tcTipFajla.setText("tip fajla");

		tvTerminali.getColumns().addAll(tcId, tcNaziv, tcTipFajla);
		tvTerminali.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		hbKontejnerTabela.getChildren().add(tvTerminali);

	}

	public static void popuniTabelu() {
		ucitajTerminale();
		tcId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tcNaziv.setCellValueFactory(new PropertyValueFactory<>("naziv"));
		tcTipFajla.setCellValueFactory(new PropertyValueFactory<>("tipSerijalizacije"));
		tvTerminali.setItems(terminali);
	}

	private void pretrazi() {
		FilteredList<TerminalDTO> filteredData = new FilteredList<>(terminali, p -> true);
		tfPretraga.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(terminal -> {
				if (newValue == null || newValue.isEmpty())
					return true;
				String lowerCaseFilter = newValue.toLowerCase();
				if (terminal.getId().toLowerCase().contains(lowerCaseFilter)) {
					return true;
				} else if (terminal.getNaziv().toLowerCase().contains(lowerCaseFilter)) {
					return true;
				} else if (terminal.getTipSerijalizacije().toLowerCase().contains(lowerCaseFilter)) {
					return true;
				} else
					return false;
			});
		});
		SortedList<TerminalDTO> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(tvTerminali.comparatorProperty());
		tvTerminali.setItems(sortedData);
	}

	private void prikaziProlaze() {
		tvTerminali.setRowFactory(tv -> {
			TableRow<TerminalDTO> red = new TableRow<>();
			red.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!red.isEmpty())) {
					odabraniTerminal = red.getItem();
					FXMLLoader loader = new FXMLLoader(
							getClass().getResource("/org/unibl/etf/view/PregledTerminal.fxml"));
					Stage stage = new Stage(StageStyle.UNDECORATED);
					try {
						stage.setScene(new Scene((Pane) loader.load()));
					} catch (IOException e) {
						FileLogger.log(Level.SEVERE, "fxml fajl nije pronadjen!", e);
					}
					PregledTerminal controller = loader.<PregledTerminal>getController();
					controller.initData(odabraniTerminal);
					stage.show();
				}
			});
			return red;
		});
	}

	public static ObservableList<TerminalDTO> getTerminali() {
		return terminali;
	}

	public static void setTerminali(ObservableList<TerminalDTO> terminali) {
		GlavnaStrana.terminali = terminali;
	}

}
