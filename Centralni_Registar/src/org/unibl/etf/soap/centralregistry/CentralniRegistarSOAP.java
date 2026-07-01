package org.unibl.etf.soap.centralregistry;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.unibl.etf.dao.DAOFactory;
import org.unibl.etf.model.KorisnikDTO;
import org.unibl.etf.model.ProlazDTO;
import org.unibl.etf.model.ProlazakDTO;
import org.unibl.etf.model.TerminalDTO;

@WebService
public class CentralniRegistarSOAP {

	public CentralniRegistarSOAP() {
	}

	@WebMethod
	public TerminalDTO terminal(String idTerminal) {
		return DAOFactory.getDAOFactory().getTerminalDAO().terminal(idTerminal);
	}

	@WebMethod
	public TerminalDTO[] terminali() {
		return DAOFactory.getDAOFactory().getTerminalDAO().terminali();
	}

	@WebMethod
	public void dodajTerminal(TerminalDTO terminal) {
		DAOFactory.getDAOFactory().getTerminalDAO().dodajTerminal(terminal);
	}

	@WebMethod
	public boolean obrisiTerminal(String idTerminal) {
		return DAOFactory.getDAOFactory().getTerminalDAO().obrisiTerminal(idTerminal);
	}

	@WebMethod
	public boolean izmjeniTerminal(TerminalDTO terminal) {
		return DAOFactory.getDAOFactory().getTerminalDAO().izmjeniTerminal(terminal);
	}

	@WebMethod
	public boolean provjeriTerminal(String nazivTeminala) {
		return DAOFactory.getDAOFactory().getTerminalDAO().provjeriTerminal(nazivTeminala);
	}

	@WebMethod
	public ProlazDTO[] prolazi() {
		return DAOFactory.getDAOFactory().getProlazDAO().prolazi();
	}

	@WebMethod
	public boolean aktivanProlaz(String idProlaz) {
		return DAOFactory.getDAOFactory().getProlazDAO().aktivanProlaz(idProlaz);
	}

	@WebMethod
	public KorisnikDTO[] korisnici() {
		return DAOFactory.getDAOFactory().getKorisnikDAO().korisnici();
	}

	@WebMethod
	public void postaviStatus(String korisnickoIme, boolean aktivan) {
		DAOFactory.getDAOFactory().getKorisnikDAO().postaviStatus(korisnickoIme, aktivan);
	}

	@WebMethod
	public void evidentirajProlazak(ProlazakDTO prolazak) {
		DAOFactory.getDAOFactory().getEvidencijaDAO().evidentirajProlazak(prolazak);
	}

	@WebMethod
	public void evidentirajPotjernicu(ProlazakDTO prolazak) {
		DAOFactory.getDAOFactory().getEvidencijaDAO().evidentirajPotjernicu(prolazak);
	}

	@WebMethod
	public void evidentirajDokumente(ProlazakDTO prolazak) {
		DAOFactory.getDAOFactory().getEvidencijaDAO().evidentirajDokumente(prolazak);
	}

}
