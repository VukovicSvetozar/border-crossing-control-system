package org.unibl.etf.controller;

import java.util.logging.Level;

import org.unibl.etf.utility.*;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Prijava {

	@FXML
	private TextField txtKorisnickoIme;

	@FXML
	private PasswordField txtLozinka;

	@FXML
	private Button btnPrijava;

	@FXML
	private Button btnIzlaz;

	public static String imeAdministratora;

	@FXML
	void prijava(ActionEvent event) {
		String korisnickoIme = txtKorisnickoIme.getText();
		String lozinka = txtLozinka.getText();
		if (popunjenaPolja() && validacijaUlaza() && provjeriLozinku(korisnickoIme, lozinka)) {
			imeAdministratora = txtKorisnickoIme.getText();
			ucitajStranicu();
		}
	}

	@FXML
	void izadji(ActionEvent event) {
		Platform.exit();
	}

	private boolean popunjenaPolja() {
		String porukaOPogresnomUnosu = "";
		if (praznoPolje(txtKorisnickoIme.getText()))
			porukaOPogresnomUnosu += "Unesite korisnicko ime!\n";
		if (praznoPolje(txtLozinka.getText()))
			porukaOPogresnomUnosu += "Unesite lozinku!\n";
		if (porukaOPogresnomUnosu.length() != 0) {
			String upozorenje = "Niste unijeli sva polja!\n";
			AlertsUtil.showWarningDialog("Upozorenje", upozorenje, porukaOPogresnomUnosu);
			return false;
		}
		return true;
	}

	private boolean validacijaUlaza() {
		if (!txtKorisnickoIme.getText().matches("[a-zA-Z0-9_ ]+")) {
			String upozorenje = "Nisu dozvoljeni specijalni karakteri!\n";
			AlertsUtil.showWarningDialog("Upozorenje", upozorenje, "Potrebno je da unesete cifre i slova.");
			return false;
		}
		if (!txtLozinka.getText().matches("[a-zA-Z0-9_]+")) {
			String upozorenje = "Nisu dozvoljeni specijalni karakteri!\n";
			AlertsUtil.showWarningDialog("Upozorenje", upozorenje, "Potrebno je da unesete cifre i slova.");
			return false;
		}
		return true;
	}

	private boolean provjeriLozinku(String korisnickoIme, String unesenaLozinka) {
		String zasticenaLozinka = PropertiesUtil.vratiSvojstvo("ZASTICENA_LOZINKA_ADMIN", String.class);
		String salt = PropertiesUtil.vratiSvojstvo("SALT_ADMIN", String.class);
		if (CryptographyUtil.verifikacijaKorisnickeLozinke(unesenaLozinka, zasticenaLozinka, salt))
			return true;
		String upozorenje = "Uneseno je pogresno korisnicko ime ili lozinka!";
		AlertsUtil.showWarningDialog("Upozorenje", "Ovo je upozorenje!", upozorenje);
		return false;
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

	private static boolean praznoPolje(String unos) {
		if (unos == null)
			return true;
		return unos.trim().length() == 0;
	}

}
