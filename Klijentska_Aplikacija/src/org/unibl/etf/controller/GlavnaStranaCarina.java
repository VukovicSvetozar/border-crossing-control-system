package org.unibl.etf.controller;

import static org.unibl.etf.controller.Prijava.*;
import static org.unibl.etf.utility.ConstantsUtil.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.unibl.etf.model.*;
import org.unibl.etf.rmi.file.FileInterface;
import org.unibl.etf.service.chat.*;
import org.unibl.etf.service.notification.ObavjestenjeServis;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

public class GlavnaStranaCarina {

	@FXML
	private Label lblNazivTerminala;

	@FXML
	private Label lblProlaz;

	@FXML
	private Label lblNazivKorisnika;

	@FXML
	private Button btnIzlaz;

	@FXML
	private Button btnOdaberiFajl;

	@FXML
	private HBox hbObavjestenja;

	@FXML
	private Button btnpromjenaLoznike;

	@FXML
	private Button btnObrisiPodatke;

	@FXML
	private TextArea taOdabraniFajlovi;

	@FXML
	private Button btnProvjereniPutnici;

	@FXML
	private Button btnPosaljiFajl;

	@FXML
	private TableView<ProlazakDTO> tvOsobe;

	@FXML
	private TableColumn<ProlazakDTO, String> tcId;

	@FXML
	private TableColumn<ProlazakDTO, String> tcStatus;

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
	private ChatThread ct;
	private MultiChatThread mct;
	private FileInterface fi;
	private boolean deaktiviranaDugmad;

	public static ObservableList<ProlazakDTO> listaProlazaka = FXCollections.observableArrayList();
	private HashMap<ChatKorisnikDTO, ArrayList<String>> mapaChatPoruka = new HashMap<>();

	@FXML
	public void initialize() {

		taChatPoruke.setEditable(false);

		kreirajLabelu();
		popuniTabelu();
		informacijeOKorisniku();
		provjeriDostupnostTerminala();
		prikaziInformacije();
		omogucislanjeFajlova();
		ucitajOdabraneFajlove();
		kreirajNitZaAzuriranje();

		ct = new ChatThread(lvChatKorisnici, taChatPoruke, mapaChatPoruka, lvBroadcast);
		ct.start();

		mct = new MultiChatThread(lvMulticast);
		mct.start();
	}

	@FXML
	void odaberiFajl(ActionEvent event) {
		ProlazakDTO odabraniProlazak = odaberiStavku();
		if (odabraniProlazak != null) {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Odaberite dokument");
			fileChooser.getExtensionFilters().addAll(
					new ExtensionFilter("Dozvoljene ekstenzije", "*.csv", "*.txt", "*.pdf", "*.docx", "*.xlsx"));
			fileChooser.setInitialDirectory(new File(POCETNI_DIREKTORIJUM + File.separator + RESURSI_DIREKTORIJUM
					+ File.separator + DOKUMENTI_DIREKTORIJUM));
			String odabraniFajl = fileChooser.showOpenDialog((Stage) btnOdaberiFajl.getScene().getWindow()).toString();
			for (int i = 0; i < odabraniProlazak.getOdabraniFajlovi().length; i++)
				if (odabraniProlazak.getOdabraniFajlovi()[i] == null) {
					odabraniProlazak.getOdabraniFajlovi()[i] = odabraniFajl;
					break;
				}
			prikaziOdabraneFajlove(odabraniProlazak);
		}
	}

	@FXML
	void obrisiPodatke(ActionEvent event) {
		ProlazakDTO odabraniProlazak = odaberiStavku();
		if (odabraniProlazak != null) {
			Arrays.fill(odabraniProlazak.getOdabraniFajlovi(), null);
			prikaziOdabraneFajlove(odabraniProlazak);
		}
	}

