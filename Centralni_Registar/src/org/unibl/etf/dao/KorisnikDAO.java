package org.unibl.etf.dao;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.unibl.etf.model.KorisnikDTO;
import org.unibl.etf.model.ProlazDTO;
import org.unibl.etf.model.TerminalDTO;

public class KorisnikDAO {

	private static KorisnikDAO korisnikDAO;

	public static KorisnikDAO getKorisnikDAO() {
		if (korisnikDAO == null)
			korisnikDAO = new KorisnikDAO();
		return korisnikDAO;
	}

	public KorisnikDTO[] korisnici() {
		return Stream.of(DAOFactory.getDAOFactory().getProlazDAO().prolazi()).flatMap(t -> Stream.of(t.getKorisnici()))
				.toArray(KorisnikDTO[]::new);
	}

	public void postaviStatus(String korisnickoIme, boolean aktivan) {
		TerminalDTO terminal = pronadjiTerminal(korisnickoIme);
		List<ProlazDTO> listaProlaza = Arrays.stream(terminal.getProlazi()).collect(Collectors.toList());
		ProlazDTO prolaz = pronadjiProlaz(listaProlaza, korisnickoIme);
		List<KorisnikDTO> listaKorisnika = Arrays.stream(prolaz.getKorisnici()).collect(Collectors.toList());
		KorisnikDTO korisnik = listaKorisnika.stream().filter(k -> korisnickoIme.equals(k.getKorisnickoIme()))
				.findFirst().orElse(null);
		if (korisnik != null)
			korisnik.setAktivan(aktivan);
		ProlazDTO[] nizProlaza = listaProlaza.toArray(new ProlazDTO[0]);
		TerminalDTO promjenjeniTerminal = new TerminalDTO(terminal.getId(), terminal.getNaziv(), nizProlaza);
		DAOFactory.getDAOFactory().getTerminalDAO().izmjeniTerminal(promjenjeniTerminal);
	}

	private TerminalDTO pronadjiTerminal(String korisnickoIme) {
		TerminalDTO t = null;
		L: for (ProlazDTO p : DAOFactory.getDAOFactory().getProlazDAO().prolazi()) {
			for (KorisnikDTO k : p.getKorisnici())
				if (k.getKorisnickoIme().equals(korisnickoIme)) {
					String idTerminal = p.getIdTerminal();
					t = DAOFactory.getDAOFactory().getTerminalDAO().terminal(idTerminal);
					break L;
				}
		}
		return t;
	}

	private ProlazDTO pronadjiProlaz(List<ProlazDTO> listaProlaza, String korisnickoIme) {
		ProlazDTO pr = null;
		L: for (ProlazDTO p : listaProlaza) {
			for (KorisnikDTO k : p.getKorisnici())
				if (k.getKorisnickoIme().equals(korisnickoIme)) {
					pr = p;
					break L;
				}
		}
		return pr;
	}

}
