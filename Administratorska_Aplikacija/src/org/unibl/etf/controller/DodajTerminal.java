package org.unibl.etf.controller;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

import org.unibl.etf.dao.DAOFactory;
import org.unibl.etf.model.*;
import org.unibl.etf.soap.centralregistry.*;
import org.unibl.etf.utility.*;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.Callback;

public class DodajTerminal {

	@FXML
	private TextField tfNaziv;

	@FXML
	private ChoiceBox<String> cbTipProlaza;

	@FXML
	private TextField tfIdProlaza;

	@FXML
	private Button btnDodajProlaz;

	@FXML
	private TableView<KorisnikDTO> tvKorisnici;

	@FXML
	private TableColumn<KorisnikDTO, String> tcId;

	@FXML
	private TableColumn<KorisnikDTO, String> tcTipProlaza;

	@FXML
	private TableColumn<KorisnikDTO, String> tcTipKontrole;

	@FXML
	private TableColumn<KorisnikDTO, String> tcKorisnickoIme;

	@FXML
	private TableColumn<KorisnikDTO, String> tcAkcija;

	@FXML
	private Button btnOk;

	@FXML
	private Button btnOtkazi;

	private static AtomicInteger brojac;
	private String idTerminal;
	private List<ProlazDTO> listaProlaza;
	private ObservableList<KorisnikDTO> listaKorisnika;

	@FXML
	void initialize() {
		inicijalizujSvojstva();
		kreirajChoiceBoxZaTipProlaza();
		kreirajTabelu();
		kreirajIdTerminala();
	}

	@FXML
	void dodajProlaz(ActionEvent event) {
		if (popunjenaPoljaZaProlaz()) {
			String tipProlaza = cbTipProlaza.getSelectionModel().getSelectedItem();
			String idProlaz = tfIdProlaza.getText();

			KorisnikDTO korisnik1 = new KorisnikDTO(false, idProlaz, "", "", TipKontrole.POLICIJSKA);
			listaKorisnika.add(korisnik1);
			KorisnikDTO korisnik2 = new KorisnikDTO(false, idProlaz, "", "", TipKontrole.CARINSKA);
			listaKorisnika.add(korisnik2);

			KorisnikDTO[] korisnici = { korisnik1, korisnik2 };
			ProlazDTO prolaz = new ProlazDTO(idProlaz, idTerminal, korisnici, tipProlaza);
			listaProlaza.add(prolaz);

			tfIdProlaza.clear();
		}
	}

	@FXML
	void potvrdi(ActionEvent event) {

		if (popunjenaPoljaZaTerminal() && provjeraKorisnickihImena()) {

			for (KorisnikDTO tKorisnik : tvKorisnici.getItems()) {
				String korisnickoIme = tKorisnik.getKorisnickoIme();
				String salt = CryptographyUtil.getSalt(30);
				String lozinka = new StringBuilder(korisnickoIme).reverse().toString();
				String enkodovanaLozinka = CryptographyUtil.getSigurnaLozinka(lozinka, salt);
				KredencijaliDTO kredencijali = new KredencijaliDTO(korisnickoIme, enkodovanaLozinka, salt);
				DAOFactory.getDAOFactory().getKredencijaliDAO().dodajKredencijale(kredencijali);
			}

			String naziv = tfNaziv.getText();
			ProlazDTO[] nizProlaza = listaProlaza.toArray(new ProlazDTO[0]);
			TerminalDTO terminal = new TerminalDTO(idTerminal, naziv, nizProlaza);
			CentralniRegistarSOAPServiceLocator loc = new CentralniRegistarSOAPServiceLocator();
			try {
				CentralniRegistarSOAP ser = loc.getCentralniRegistarSOAP();
				ser.dodajTerminal(terminal);
			} catch (Exception e) {
				FileLogger.log(Level.SEVERE, "Greska u radu sa SOAP servisom!", e);

			}
			GlavnaStrana.ucitajTerminale();
			GlavnaStrana.popuniTabelu();

			final Node source = (Node) event.getSource();
			final Stage stage = (Stage) source.getScene().getWindow();
			stage.close();
		}

	}

	@FXML
	void otkazi(ActionEvent event) {
		Stage stage = (Stage) btnOtkazi.getScene().getWindow();
		stage.close();
	}

	private void inicijalizujSvojstva() {
		GlavnaStrana.ucitajTerminale();
		int maksimalanId = Integer.parseInt(
				GlavnaStrana.getTerminali().stream().map(t -> t.getId()).max(Comparator.naturalOrder()).get());
		brojac = new AtomicInteger(maksimalanId);
		listaProlaza = new ArrayList<>();
		listaKorisnika = FXCollections.observableArrayList();
	}

	private void kreirajChoiceBoxZaTipProlaza() {
		cbTipProlaza.getItems().add(TipProlaza.ULAZ);
		cbTipProlaza.getItems().add(TipProlaza.IZLAZ);
	}

