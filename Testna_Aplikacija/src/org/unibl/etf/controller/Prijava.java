package org.unibl.etf.controller;

import java.util.logging.Level;

import org.unibl.etf.model.*;
import org.unibl.etf.soap.centralregistry.*;
import org.unibl.etf.soap.transit.*;
import org.unibl.etf.utility.*;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Prijava {

	@FXML
	private TextField tfNazivTerminala;

	@FXML
	private TextField tfIdProlaza;

	@FXML
	private VBox kontejnerPrijava;

	@FXML
	private Button btnPrijava;

	@FXML
	private Button btnIzlaz;

	public static String nazivTerminala;
	public static String idTerminal;
	public static String idProlaz;

	@FXML
	void izadji(ActionEvent event) {
		Platform.exit();
	}

	@FXML
	void prijava(ActionEvent event) {
		if (popunjenaPolja() && validacijaUlaza() && provjeraTerminala() && aktivanProlaz() && registracija()) {
			inicijalizujPolja();
			ucitajStranicu();
		}
	}

	private boolean popunjenaPolja() {
		String porukaOPogresnomUnosu = "";
		if (praznoPolje(tfNazivTerminala.getText()))
			porukaOPogresnomUnosu += "Unesite naziv terminala!\n";
		if (praznoPolje(tfIdProlaza.getText()))
			porukaOPogresnomUnosu += "Unesite id prolaza!\n";
		if (porukaOPogresnomUnosu.length() != 0) {
			String upozorenje = "Niste unijeli sva polja!\n";
			AlertsUtil.showWarningDialog("Upozorenje", upozorenje, porukaOPogresnomUnosu);
			return false;
		}
		return true;
	}

	private static boolean praznoPolje(String unos) {
		if (unos == null)
			return true;
		return unos.trim().length() == 0;
	}

	private boolean validacijaUlaza() {
		if (!tfNazivTerminala.getText().matches("[a-zA-Z0-9_]+") || !tfIdProlaza.getText().matches("[a-zA-Z0-9_]+")) {
			String upozorenje = "Nisu dozvoljeni specijalni karakteri!\n";
			AlertsUtil.showWarningDialog("Upozorenje", upozorenje, "Potrebno je da unesete cifre i slova.");
			return false;
		}
		return true;
	}

	private boolean provjeraTerminala() {
		boolean postojiTerminal = false;
		CentralniRegistarSOAPServiceLocator loc = new CentralniRegistarSOAPServiceLocator();
		try {
			CentralniRegistarSOAP ser = loc.getCentralniRegistarSOAP();
			postojiTerminal = ser.provjeriTerminal(tfNazivTerminala.getText());
			if (!postojiTerminal) {
				String upozorenje = "Uneseni naziv terminala ne postoji!\n";
				AlertsUtil.showWarningDialog("Upozorenje", upozorenje, "Pokusajte ponovo.");
			}
		} catch (Exception e) {
			FileLogger.log(Level.SEVERE, "Greska pri radu sa SOAP servisom!", e);
		}
		return postojiTerminal;
	}

	private boolean aktivanProlaz() {
		boolean aktivan = false;
		CentralniRegistarSOAPServiceLocator loc = new CentralniRegistarSOAPServiceLocator();
		try {
			CentralniRegistarSOAP ser = loc.getCentralniRegistarSOAP();
			aktivan = ser.aktivanProlaz(tfIdProlaza.getText());
			if (!aktivan) {
				String upozorenje = "Odabrani prolaz nije aktivan!\n";
				AlertsUtil.showWarningDialog("Upozorenje", upozorenje, "Pokusajte ponovo kasnije.");
			}
		} catch (Exception e) {
			FileLogger.log(Level.SEVERE, "Greska pri radu sa SOAP servisom!", e);
		}
		return aktivan;
	}

	private boolean registracija() {
		procitajIdTerminala();
		boolean registrovan = false;
		TransitSOAPServiceLocator loc = new TransitSOAPServiceLocator();
		try {
			TransitSOAP ser = loc.getTransitSOAP();
			registrovan = ser.registracijaKontrole(tfIdProlaza.getText(), idTerminal);
		} catch (Exception e) {
			FileLogger.log(Level.SEVERE, "Greska pri radu sa SOAP servisom!", e);
		}
		if (!registrovan) {
			String upozorenje = "Testna aplikacija je vec pokrenuta za navedeni prolaz!\n";
			AlertsUtil.showWarningDialog("Upozorenje", upozorenje, "Koristite pokrenutu aplikaciju.");
		}
		return registrovan;
	}

	private void procitajIdTerminala() {
		CentralniRegistarSOAPServiceLocator loc = new CentralniRegistarSOAPServiceLocator();
		try {
			CentralniRegistarSOAP ser = loc.getCentralniRegistarSOAP();
			for (TerminalDTO terminal : ser.terminali()) {
				if (terminal.getNaziv().equals(tfNazivTerminala.getText())) {
					idTerminal = terminal.getId();
					break;
				}
			}
		} catch (Exception e) {
			FileLogger.log(Level.SEVERE, "Greska pri radu sa SOAP servisom!", e);
		}
	}

	private void inicijalizujPolja() {
		nazivTerminala = tfNazivTerminala.getText();
		idProlaz = tfIdProlaza.getText();
	}

	private void ucitajStranicu() {
		Stage stage = (Stage) btnIzlaz.getScene().getWindow();
		stage.close();
		try {
			FxmlLoader.load(getClass(), "/org/unibl/etf/view/GlavnaStrana.fxml", "GlavnaStrana");
			stage = (Stage) btnPrijava.getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			FileLogger.log(Level.SEVERE, "Greska prilikom ucitavanja stranice!", e);
		}
	}

}