	@FXML
	void posaljiFajl(ActionEvent event) {
		ProlazakDTO odabraniProlazak = odaberiStavku();
		if (odabraniProlazak != null) {
			try {
				long brojDokumenata = Arrays.stream(odabraniProlazak.getOdabraniFajlovi()).filter(f -> f != null)
						.count();
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Dijalog potvrde");
				alert.setHeaderText("Zelite da procesirate odabrani prolazak!");
				String info = "";
				if (brojDokumenata == 0)
					info = "Prolazak nema dokumenta.";
				else
					info = "Posalji dokumente.";
				alert.setContentText(info);
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK) {
					if (brojDokumenata != 0) {
						String imeDatoteke = odabraniProlazak.getId();
						File zipFile = new File(POCETNI_DIREKTORIJUM + File.separator + RESURSI_DIREKTORIJUM
								+ File.separator + DOKUMENTI_DIREKTORIJUM_ZIP + File.separator + imeDatoteke + ".zip");
						kompresujDokumente(odabraniProlazak, zipFile);
						String rezultat = fi.slanjeFajla(imeDatoteke, zipFile.getAbsoluteFile());
						if ("OK".equals(rezultat)) {
							String obavjestenje = "Obavjestenje!\n";
							if (brojDokumenata == 1)
								AlertsUtil.showInfoDialog("Info", obavjestenje, "Odabrani fajl je poslat.");
							else
								AlertsUtil.showInfoDialog("Info", obavjestenje, "Odabrani fajlovi su poslati.");
						} else {
							String upozorenje = "Upozorenje!\n";
							AlertsUtil.showWarningDialog("Upozorenje", upozorenje, "Nije moguce poslati fajl.");
						}
					}

					prikaziOdabraneFajlove(odabraniProlazak);

					TransitSOAPServiceLocator locT = new TransitSOAPServiceLocator();
					CentralniRegistarSOAPServiceLocator locC = new CentralniRegistarSOAPServiceLocator();
					try {
						TransitSOAP serT = locT.getTransitSOAP();
						CentralniRegistarSOAP serC = locC.getCentralniRegistarSOAP();
						odabraniProlazak.setStatus(StatusProlazak.ZAVRSEN);
						serT.azurirajProlazak(idProlaz, odabraniProlazak);
						serC.evidentirajProlazak(odabraniProlazak);
						ObavjestenjeServis.azurirajInformacije(odabraniProlazak, StatusProlazak.ZAVRSEN);
						int indeks = listaProlazaka.indexOf(odabraniProlazak);
						listaProlazaka.set(indeks, odabraniProlazak);
						String[] dokumenti = Arrays.stream(odabraniProlazak.getOdabraniFajlovi()).filter(f -> f != null)
								.toArray(String[]::new);
						odabraniProlazak.setOdabraniFajlovi(dokumenti);
						serC.evidentirajDokumente(odabraniProlazak);
						popuniTabelu();
						Arrays.fill(odabraniProlazak.getOdabraniFajlovi(), null);
					} catch (Exception e) {
						FileLogger.log(Level.SEVERE, "Greska pri radu sa SOAP servisom!", e);
					}
				}
			} catch (RemoteException e) {
				FileLogger.log(Level.SEVERE, "Greska pri koriscenju RMI servera.", e);
			}
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
		obrisiZipovaneDokumente();
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

	public static void ispisiObavjestenje(String poruka) {
		final KeyFrame kf = new KeyFrame(Duration.seconds(3), e -> labelaObavjestenje.setText(poruka));
		final Timeline timeline = new Timeline(kf);
		Platform.runLater(timeline::play);
	}

	private void kreirajNitZaAzuriranje() {
		ScheduledThreadPoolExecutor executor;
		Thread radnaNit = new Thread() {
			@Override
			public void run() {
				if (otvorenTerminal)
					aktivirajCarinskuKontrolu();
				else
					onemoguciRadSaDokumentacijom();
			}
		};
		radnaNit.setDaemon(true);
		executor = new ScheduledThreadPoolExecutor(1);
		executor.scheduleAtFixedRate(radnaNit, 1, 5, TimeUnit.SECONDS);
	}

	private void aktivirajCarinskuKontrolu() {
		if (deaktiviranaDugmad)
			omoguciRadSaDokumentacijom();
		TransitSOAPServiceLocator locT = new TransitSOAPServiceLocator();
		try {
			TransitSOAP serT = locT.getTransitSOAP();
			ProlazakDTO aktivanProlazak = serT.provjeriOsobu(idProlaz, StatusProlazak.CARINSKA_KONTROLA);
			if (aktivanProlazak != null) {
				listaProlazaka.add(aktivanProlazak);
				aktivanProlazak.setStatus(StatusProlazak.DOKUMENTACIJA);
				serT.azurirajProlazak(idProlaz, aktivanProlazak);
				ObavjestenjeServis.azurirajInformacije(aktivanProlazak, StatusProlazak.DOKUMENTACIJA);
				popuniTabelu();
			}
		} catch (Exception e) {
			FileLogger.log(Level.SEVERE, "Greska pri radu sa SOAP servisom!", e);
		}
	}

	private ProlazakDTO odaberiStavku() {
		ProlazakDTO odabraniProlazak = null;
		if (tvOsobe.getSelectionModel().getSelectedItem() == null) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Upozorenje");
			alert.setHeaderText("Niste odabrali ni jednu stavku!");
			alert.setContentText("Odaberite stavku.");
			alert.showAndWait();
		} else {
			String status = tvOsobe.getSelectionModel().getSelectedItem().getStatus();
			if (!StatusProlazak.DOKUMENTACIJA.equals(status)) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Upozorenje");
				alert.setHeaderText("Odabrali ste dokumentovan prolazak!");
				alert.setContentText("Odaberite drugu stavku.");
				alert.showAndWait();
			} else {
				odabraniProlazak = tvOsobe.getSelectionModel().getSelectedItem();
			}
		}
		return odabraniProlazak;
	}

	private void omogucislanjeFajlova() {
		System.setProperty("java.security.policy", POCETNI_DIREKTORIJUM + File.separator + RESURSI_DIREKTORIJUM
				+ File.separator + POLICY_DIREKTORIJUM + File.separator + POLICY_DATOTEKA);
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		try {
			Registry registry = LocateRegistry.getRegistry(10099);
			fi = (FileInterface) registry.lookup("FileA");

		} catch (Exception ex) {
			FileLogger.log(Level.SEVERE, "Greska pri radu sa rmi.", ex);
		}
	}

	private void ucitajOdabraneFajlove() {
		prikaziOdabraneFajlove(null);
		tvOsobe.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> prikaziOdabraneFajlove(newValue));
	}

	private void prikaziOdabraneFajlove(ProlazakDTO prolazak) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (prolazak != null) {
					String spisakFajlova = Arrays.stream(prolazak.getOdabraniFajlovi()).filter(f -> f != null)
							.map(s -> s.substring(s.lastIndexOf("\\") + 1, s.length() - 1))
							.collect(Collectors.joining("\n"));
					if (spisakFajlova != null)
						taOdabraniFajlovi.setText(spisakFajlova);
					else
						taOdabraniFajlovi.setText("Bez dokumenata.");
				} else
					taOdabraniFajlovi.setText("");
			}
		});
	}

	private void kompresujDokumente(ProlazakDTO odabraniProlazak, File zipFile) {
		try {
			ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
			for (String file : odabraniProlazak.getOdabraniFajlovi()) {
				if (file == null)
					break;
				File fileToZip = new File(file);
				FileInputStream fis = new FileInputStream(fileToZip);
				ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
				zipOut.putNextEntry(zipEntry);
				byte[] bytes = new byte[1024];
				int length;
				while ((length = fis.read(bytes)) >= 0) {
					zipOut.write(bytes, 0, length);
				}
				fis.close();
			}
			zipOut.close();
		} catch (FileNotFoundException e) {
			FileLogger.log(Level.SEVERE, "Putanja nije pronadjena.", e);
		} catch (IOException e) {
			FileLogger.log(Level.SEVERE, "Greska pri radu sa I/O.", e);
		}
	}

	private void onemoguciRadSaDokumentacijom() {
		btnOdaberiFajl.setDisable(true);
		btnObrisiPodatke.setDisable(true);
		btnPosaljiFajl.setDisable(true);
		deaktiviranaDugmad = true;
	}

	private void omoguciRadSaDokumentacijom() {
		btnOdaberiFajl.setDisable(false);
		btnObrisiPodatke.setDisable(false);
		btnPosaljiFajl.setDisable(false);
		deaktiviranaDugmad = false;
	}

	private void obrisiZipovaneDokumente() {
		File zipFolder = new File(POCETNI_DIREKTORIJUM + File.separator + RESURSI_DIREKTORIJUM + File.separator
				+ DOKUMENTI_DIREKTORIJUM_ZIP);
		Arrays.stream(Objects.requireNonNull(zipFolder.listFiles())).filter(f -> f.isFile()).forEach(File::delete);
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