	private void kreirajTabelu() {

		tvKorisnici.setEditable(true);

		tcId.setCellValueFactory(new Callback<CellDataFeatures<KorisnikDTO, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<KorisnikDTO, String> p) {
				return new SimpleStringProperty(p.getValue().getIdProlaz());
			}
		});

		tcKorisnickoIme.setCellValueFactory(new PropertyValueFactory<KorisnikDTO, String>("korisnickoIme"));
		tcKorisnickoIme.setCellFactory(TextFieldTableCell.forTableColumn());
		tcKorisnickoIme.setOnEditCommit(new EventHandler<CellEditEvent<KorisnikDTO, String>>() {
			@Override
			public void handle(CellEditEvent<KorisnikDTO, String> t) {
				((KorisnikDTO) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setKorisnickoIme(t.getNewValue());
			}
		});

		tcTipProlaza
				.setCellValueFactory(new Callback<CellDataFeatures<KorisnikDTO, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<KorisnikDTO, String> p) {
						return new SimpleStringProperty(vratiTipProlaza(p.getValue().getIdProlaz()));
					}
				});

		tcTipKontrole
				.setCellValueFactory(new Callback<CellDataFeatures<KorisnikDTO, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<KorisnikDTO, String> p) {
						return new SimpleStringProperty(p.getValue().getTipKontrole());
					}
				});

		tcAkcija.setCellValueFactory(new PropertyValueFactory<>("akcija"));
		Callback<TableColumn<KorisnikDTO, String>, TableCell<KorisnikDTO, String>> cellFactory = param -> {
			final TableCell<KorisnikDTO, String> cell = new TableCell<KorisnikDTO, String>() {
				Button btn = new Button("Ukloni");
				@Override
				public void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setGraphic(null);
					} else {
						btn.setOnAction(event -> {
							String idProlaz = getTableView().getItems().get(getIndex()).getIdProlaz();
							getTableView().getItems().removeIf(k -> (k.getIdProlaz().equals(idProlaz)));
							listaProlaza.removeIf(p -> (p.getId().equals(idProlaz)));
						});
						setGraphic(btn);
					}
				}
			};
			return cell;
		};

		tcAkcija.setCellFactory(cellFactory);

		tvKorisnici.setItems(listaKorisnika);
	}

	private void kreirajIdTerminala() {
		idTerminal = String.format("%03d", brojac.incrementAndGet());
	}

	private boolean popunjenaPoljaZaTerminal() {
		String porukaOPogresnomUnosu = "";

		if (praznoPolje(tfNaziv.getText()))
			porukaOPogresnomUnosu += "Unesite naziv terminala!\n";
		if (listaProlaza.isEmpty())
			porukaOPogresnomUnosu += "Lista korisnika je prazna!\n";
		if (porukaOPogresnomUnosu.length() != 0) {
			String upozorenje = "Niste unijeli sva polja!\n";
			AlertsUtil.showWarningDialog("Upozorenje", upozorenje, porukaOPogresnomUnosu);
			return false;
		}

		if (ponovljenNazivTerminala(tfNaziv.getText())) {
			String upozorenje = "Naziv terminala je zauzet!\n";
			AlertsUtil.showWarningDialog("Upozorenje", upozorenje, "Odaberite drugi naziv terminala.");
			return false;
		}
		return true;
	}

	private boolean ponovljenNazivTerminala(String noviNazivTerminala) {
		ObservableList<TerminalDTO> terminali = GlavnaStrana.getTerminali();
		Set<String> naziviTerminala = new HashSet<String>();
		for (TerminalDTO terminal : terminali)
			naziviTerminala.add(terminal.getNaziv());
		return !naziviTerminala.add(noviNazivTerminala);
	}

	private boolean provjeraKorisnickihImena() {
		boolean uspjesno = true;
		ObservableList<String> listaImena = DAOFactory.getDAOFactory().getKredencijaliDAO().svaKorisnickaImena();
		Set<String> setImena = new HashSet<String>(listaImena);
		for (KorisnikDTO red : tvKorisnici.getItems()) {
			if (red.getKorisnickoIme().length() == 0) {
				String upozorenje = "Niste unijeli sva korisnicka imena!\n";
				AlertsUtil.showWarningDialog("Upozorenje", upozorenje, "Popunite sva polja.");
				uspjesno = false;
				break;
			}
			if (!setImena.add(red.getKorisnickoIme())) {
				String upozorenje = "Korisnicko ime: " + red.getKorisnickoIme() + " je zauzeto!\n";
				AlertsUtil.showWarningDialog("Upozorenje", upozorenje, "Odaberite drugo korisnicko ime.");
				uspjesno = false;
				break;
			}
		}
		return uspjesno;

	}

	private boolean popunjenaPoljaZaProlaz() {
		String porukaOPogresnomUnosu = "";

		if (praznoPolje(tfIdProlaza.getText()))
			porukaOPogresnomUnosu += "Unesite id prolaza!\n";
		if (cbTipProlaza.getSelectionModel().isEmpty())
			porukaOPogresnomUnosu += "Odaberite tip prolaza!\n";

		if (porukaOPogresnomUnosu.length() != 0) {
			String upozorenje = "Niste odabrali sve opcije!\n";
			AlertsUtil.showWarningDialog("Upozorenje", upozorenje, porukaOPogresnomUnosu);
			return false;
		}

		if (ponovljenIdProlaza(tfIdProlaza.getText())) {
			String upozorenje = "Id prolaza je zauzet!\n";
			AlertsUtil.showWarningDialog("Upozorenje", upozorenje, "Odaberite drugu vrijednost.");
			return false;
		}
		return true;
	}

	private boolean ponovljenIdProlaza(String uneseniId) {
		ObservableList<TerminalDTO> terminali = GlavnaStrana.getTerminali();
		Set<String> setId = new HashSet<String>();
		for (TerminalDTO terminal : terminali)
			for (ProlazDTO prolaz : terminal.getProlazi())
				setId.add(prolaz.getId());
		for (ProlazDTO prolaz : listaProlaza)
			setId.add(prolaz.getId());
		return !setId.add(uneseniId);
	}

	private static boolean praznoPolje(String unos) {
		if (unos == null)
			return true;
		return unos.trim().length() == 0;
	}

	private String vratiTipProlaza(String idProlaz) {
		String tipProlaza = null;
		for (ProlazDTO prolaz : listaProlaza)
			if (prolaz.getId().equals(idProlaz)) {
				tipProlaza = prolaz.getTipProlaza();
				break;
			}
		return tipProlaza;
	}

}
