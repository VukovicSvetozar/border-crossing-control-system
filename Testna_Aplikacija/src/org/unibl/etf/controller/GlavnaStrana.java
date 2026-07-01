package org.unibl.etf.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;

import org.unibl.etf.model.*;
import org.unibl.etf.service.notification.ObavjestenjeServis;
import org.unibl.etf.service.notification.ObavjestenjeThread;
import org.unibl.etf.soap.transit.TransitSOAP;
import org.unibl.etf.soap.transit.TransitSOAPServiceLocator;
import org.unibl.etf.utility.*;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.util.Duration;

import static org.unibl.etf.controller.Prijava.*;

public class GlavnaStrana implements Initializable {

	@FXML
	private Label txtNazivTerminala;

	@FXML
	private Label txtIdKontrola;

	@FXML
	private Button btnIzlaz;

	@FXML
	private VBox vbInfoKontejner;

	@FXML
	private HBox hbObavjestenja;

	@FXML
	private Button btnProvjeriOsobu;

	@FXML
	private TextField tfIdOsoba;

	@FXML
	private ListView<String> lvListaProlazaka;

	private static Label labelaObavjestenje;
	public static String obavjestenje;
	public static TextArea taInfo;
	public static boolean otvorenTerminal;
	private ArrayList<ProlazakDTO> listaProlazaka = new ArrayList<>();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		kreirajLabeluZaObavjestenja();
		kreirajPodrucjeZaObavjestenja();
		provjeriDostupnostTerminala();
		ucitajPocetneInformacije();
		prikaziInformacijeOProlascima();
		ukljuciObavjestenja();
	}

	@FXML
	void provjeriOsobu(ActionEvent event) {
		if (tfIdOsoba.getText().isEmpty()) {
			String porukaOPogresnomUnosu = "Unesite id osobe!\n";
			String upozorenje = "Niste unijeli potrebnu vrijednost!\n";
			AlertsUtil.showWarningDialog("Upozorenje", upozorenje, porukaOPogresnomUnosu);
		} else {
			ProlazakDTO prolazak = new ProlazakDTO(idTerminal, idProlaz, tfIdOsoba.getText());
			posaljiPodatkeOProlasku(prolazak);
		}
	}

	@FXML
	void izadji(ActionEvent event) {
		TransitSOAPServiceLocator loc = new TransitSOAPServiceLocator();
		try {
			TransitSOAP ser = loc.getTransitSOAP();
			ser.odjavaKontrole(idProlaz, idTerminal);
		} catch (Exception e) {
			FileLogger.log(Level.SEVERE, "Greska pri radu sa SOAP servisom!", e);
		}
		ObavjestenjeServis.zatvoriMulticastSocket();
		System.exit(0);
	}

	private void kreirajLabeluZaObavjestenja() {
		labelaObavjestenje = new Label();
		labelaObavjestenje.setText("Provjera terminala");
		labelaObavjestenje.setTextFill(Color.LIGHTSKYBLUE);
		labelaObavjestenje.setFont(Font.font("System Bold Italic", FontPosture.ITALIC, 18));
		hbObavjestenja.getChildren().add(labelaObavjestenje);
	}

	private void kreirajPodrucjeZaObavjestenja() {
		taInfo = new TextArea();
		taInfo.setText("informacije o prolascima");
		taInfo.setPrefHeight(230);
		taInfo.setPrefWidth(230);
		vbInfoKontejner.getChildren().add(taInfo);
	}

	private void provjeriDostupnostTerminala() {
		TransitSOAPServiceLocator loc = new TransitSOAPServiceLocator();
		try {
			TransitSOAP ser = loc.getTransitSOAP();
			otvorenTerminal = ser.provjeriDostupnost(idTerminal);
		} catch (Exception e) {
			FileLogger.log(Level.SEVERE, "Greska pri radu sa SOAP servisom!", e);
		}
	}

	public static void ispisiObavjestenje(String poruka) {
		final KeyFrame kf = new KeyFrame(Duration.seconds(2), e -> labelaObavjestenje.setText(poruka));
		final Timeline timeline = new Timeline(kf);
		Platform.runLater(timeline::play);
	}

	private void ucitajPocetneInformacije() {
		taInfo.setEditable(false);
		txtNazivTerminala.setText(nazivTerminala);
		txtIdKontrola.setText(idProlaz);
		if (otvorenTerminal)
			ispisiObavjestenje("Terminal je otvoren");
		else
			ispisiObavjestenje("Terminal je zatvoren");
	}

	private void prikaziInformacijeOProlascima() {
		prikaziDetalje(null);
		lvListaProlazaka.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> prikaziDetalje(newValue));
	}

	public static void prikaziDetalje(String idProlazak) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				TransitSOAPServiceLocator loc = new TransitSOAPServiceLocator();
				try {
					TransitSOAP ser = loc.getTransitSOAP();
					ProlazakDTO aktivanProlazak = ser.azurirajInformacije(idProlaz, idProlazak);
					if (aktivanProlazak != null)
						taInfo.setText(aktivanProlazak.getInformacije());
					else
						taInfo.setText("");
				} catch (Exception e) {
					FileLogger.log(Level.SEVERE, "Greska pri radu sa SOAP servisom!", e);
				}
			}
		});
	}

	private void ukljuciObavjestenja() {
		ObavjestenjeServis.omoguciObavjestenje(Integer.parseInt(idTerminal));
		ObavjestenjeThread ot = new ObavjestenjeThread();
		ot.start();
	}

	private void posaljiPodatkeOProlasku(ProlazakDTO prolazak) {
		if (otvorenTerminal) {
			TransitSOAPServiceLocator loc = new TransitSOAPServiceLocator();
			try {
				TransitSOAP ser = loc.getTransitSOAP();
				String status = ser.provjeraStatusaKontrole(idProlaz);
				switch (status) {
				case StatusProlaz.AKTIVAN:
					ser.dodajProlazak(idTerminal, idProlaz, prolazak);
					listaProlazaka.add(prolazak);
					lvListaProlazaka.getItems().add(prolazak.getId());
					obavjestenje = "Trenutno se testira: " + tfIdOsoba.getText();
					ispisiObavjestenje(obavjestenje);
					tfIdOsoba.clear();
					prikaziDetalje(prolazak.getId());
					break;
				case StatusProlaz.ZAUZET:
					String upozorenjeZauzet = "Prolaz: " + idProlaz + " je trenutno zauzet!\n";
					AlertsUtil.showWarningDialog("Upozorenje", upozorenjeZauzet, "Pokusajte kasnije.");
					tfIdOsoba.clear();
					break;
				}
			} catch (Exception e) {
				FileLogger.log(Level.SEVERE, "Greska pri radu sa SOAP servisom!", e);
			}
		} else {
			String upozorenjeZatvoren = "Granicni prelaz je trenutno zatvoren!\n";
			AlertsUtil.showWarningDialog("Upozorenje", upozorenjeZatvoren, "Pokusajte kasnije.");
			tfIdOsoba.clear();
		}
	}

}
