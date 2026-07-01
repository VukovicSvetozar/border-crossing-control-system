package org.unibl.etf.controller;

import java.io.IOException;
import java.util.logging.Level;

import org.json.JSONException;
import org.unibl.etf.model.*;
import org.unibl.etf.rest.login.KredencijaliServis;
import org.unibl.etf.service.chat.*;
import org.unibl.etf.service.notification.*;
import org.unibl.etf.soap.centralregistry.*;
import org.unibl.etf.soap.transit.TransitSOAP;
import org.unibl.etf.soap.transit.TransitSOAPServiceLocator;
import org.unibl.etf.utility.*;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Prijava {

	@FXML
	private TextField tfIdProlaza;

	@FXML
	private ToggleGroup kontrola;

	@FXML
	private RadioButton rbPolicijskaKontrola;

	@FXML
	private RadioButton rbCarinskaKontrola;

	@FXML
	private TextField tfNazivTerminala;

	@FXML
	private Button btnProvjera;

	@FXML
	private VBox kontejnerPrijava;

	@FXML
	private TextField tfKorisnickoIme;

	@FXML
	private PasswordField tfLozinka;

	@FXML
	private Button btnPrijava;

	@FXML
	private Button btnIzlaz;

	public static KorisnikDTO aktivniKorisnik;
	public static String nazivTerminala;
	public static String idTerminal;
	public static String idProlaz;
	public static String tipKontrole;
	public static boolean otvorenTerminal;

	@FXML
	void initialize() {
		tfKorisnickoIme.setDisable(true);
		tfLozinka.setDisable(true);
		btnPrijava.setDisable(true);
	}

	@FXML
	void izadji(ActionEvent event) {
		Platform.exit();
	}

	@FXML
	void provjera(ActionEvent event) {
		if (popunjenaPolja() && validacijaUlaza() && provjeraTerminala()) {
			omoguciPrijavu();
		}
	}

	@FXML
	void prijava(ActionEvent event) throws JSONException, IOException {
		String korisnickoIme = tfKorisnickoIme.getText();
		String lozinka = tfLozinka.getText();
		tipKontrole = vratiTipKontrole();
		KorisnikDTO korisnik = new KorisnikDTO(false, tfIdProlaza.getText(), korisnickoIme, lozinka, tipKontrole);
		if (popunjeniKredencijali() && validacijaKredencijala() && provjeriLozinku(korisnik)) {
			aktivniKorisnik = korisnik;
			nazivTerminala = tfNazivTerminala.getText();
			idTerminal = vratiIdTerminala();
			idProlaz = tfIdProlaza.getText();
			omoguciChat();
			omoguciMulticast();
			provjeriDostupnostTerminala();
			ukljuciObavjestenja();
			ucitajStranicu(tipKontrole);
		}
	}

	private static boolean praznoPolje(String unos) {
		if (unos == null)
			return true;
		return unos.trim().length() == 0;
	}

	private boolean popunjenaPolja() {
		String porukaOPogresnomUnosu = "";
		if (praznoPolje(tfIdProlaza.getText()))
			porukaOPogresnomUnosu += "Unesite id prolaza!\n";
		if (kontrola.getSelectedToggle() == null)
			porukaOPogresnomUnosu += "Odaberite tip kontrole!\n";
		if (praznoPolje(tfNazivTerminala.getText()))
			porukaOPogresnomUnosu += "Unesite naziv terminala!\n";
		if (porukaOPogresnomUnosu.length() != 0) {
			String upozorenje = "Niste unijeli sva polja!\n";
			AlertsUtil.showWarningDialog("Upozorenje", upozorenje, porukaOPogresnomUnosu);
			return false;
		}
		return true;
	}

	private boolean validacijaUlaza() {
		if (!tfNazivTerminala.getText().matches("[a-zA-Z0-9_ ]+") || !tfIdProlaza.getText().matches("[a-zA-Z0-9_ ]+")) {
			String upozorenje = "Nisu dozvoljeni specijalni karakteri!\n";
			AlertsUtil.showWarningDialog("Upozorenje", upozorenje, "Potrebno je da unesete cifre i slova.");
			return false;
		}
		return true;
	}

	public boolean provjeraTerminala() {
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
			e.printStackTrace();
			FileLogger.log(Level.SEVERE, "Greska pri radu sa SOAP servisom!", e);
		}
		return postojiTerminal;
	}

	public void omoguciPrijavu() {
		tfNazivTerminala.setDisable(true);
		btnProvjera.setDisable(true);
		tfKorisnickoIme.setDisable(false);
		tfLozinka.setDisable(false);
		btnPrijava.setDisable(false);
	}

	private String vratiTipKontrole() {
		String tipKontrole = null;
		RadioButton selectedRadioButton = (RadioButton) kontrola.getSelectedToggle();
		String odabraniTip = selectedRadioButton.getText().trim();
		if (odabraniTip.startsWith("policijska"))
			tipKontrole = TipKontrole.POLICIJSKA;
		else
			tipKontrole = TipKontrole.CARINSKA;
		return tipKontrole;
	}

	private boolean popunjeniKredencijali() {
		String porukaOPogresnomUnosu = "";
		if (praznoPolje(tfKorisnickoIme.getText()))
			porukaOPogresnomUnosu += "Unesite korisnicko ime!\n";
		if (praznoPolje(tfLozinka.getText()))
			porukaOPogresnomUnosu += "Unesite lozinku!\n";
		if (porukaOPogresnomUnosu.length() != 0) {
			String upozorenje = "Niste unijeli sva polja!\n";
			AlertsUtil.showWarningDialog("Upozorenje", upozorenje, porukaOPogresnomUnosu);
			return false;
		}
		return true;
	}

	private boolean validacijaKredencijala() {
		if (!tfKorisnickoIme.getText().matches("[a-zA-Z0-9_]+") || !tfLozinka.getText().matches("[a-zA-Z0-9_]+")
				|| !tfIdProlaza.getText().matches("[a-zA-Z0-9_]+")) {
			String upozorenje = "Nisu dozvoljeni specijalni karakteri!\n";
			AlertsUtil.showWarningDialog("Upozorenje", upozorenje, "Potrebno je da unesete cifre i slova.");
			return false;
		}
		return true;
	}

	private boolean provjeriLozinku(KorisnikDTO korisnik) {
		String poruka = KredencijaliServis.provjeraKredencijala(korisnik);
		if ("OK".equals(poruka))
			return true;
		else {
			AlertsUtil.showWarningDialog("Upozorenje", "Ovo je upozorenje!", poruka);
			return false;
		}
	}

	private String vratiIdTerminala() {
		String id = null;
		CentralniRegistarSOAPServiceLocator loc = new CentralniRegistarSOAPServiceLocator();
		try {
			CentralniRegistarSOAP ser = loc.getCentralniRegistarSOAP();
			for (TerminalDTO terminal : ser.terminali()) {
				if (terminal.getNaziv().equals(tfNazivTerminala.getText())) {
					id = terminal.getId();
					break;
				}
			}
		} catch (Exception e) {
			FileLogger.log(Level.SEVERE, "Greska pri radu sa SOAP servisom!", e);
		}
		return id;
	}

	private void omoguciChat() {
		ChatServis.getChatServis().ukljuciChat();
		prijavaNaChat();
	}

	private void prijavaNaChat() {
		String korisnickoIme = tfKorisnickoIme.getText();
		ChatServis.prijavaNaChat(korisnickoIme);
	}

	private void omoguciMulticast() {
		MultiChatServis.omoguciMulticast(Integer.parseInt(idTerminal), tfKorisnickoIme.getText());
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

	private void ukljuciObavjestenja() {
		ObavjestenjeServis.omoguciObavjestenje(Integer.parseInt(idTerminal));
		ObavjestenjeThread ot = new ObavjestenjeThread();
		ot.start();
	}

	private void ucitajStranicu(String tipKontrole) {
		Stage stage = (Stage) btnIzlaz.getScene().getWindow();
		stage.close();
		try {
			if (tipKontrole == TipKontrole.POLICIJSKA)
				FxmlLoader.load(getClass(), "/org/unibl/etf/view/GlavnaStranaPolicija.fxml", "GlavnaStrana");
			else
				FxmlLoader.load(getClass(), "/org/unibl/etf/view/GlavnaStranaCarina.fxml", "GlavnaStrana");
			stage = (Stage) btnPrijava.getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			FileLogger.log(Level.SEVERE, "Greska prilikom ucitavanja stranice!", e);
		}
	}

}
