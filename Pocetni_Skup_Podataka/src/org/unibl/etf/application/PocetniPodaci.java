package org.unibl.etf.application;

import java.util.*;
import java.util.logging.Level;

import org.unibl.etf.model.*;
import org.unibl.etf.soap.centralregistry.*;
import org.unibl.etf.utility.*;

public class PocetniPodaci {

	private final static String REDIS_KLJUC_SEPARATOR = PropertiesUtil.vratiSvojstvo("REDIS_KLJUC_SEPARATOR",
			String.class);
	private final static String REDIS_KLJUC_KREDENCIJAL = PropertiesUtil.vratiSvojstvo("REDIS_KLJUC_KREDENCIJAL",
			String.class);

	public static void main(String[] args) {

		CentralniRegistarSOAPServiceLocator loc = new CentralniRegistarSOAPServiceLocator();
		try {
			CentralniRegistarSOAP ser = loc.getCentralniRegistarSOAP();

			List<TerminalDTO> terminali = new ArrayList<TerminalDTO>();

			ProlazDTO[] prolazi1 = new ProlazDTO[2];
			KorisnikDTO k11 = kreirajKorisnika("Ana", "101", TipKontrole.POLICIJSKA);
			KorisnikDTO k12 = kreirajKorisnika("Ena", "101", TipKontrole.CARINSKA);
			KorisnikDTO[] korisnici1 = { k11, k12 };
			prolazi1[0] = new ProlazDTO("101", "001", korisnici1, TipProlaza.ULAZ);
			KorisnikDTO k13 = kreirajKorisnika("Ina", "102", TipKontrole.POLICIJSKA);
			KorisnikDTO k14 = kreirajKorisnika("Una", "102", TipKontrole.CARINSKA);
			KorisnikDTO[] korisnici2 = { k13, k14 };
			prolazi1[1] = new ProlazDTO("102", "001", korisnici2, TipProlaza.IZLAZ);
			terminali.add(new TerminalDTO("001", "AAA", prolazi1, TipSerijalizacije.GSON));

			ProlazDTO[] prolazi2 = new ProlazDTO[2];
			KorisnikDTO k21 = kreirajKorisnika("Lana", "103", TipKontrole.POLICIJSKA);
			KorisnikDTO k22 = kreirajKorisnika("Lena", "103", TipKontrole.CARINSKA);
			KorisnikDTO[] korisnici3 = { k21, k22 };
			prolazi2[0] = new ProlazDTO("103", "002", korisnici3, TipProlaza.ULAZ);
			KorisnikDTO k23 = kreirajKorisnika("Maja", "104", TipKontrole.POLICIJSKA);
			KorisnikDTO k24 = kreirajKorisnika("Masa", "104", TipKontrole.CARINSKA);
			KorisnikDTO[] korisnici4 = { k23, k24 };
			prolazi2[1] = new ProlazDTO("104", "002", korisnici4, TipProlaza.IZLAZ);
			terminali.add(new TerminalDTO("002", "BBB", prolazi2, TipSerijalizacije.KRYO));

			ProlazDTO[] prolazi3 = new ProlazDTO[2];
			KorisnikDTO k31 = kreirajKorisnika("Tena", "105", TipKontrole.POLICIJSKA);
			KorisnikDTO k32 = kreirajKorisnika("Tina", "105", TipKontrole.CARINSKA);
			KorisnikDTO[] korisnici5 = { k31, k32 };
			prolazi3[0] = new ProlazDTO("105", "003", korisnici5, TipProlaza.ULAZ);
			KorisnikDTO k33 = kreirajKorisnika("Rada", "106", TipKontrole.POLICIJSKA);
			KorisnikDTO k34 = kreirajKorisnika("Dara", "106", TipKontrole.CARINSKA);
			KorisnikDTO[] korisnici6 = { k33, k34 };
			prolazi3[1] = new ProlazDTO("106", "003", korisnici6, TipProlaza.IZLAZ);
			terminali.add(new TerminalDTO("003", "CCC", prolazi3, TipSerijalizacije.JAVA));

			ProlazDTO[] prolazi4 = new ProlazDTO[2];
			KorisnikDTO k41 = kreirajKorisnika("Kata", "107", TipKontrole.POLICIJSKA);
			KorisnikDTO k42 = kreirajKorisnika("Sanja", "107", TipKontrole.CARINSKA);
			KorisnikDTO[] korisnici7 = { k41, k42 };
			prolazi4[0] = new ProlazDTO("107", "004", korisnici7, TipProlaza.ULAZ);
			KorisnikDTO k43 = kreirajKorisnika("Vanja", "108", TipKontrole.POLICIJSKA);
			KorisnikDTO k44 = kreirajKorisnika("Tanja", "108", TipKontrole.CARINSKA);
			KorisnikDTO[] korisnici8 = { k43, k44 };
			prolazi4[1] = new ProlazDTO("108", "004", korisnici8, TipProlaza.IZLAZ);
			terminali.add(new TerminalDTO("004", "DDD", prolazi4, TipSerijalizacije.XML));

			for (TerminalDTO terminal : terminali) {
				ser.dodajTerminal(terminal);
			}

		} catch (Exception e) {
			FileLogger.log(Level.SEVERE, "Greska pri kreiranju pocetnih podataka!", e);
		}

		System.out.println("Podaci su uspjesno kreirani.");

	}

	private static KorisnikDTO kreirajKorisnika(String korisnickoIme, String idProlaz, String tipKontrole) {
		String lozinka = new StringBuilder(korisnickoIme).reverse().toString();
		String salt = CryptographyUtil.getSalt(30);
		String enkodovanaLozinka = CryptographyUtil.getSigurnaLozinka(lozinka, salt);
		KredencijaliDTO kredencijal = new KredencijaliDTO(korisnickoIme, enkodovanaLozinka, salt);
		RedisUtil.getInstance().storeObject(
				REDIS_KLJUC_KREDENCIJAL + REDIS_KLJUC_SEPARATOR + kredencijal.getKorisnickoIme(), kredencijal);
		return new KorisnikDTO(false, idProlaz, korisnickoIme, "", tipKontrole);
	}

}
