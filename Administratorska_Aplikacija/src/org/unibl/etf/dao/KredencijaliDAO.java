package org.unibl.etf.dao;

import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.unibl.etf.model.*;
import org.unibl.etf.soap.centralregistry.*;
import org.unibl.etf.utility.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class KredencijaliDAO {

	private static KredencijaliDAO kredencijaliDAO;

	public static KredencijaliDAO getKredencijaliDAO() {
		if (kredencijaliDAO == null)
			kredencijaliDAO = new KredencijaliDAO();
		return kredencijaliDAO;
	}

	private final String REDIS_KLJUC_SEPARATOR = PropertiesUtil.vratiSvojstvo("REDIS_KLJUC_SEPARATOR", String.class);
	private final String REDIS_KLJUC_KREDENCIJAL = PropertiesUtil.vratiSvojstvo("REDIS_KLJUC_KREDENCIJAL",
			String.class);

	public KredencijaliDTO kredencijali(String korisnickoIme) {
		return RedisUtil.getInstance().restoreObject(REDIS_KLJUC_KREDENCIJAL + REDIS_KLJUC_SEPARATOR + korisnickoIme,
				KredencijaliDTO.class);
	}

	public ObservableList<String> svaKorisnickaImena() {
		List<String> keys = RedisUtil.getInstance().restoreKeys(REDIS_KLJUC_KREDENCIJAL);
		List<String> ids = keys.stream().map(k -> k.substring(k.lastIndexOf(REDIS_KLJUC_SEPARATOR) + 1))
				.collect(Collectors.toList());
		ObservableList<String> retVal = FXCollections.observableArrayList(ids);
		return retVal;
	}

	public ObservableList<KredencijaliDTO> kredencijaliNaTerminalu(String idTerminal) {

		List<KredencijaliDTO> listaKredencijala = new ArrayList<>();
		CentralniRegistarSOAPServiceLocator loc = new CentralniRegistarSOAPServiceLocator();
		try {
			CentralniRegistarSOAP ser = loc.getCentralniRegistarSOAP();
			ProlazDTO[] prolazi = ser.prolazi();
			List<String> korisnickaImenaNaTerminalu = Stream.of(prolazi)
					.filter(p -> p.getIdTerminal().equals(idTerminal)).flatMap(p -> Stream.of(p.getKorisnici()))
					.map(k -> k.getKorisnickoIme()).collect(Collectors.toList());
			for (String korisnickoIme : korisnickaImenaNaTerminalu)
				listaKredencijala.add(kredencijali(korisnickoIme));
		} catch (Exception e) {
			FileLogger.log(Level.SEVERE, "Greska u radu sa SOAP servisom!", e);
		}
		ObservableList<KredencijaliDTO> retVal = FXCollections.observableArrayList(listaKredencijala);
		return retVal;
	}

	public void dodajKredencijale(KredencijaliDTO kredencijali) {
		RedisUtil.getInstance().storeObject(
				REDIS_KLJUC_KREDENCIJAL + REDIS_KLJUC_SEPARATOR + kredencijali.getKorisnickoIme(), kredencijali);
	}

	public void obrisiKredencijale(KredencijaliDTO kredencijali) {
		RedisUtil.getInstance().deleteObject(
				REDIS_KLJUC_KREDENCIJAL + REDIS_KLJUC_SEPARATOR + kredencijali.getKorisnickoIme(), kredencijali);
	}

	public String provjeriKredencijale(KorisnikDTO korisnik) {
		String poruka = null;
		KorisnikDTO korisnikUBazi = DAOFactory.getDAOFactory().getKorisnikDAO().korisnik(korisnik.getKorisnickoIme());
		if (korisnikUBazi == null)
			poruka = PropertiesUtil.vratiSvojstvo("GRESKA_NIJE_PRONADJEN", String.class);
		else if (korisnikUBazi.getAktivan())
			poruka = PropertiesUtil.vratiSvojstvo("GRESKA_AKTIVAN", String.class);
		else if (!korisnikUBazi.getIdProlaz().equals(korisnik.getIdProlaz()))
			poruka = PropertiesUtil.vratiSvojstvo("GRESKA_POGRESAN_PROLAZ", String.class);
		else if (!korisnikUBazi.getTipKontrole().equals(korisnik.getTipKontrole()))
			poruka = PropertiesUtil.vratiSvojstvo("GRESKA_POGRESNA_KONTROLA", String.class);
		else {
			KredencijaliDTO kredencijali = DAOFactory.getDAOFactory().getKredencijaliDAO()
					.kredencijali(korisnik.getKorisnickoIme());
			String salt = kredencijali.getSalt();
			if (CryptographyUtil.verifikacijaKorisnickeLozinke(korisnik.getLozinka(),
					kredencijali.getEnkodovanaLozinka(), salt)) {
				poruka = PropertiesUtil.vratiSvojstvo("OK", String.class);
				DAOFactory.getDAOFactory().getKorisnikDAO().postaviStatus(korisnik.getKorisnickoIme(), true);
			} else
				poruka = PropertiesUtil.vratiSvojstvo("GRESKA_POGRESNI_KREDENCIJALI", String.class);
		}
		return poruka;
	}

}
