package org.unibl.etf.dao;

import java.util.*;
import java.util.logging.Level;
import java.util.stream.*;

import org.unibl.etf.model.*;
import org.unibl.etf.soap.centralregistry.*;
import org.unibl.etf.utility.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class KorisnikDAO {

	private static KorisnikDAO korisnikDAO;

	public static KorisnikDAO getKorisnikDAO() {
		if (korisnikDAO == null)
			korisnikDAO = new KorisnikDAO();
		return korisnikDAO;
	}

	public KorisnikDTO korisnik(String korisnickoIme) {
		return sviKorisnici().stream().filter(k -> k.getKorisnickoIme().equals(korisnickoIme)).findFirst().orElse(null);
	}

	public ObservableList<KorisnikDTO> sviKorisnici() {
		ObservableList<KorisnikDTO> korisnici = null;
		CentralniRegistarSOAPServiceLocator loc = new CentralniRegistarSOAPServiceLocator();
		try {
			CentralniRegistarSOAP ser = loc.getCentralniRegistarSOAP();
			List<KorisnikDTO> listaKorisnika = Arrays.stream(ser.korisnici()).collect(Collectors.toList());
			korisnici = FXCollections.observableList(listaKorisnika);
		} catch (Exception e) {
			FileLogger.log(Level.SEVERE, "Greska u radu sa SOAP servisom!", e);
		}
		return korisnici;
	}

	public ObservableList<KorisnikDTO> korisniciProlaza(String idProlaz) {
		ObservableList<KorisnikDTO> korisnici = null;
		CentralniRegistarSOAPServiceLocator loc = new CentralniRegistarSOAPServiceLocator();
		try {
			CentralniRegistarSOAP ser = loc.getCentralniRegistarSOAP();
			List<ProlazDTO> listaProlaza = Arrays.stream(ser.prolazi()).collect(Collectors.toList());
			List<KorisnikDTO> listaKorisnika = listaProlaza.stream().filter(p -> p.getId().equals(idProlaz))
					.flatMap(p -> Stream.of(p.getKorisnici())).collect(Collectors.toList());
			korisnici = FXCollections.observableList(listaKorisnika);
		} catch (Exception e) {
			FileLogger.log(Level.SEVERE, "Greska u radu sa SOAP servisom!", e);
		}
		return korisnici;
	}

	public ObservableList<KorisnikDTO> korisniciTerminala(String idTerminal) {
		ObservableList<KorisnikDTO> korisnici = null;
		CentralniRegistarSOAPServiceLocator loc = new CentralniRegistarSOAPServiceLocator();
		try {
			CentralniRegistarSOAP ser = loc.getCentralniRegistarSOAP();
			ProlazDTO[] prolazi = ser.prolazi();
			List<KorisnikDTO> listaKorisnika = Stream.of(prolazi).filter(p -> p.getIdTerminal().equals(idTerminal))
					.flatMap(p -> Stream.of(p.getKorisnici())).collect(Collectors.toList());
			korisnici = FXCollections.observableList(listaKorisnika);
		} catch (Exception e) {
			FileLogger.log(Level.SEVERE, "Greska u radu sa SOAP servisom!", e);
		}
		return korisnici;
	}

	public void postaviStatus(String korisnickoIme, boolean aktivan) {
		CentralniRegistarSOAPServiceLocator loc = new CentralniRegistarSOAPServiceLocator();
		try {
			CentralniRegistarSOAP ser = loc.getCentralniRegistarSOAP();
			ser.postaviStatus(korisnickoIme, aktivan);
		} catch (Exception e) {
			FileLogger.log(Level.SEVERE, "Greska u radu sa SOAP servisom!", e);
		}
	}

}
