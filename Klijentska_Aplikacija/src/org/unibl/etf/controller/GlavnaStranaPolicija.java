package org.unibl.etf.controller;

import static org.unibl.etf.controller.Prijava.*;
import static org.unibl.etf.utility.ConstantsUtil.*;

import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.unibl.etf.model.*;
import org.unibl.etf.rmi.potjernice.PotjerniceKlijent;
import org.unibl.etf.service.chat.*;
import org.unibl.etf.service.notification.*;
import org.unibl.etf.soap.centralregistry.*;
import org.unibl.etf.soap.transit.TransitSOAP;
import org.unibl.etf.soap.transit.TransitSOAPServiceLocator;
import org.unibl.etf.utility.*;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.util.Callback;
import javafx.util.Duration;

public class GlavnaStranaPolicija {

	@FXML
	private Label lblNazivTerminala;

	@FXML
	private Label lblProlaz;

	@FXML
	private Label lblNazivKorisnika;

	@FXML
	private Button btnIzlaz;

	@FXML
	private HBox hbObavjestenja;

	@FXML
	private Button btnpromjenaLoznike;

	@FXML
	private Button btnProvjereniPutnici;

	@FXML
	private Button btnProcesiraj;

	@FXML
	private TableView<ProlazakDTO> tvOsobe;

	@FXML
	private TableColumn<ProlazakDTO, String> tcId;

	@FXML
	private TableColumn<ProlazakDTO, String> tcStatus;

	@FXML
	private TableColumn<ProlazakDTO, String> tcDatum;

	@FXML
	private ListView<ChatKorisnikDTO> lvChatKorisnici;

	@FXML
	private TextArea taChatPoruke;

	@FXML
	private TextArea taChat;

	@FXML
	private Button btnChat;

	@FXML
	private ListView<String> lvBroadcast;

	@FXML
	private TextArea taBroadcast;

	@FXML
	private Button btnBroadcast;

	@FXML
	private ListView<String> lvMulticast;

	@FXML
	private TextArea taMulticast;

	@FXML
	private Button btnMulticast;

	private static Label labelaObavjestenje;
	private boolean zauzetaKontrola;
	private static ProlazakDTO aktivanProlazak;
	private ChatThread ct;
	private MultiChatThread mct;
	private HashMap<ChatKorisnikDTO, ArrayList<String>> mapaChatPoruka = new HashMap<>();
	public static ObservableList<ProlazakDTO> listaProlazaka = FXCollections.observableArrayList();

	@FXML
	public void initialize() {

		taChatPoruke.setEditable(false);
		btnProcesiraj.setDisable(true);

		kreirajLabelu();
		popuniTabelu();
		provjeriDostupnostTerminala();
		informacijeOKorisniku();
		prikaziInformacije();
		kreirajNitZaAzuriranje();

		ct = new ChatThread(lvChatKorisnici, taChatPoruke, mapaChatPoruka, lvBroadcast);
		ct.start();

		mct = new MultiChatThread(lvMulticast);
		mct.start();
	}

	@FXML
	void procesiraj(ActionEvent event) {
		btnProcesiraj.setDisable(true);
		TransitSOAPServiceLocator locT = new TransitSOAPServiceLocator();
		CentralniRegistarSOAPServiceLocator locC = new CentralniRegistarSOAPServiceLocator();
		try {
			TransitSOAP serT = locT.getTransitSOAP();
			CentralniRegistarSOAP serC = locC.getCentralniRegistarSOAP();
			zauzetaKontrola = false;
			ispisiObavjestenje("Prolaz je otvoren.");
			serT.promjeniDostupnost(idTerminal, true);
			serT.azurirajStatusKontrole(idTerminal, idProlaz, StatusProlaz.AKTIVAN);
			int indeks = listaProlazaka.indexOf(aktivanProlazak);
			listaProlazaka.set(indeks, aktivanProlazak);
			popuniTabelu();
			ObavjestenjeServis.posaljiObavjestenje(OBAVJESTENJE_OTVOREN);
			PotjerniceKlijent.getPotjerniceKlijent().getPotjernice().ukloniSaPotjernice(aktivanProlazak.getIdOsoba());
			aktivanProlazak.setStatus(StatusProlazak.ZAVRSEN);
			serT.azurirajProlazak(idProlaz, aktivanProlazak);
			ObavjestenjeServis.azurirajInformacije(aktivanProlazak, StatusProlazak.ZAVRSEN);
			serC.evidentirajProlazak(aktivanProlazak);
			aktivanProlazak = null;

		} catch (Exception e) {
			FileLogger.log(Level.SEVERE, "Greska pri radu sa SOAP servisom!", e);
		}
	}

	@FXML
	void evidencijaProvjernihPutnika(ActionEvent event) {
		FxmlLoader.load(getClass(), "/org/unibl/etf/view/EvidencijaPutnika.fxml", "Evidencija putnika");
	}

