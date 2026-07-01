package org.unibl.etf.controller;

import org.unibl.etf.utility.AlertsUtil;
import org.unibl.etf.utility.CryptographyUtil;
import org.unibl.etf.utility.RedisUtil;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import static org.unibl.etf.controller.Prijava.aktivniKorisnik;
import static org.unibl.etf.utility.ConstantsUtil.*;

import org.unibl.etf.model.KredencijaliDTO;

public class PromjenaLozinke {

	@FXML
	private PasswordField pfStaraLozinka;

	@FXML
	private PasswordField pfNovaLozinka;

	@FXML
	private PasswordField pfPotvrdaLozinke;

	@FXML
	private Button btnPromjeni;

	@FXML
	private Button btnIzlaz;

	@FXML
	void promjena(ActionEvent event) {
		if (popunjenaPolja() && validacijaUlaza() && provjeriStaruLozinku()) {
			promjeniLozinku();
			final Node source = (Node) event.getSource();
			final Stage stage = (Stage) source.getScene().getWindow();
			stage.close();
		}
	}

	@FXML
	void odustani(ActionEvent event) {
		Stage stage = (Stage) btnIzlaz.getScene().getWindow();
		stage.close();
	}

	private boolean popunjenaPolja() {
		String porukaOPogresnomUnosu = "";
		if (praznoPolje(pfStaraLozinka.getText()))
			porukaOPogresnomUnosu += "Unesite staru lozinku!\n";
		if (praznoPolje(pfNovaLozinka.getText()))
			porukaOPogresnomUnosu += "Unesite novu lozinku!\n";
		if (praznoPolje(pfPotvrdaLozinke.getText()))
			porukaOPogresnomUnosu += "Potvrdite novu lozinku!\n";
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
		if (!pfStaraLozinka.getText().matches("[a-zA-Z0-9_ ]+")) {
			String upozorenje = "Nisu dozvoljeni specijalni karakteri!\n";
			AlertsUtil.showWarningDialog("Upozorenje", upozorenje, "Potrebno je da unesete cifre i slova.");
			return false;
		}
		if (!pfNovaLozinka.getText().matches("[a-zA-Z0-9_]+")) {
			String upozorenje = "Nisu dozvoljeni specijalni karakteri!\n";
			AlertsUtil.showWarningDialog("Upozorenje", upozorenje, "Potrebno je da unesete cifre i slova.");
			return false;
		}
		if (!pfPotvrdaLozinke.getText().matches("[a-zA-Z0-9_]+")) {
			String upozorenje = "Nisu dozvoljeni specijalni karakteri!\n";
			AlertsUtil.showWarningDialog("Upozorenje", upozorenje, "Potrebno je da unesete cifre i slova.");
			return false;
		}
		if (!pfNovaLozinka.getText().equals(pfPotvrdaLozinke.getText())) {
			String upozorenje = "Vrijednosti nove lozinke i potvrde se ne poklapaju!\n";
			AlertsUtil.showWarningDialog("Upozorenje", upozorenje, "Pokusajte ponovo unijeti vrijednosti.");
			return false;
		}
		return true;
	}

	private boolean provjeriStaruLozinku() {
		boolean uspjesnaProvjera = false;
		System.out.println(pfStaraLozinka.getText());
		KredencijaliDTO kredencijali = RedisUtil.getInstance().restoreObject(
				REDIS_KLJUC_KREDENCIJAL + REDIS_KLJUC_SEPARATOR + aktivniKorisnik.getKorisnickoIme(),
				KredencijaliDTO.class);
		String salt = kredencijali.getSalt();
		if (CryptographyUtil.verifikacijaKorisnickeLozinke(pfStaraLozinka.getText(),
				kredencijali.getEnkodovanaLozinka(), salt)) {
			uspjesnaProvjera = true;
		} else {
			String upozorenje = "Unesena je pogresna stara lozinka!";
			AlertsUtil.showWarningDialog("Upozorenje", "Ovo je upozorenje!", upozorenje);
		}
		return uspjesnaProvjera;
	}

	private void promjeniLozinku() {
		KredencijaliDTO kredencijali = RedisUtil.getInstance().restoreObject(
				REDIS_KLJUC_KREDENCIJAL + REDIS_KLJUC_SEPARATOR + aktivniKorisnik.getKorisnickoIme(),
				KredencijaliDTO.class);
		String salt = kredencijali.getSalt();
		kredencijali
				.setEnkodovanaLozinka(CryptographyUtil.getSigurnaLozinka(new String(pfNovaLozinka.getText()), salt));
		RedisUtil.getInstance().storeObject(
				REDIS_KLJUC_KREDENCIJAL + REDIS_KLJUC_SEPARATOR + kredencijali.getKorisnickoIme(), kredencijali);
	}

}
