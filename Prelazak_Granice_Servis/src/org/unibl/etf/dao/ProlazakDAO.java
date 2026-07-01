package org.unibl.etf.dao;

import java.util.List;

import org.unibl.etf.data.Podaci;
import org.unibl.etf.model.*;

public class ProlazakDAO {

	private static ProlazakDAO prolazakDAO;

	public static ProlazakDAO getProlazakDAO() {
		if (prolazakDAO == null)
			prolazakDAO = new ProlazakDAO();
		return prolazakDAO;
	}

	public ProlazakDAO() {
	}

	public void dodajProlazak(String idTerminal, String idKontrola, ProlazakDTO prolazak) {
		KontrolaDTO kontrola = new KontrolaDTO(idKontrola, idTerminal, StatusKontrola.ZAUZET);
		prolazak.azurirajInformacije(StatusProlazak.POLICIJSKA_KONTROLA);
		List<ProlazakDTO> prolasci = Podaci.kontrole.get(idKontrola).getProlasci();
		prolasci.add(prolazak);
		kontrola.setProlasci(prolasci);
		Podaci.kontrole.put(idKontrola, kontrola);
	}

	public void azurirajProlazak(String idKontrola, ProlazakDTO prolazak) {
		if (prolazak != null && !Podaci.kontrole.isEmpty() || Podaci.kontrole.get(idKontrola) != null) {
			int indeks = Podaci.kontrole.get(idKontrola).getProlasci().indexOf(prolazak);
			if (indeks != -1)
				Podaci.kontrole.get(idKontrola).getProlasci().set(indeks, prolazak);
		}
	}

	public ProlazakDTO azurirajInformacije(String idKontrola, String idProlazak) {
		if (Podaci.kontrole.isEmpty() || Podaci.kontrole.get(idKontrola) == null) {
			return null;
		} else {
			return Podaci.kontrole.get(idKontrola).getProlasci().stream().filter(p -> p.getId().equals(idProlazak))
					.findFirst().orElse(null);
		}
	}

	public ProlazakDTO provjeriOsobu(String idKontrola, String statusProlaska) {
		if (Podaci.kontrole.isEmpty() || Podaci.kontrole.get(idKontrola) == null)
			return null;
		else
			return Podaci.kontrole.get(idKontrola).getProlasci().stream()
					.filter(p -> p.getStatus().equals(statusProlaska)).findFirst().orElse(null);
	}
}
