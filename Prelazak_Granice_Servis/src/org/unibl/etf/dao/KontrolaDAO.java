package org.unibl.etf.dao;

import java.util.List;

import org.unibl.etf.data.Podaci;
import org.unibl.etf.model.KontrolaDTO;
import org.unibl.etf.model.ProlazakDTO;
import org.unibl.etf.model.StatusKontrola;

public class KontrolaDAO {

	private static KontrolaDAO kontrolaDAO;

	public static KontrolaDAO getKontrolaDAO() {
		if (kontrolaDAO == null)
			kontrolaDAO = new KontrolaDAO();
		return kontrolaDAO;
	}

	public KontrolaDAO() {
	}

	public boolean registracija(String idKontrola, String idTerminal) {
		if (!Podaci.terminali.containsKey(idTerminal)) {
			Podaci.terminali.put(idTerminal, true);
		}
		if (Podaci.kontrole.containsKey(idKontrola)) {
			return false;
		} else {
			KontrolaDTO kontrola = new KontrolaDTO(idKontrola, idTerminal, StatusKontrola.AKTIVAN);
			Podaci.kontrole.put(idKontrola, kontrola);
			return true;
		}
	}

	public void odjava(String idKontrola, String idTerminal) {
		Podaci.kontrole.remove(idKontrola);
		boolean postojiDrugi = Podaci.kontrole.entrySet().stream().map(e -> e.getValue())
				.anyMatch(k -> k.getIdTerminal().equals(idTerminal));
		if (!postojiDrugi)
			Podaci.terminali.remove(idTerminal);
	}

	public boolean provjeriDostupnost(String idTerminal) {
		boolean dostupan = true;
		if (!Podaci.terminali.isEmpty())
			dostupan = Podaci.terminali.get(idTerminal);
		return dostupan;
	}

	public void promjeniDostupnost(String idTerminal, boolean status) {
		Podaci.terminali.put(idTerminal, status);
	}

	public String provjeraStatusa(String idKontrola) {
		return Podaci.kontrole.get(idKontrola).getStatus();
	}

	public void azurirajStatus(String idTerminal, String idKontrola, String status) {
		KontrolaDTO kontrola = new KontrolaDTO(idKontrola, idTerminal, status);
		List<ProlazakDTO> prolasci = Podaci.kontrole.get(idKontrola).getProlasci();
		kontrola.setProlasci(prolasci);
		Podaci.kontrole.put(idKontrola, kontrola);
	}

}