	@FXML
	void promjenaLoznike(ActionEvent event) {
		FxmlLoader.load(getClass(), "/org/unibl/etf/view/PromjenaLozinke.fxml", "Promjena lozinke");
	}

	@FXML
	void izadji(ActionEvent event) {
		odjaviKorisnika(aktivniKorisnik);
		ChatServis.getChatServis().odjaviSeIzChatListe();
		ChatServis.getChatServis().prekiniChat();
		MultiChatServis.zatvoriMulticastSocket();
		Platform.exit();
		System.exit(0);
	}

	@FXML
	void posaljiChatPoruku(ActionEvent event) {
		if (taChat.getText().trim().isEmpty()) {
			String upozorenje = "Niste unijeli tekst poruke!\n";
			AlertsUtil.showWarningDialog("Upozorenje", upozorenje, "Napisite poruku.");
		} else if (!lvChatKorisnici.getSelectionModel().isEmpty()) {
			String korisnickoImePosiljaoca = aktivniKorisnik.getKorisnickoIme();
			String korisnickoImePrimaoca = lvChatKorisnici.getSelectionModel().getSelectedItem().getKorisnickoIme();
			String tekstPoruke = taChat.getText().trim();
			String trenutnoVrijeme = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).toString().replace("T", "_");
			String poruka = korisnickoImePosiljaoca + " (" + trenutnoVrijeme + "):\t" + tekstPoruke;
			ChatServis.posaljiChatPoruku(korisnickoImePrimaoca, poruka);
			ChatKorisnikDTO chatKorisnik = new ChatKorisnikDTO(korisnickoImePrimaoca);
			if (mapaChatPoruka.containsKey(chatKorisnik)) {
				mapaChatPoruka.get(chatKorisnik).add(poruka);
			} else {
				ArrayList<String> listaPoruka = new ArrayList<>();
				listaPoruka.add(poruka);
				mapaChatPoruka.put(chatKorisnik, listaPoruka);
			}
			taChat.clear();
			ChatServis.getChatServis().prikaziDetalje(chatKorisnik, taChatPoruke, mapaChatPoruka);
		} else {
			String upozorenje = "Niste odabrali sagovornika!\n";
			AlertsUtil.showWarningDialog("Upozorenje", upozorenje, "Selektujte sagovornika");
			taChat.clear();
		}
	}

	@FXML
	void posaljiMulticast(ActionEvent event) {
		if (taMulticast.getText().trim().isEmpty()) {
			String upozorenje = "Niste unijeli tekst poruke!\n";
			AlertsUtil.showWarningDialog("Upozorenje", upozorenje, "Napisite poruku.");
		} else {
			String poruka = taMulticast.getText().trim();
			if (poruka.length() != 0) {
				MultiChatServis.posaljiMulticastPoruku(poruka);
				taMulticast.clear();
			}
		}
	}

	@FXML
	void posaljiBroadcast(ActionEvent event) {
		if (taBroadcast.getText().trim().isEmpty()) {
			String upozorenje = "Niste unijeli tekst poruke!\n";
			AlertsUtil.showWarningDialog("Upozorenje", upozorenje, "Napisite poruku.");
		} else {
			String tekstPoruke = taBroadcast.getText().trim();
			ChatServis.posaljiBroadcastPoruku(tekstPoruke);
			taBroadcast.clear();
		}
	}

	private void kreirajLabelu() {
		labelaObavjestenje = new Label();
		labelaObavjestenje.setText("Nema obavjestenja");
		labelaObavjestenje.setTextFill(Color.AZURE);
		labelaObavjestenje.setFont(Font.font("System Bold Italic", FontPosture.ITALIC, 18));
		hbObavjestenja.getChildren().add(labelaObavjestenje);
	}

	private void popuniTabelu() {

		tvOsobe.setEditable(true);

		tcId.setCellValueFactory(new Callback<CellDataFeatures<ProlazakDTO, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<ProlazakDTO, String> p) {
				return new SimpleStringProperty(p.getValue().getIdOsoba());
			}
		});

		tcDatum.setCellValueFactory(new Callback<CellDataFeatures<ProlazakDTO, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<ProlazakDTO, String> p) {
				return new SimpleStringProperty(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).toString());
			}
		});

		tcStatus.setCellValueFactory(new Callback<CellDataFeatures<ProlazakDTO, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<ProlazakDTO, String> p) {
				return new SimpleStringProperty(p.getValue().getStatus());
			}
		});

		tvOsobe.setItems(listaProlazaka);
	}

	private void odjaviKorisnika(KorisnikDTO korisnik) {
		CentralniRegistarSOAPServiceLocator loc = new CentralniRegistarSOAPServiceLocator();
		try {
			CentralniRegistarSOAP ser = loc.getCentralniRegistarSOAP();
			ser.postaviStatus(korisnik.getKorisnickoIme(), false);
		} catch (Exception e) {
			FileLogger.log(Level.SEVERE, "Greska pri radu sa SOAP servisom!", e);
		}
	}

	private void informacijeOKorisniku() {
		lblNazivTerminala.setText(nazivTerminala);
		lblProlaz.setText(aktivniKorisnik.getIdProlaz());
		lblNazivKorisnika.setText(aktivniKorisnik.getKorisnickoIme());
		if (otvorenTerminal)
			ispisiObavjestenje("Terminal je otvoren");
		else
			ispisiObavjestenje("Terminal je zatvoren");
	}

	private void prikaziInformacije() {
		ChatServis.getChatServis().prikaziDetalje(null, taChatPoruke, mapaChatPoruka);
		lvChatKorisnici.getSelectionModel().selectedItemProperty().addListener((observable, oldValue,
				newValue) -> ChatServis.getChatServis().prikaziDetalje(newValue, taChatPoruke, mapaChatPoruka));
	}

	private void kreirajNitZaAzuriranje() {
		ScheduledThreadPoolExecutor executor;
		Thread radnaNit = new Thread() {
			@Override
			public void run() {
				if (otvorenTerminal && !zauzetaKontrola)
					aktivirajPolicijskuKontrolu();
			}
		};
		radnaNit.setDaemon(true);
		executor = new ScheduledThreadPoolExecutor(1);
		executor.scheduleAtFixedRate(radnaNit, 1, 5, TimeUnit.SECONDS);
	}

	private void aktivirajPolicijskuKontrolu() {
		TransitSOAPServiceLocator locT = new TransitSOAPServiceLocator();
		CentralniRegistarSOAPServiceLocator locC = new CentralniRegistarSOAPServiceLocator();
		try {
			TransitSOAP serT = locT.getTransitSOAP();
			CentralniRegistarSOAP serC = locC.getCentralniRegistarSOAP();
			aktivanProlazak = serT.provjeriOsobu(idProlaz, StatusProlazak.POLICIJSKA_KONTROLA);
			if (aktivanProlazak != null) {
				zauzetaKontrola = true;
				listaProlazaka.add(aktivanProlazak);
				if (naPotjernici(aktivanProlazak.getIdOsoba())) {
					btnProcesiraj.setDisable(false);
					aktivanProlazak.setStatus(StatusProlazak.NA_POTJERNICI);
					serT.azurirajProlazak(idProlaz, aktivanProlazak);
					ObavjestenjeServis.azurirajInformacije(aktivanProlazak, StatusProlazak.NA_POTJERNICI);
					int indeks = listaProlazaka.indexOf(aktivanProlazak);
					listaProlazaka.set(indeks, aktivanProlazak);
					popuniTabelu();
					serT.promjeniDostupnost(idTerminal, false);
					ispisiObavjestenje("Terminal je zatvoren.");
					ObavjestenjeServis.posaljiObavjestenje(OBAVJESTENJE_ZATVOREN);
					serC.evidentirajPotjernicu(aktivanProlazak);
				} else {
					ObavjestenjeServis.azurirajInformacije(aktivanProlazak, StatusProlazak.NIJE_NA_POTJERNICI);
					serT.azurirajStatusKontrole(idTerminal, idProlaz, StatusProlaz.AKTIVAN);
					aktivanProlazak.setStatus(StatusProlazak.CARINSKA_KONTROLA);
					serT.azurirajProlazak(idProlaz, aktivanProlazak);
					ObavjestenjeServis.azurirajInformacije(aktivanProlazak, StatusProlazak.CARINSKA_KONTROLA);
					int indeks = listaProlazaka.indexOf(aktivanProlazak);
					listaProlazaka.set(indeks, aktivanProlazak);
					popuniTabelu();
					zauzetaKontrola = false;
					aktivanProlazak = null;
				}
			}
		} catch (Exception e) {
			FileLogger.log(Level.SEVERE, "Greska pri radu sa SOAP servisom!", e);
		}

	}

	private boolean naPotjernici(String idOsoba) {
		boolean naPotjernici = false;
		try {
			naPotjernici = PotjerniceKlijent.getPotjerniceKlijent().getPotjernice().naPotjernici(idOsoba);
		} catch (RemoteException ex) {
			FileLogger.log(Level.SEVERE, "Greska pri koriscenju RMI servera.", ex);
		}
		return naPotjernici;
	}

	public static void ispisiObavjestenje(String poruka) {
		final KeyFrame kf = new KeyFrame(Duration.seconds(2), e -> labelaObavjestenje.setText(poruka));
		final Timeline timeline = new Timeline(kf);
		Platform.runLater(timeline::play);
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

}
