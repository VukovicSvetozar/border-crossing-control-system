package org.unibl.etf.soap.transit;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.unibl.etf.dao.DAOFactory;
import org.unibl.etf.model.ProlazakDTO;

@WebService
public class TransitSOAP {

	public TransitSOAP() {
	}

	@WebMethod
	public boolean registracijaKontrole(String idKontrola, String idTerminal) {
		return DAOFactory.getDAOFactory().getKontrolaDAO().registracija(idKontrola, idTerminal);
	}

	@WebMethod
	public void odjavaKontrole(String idKontrola, String idTerminal) {
		DAOFactory.getDAOFactory().getKontrolaDAO().odjava(idKontrola, idTerminal);
	}

	@WebMethod
	public boolean provjeriDostupnost(String idTerminal) {
		return DAOFactory.getDAOFactory().getKontrolaDAO().provjeriDostupnost(idTerminal);
	}

	@WebMethod
	public void promjeniDostupnost(String idTerminal, boolean status) {
		DAOFactory.getDAOFactory().getKontrolaDAO().promjeniDostupnost(idTerminal, status);
	}

	@WebMethod
	public String provjeraStatusaKontrole(String idKontrola) {
		return DAOFactory.getDAOFactory().getKontrolaDAO().provjeraStatusa(idKontrola);
	}

	@WebMethod
	public void azurirajStatusKontrole(String idTerminal, String idKontrola, String status) {
		DAOFactory.getDAOFactory().getKontrolaDAO().azurirajStatus(idTerminal, idKontrola, status);
	}

	@WebMethod
	public void dodajProlazak(String idTerminal, String idKontrola, ProlazakDTO prolazak) {
		DAOFactory.getDAOFactory().getProlazakDAO().dodajProlazak(idTerminal, idKontrola, prolazak);
	}

	@WebMethod
	public void azurirajProlazak(String idKontrola, ProlazakDTO prolazak) {
		DAOFactory.getDAOFactory().getProlazakDAO().azurirajProlazak(idKontrola, prolazak);
	}

	@WebMethod
	public ProlazakDTO azurirajInformacije(String idKontrola, String idProlazak) {
		return DAOFactory.getDAOFactory().getProlazakDAO().azurirajInformacije(idKontrola, idProlazak);
	}

	@WebMethod
	public static ProlazakDTO provjeriOsobu(String idKontrola, String statusProlaska) {
		return DAOFactory.getDAOFactory().getProlazakDAO().provjeriOsobu(idKontrola, statusProlaska);
	}

}
