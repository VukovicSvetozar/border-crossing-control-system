package org.unibl.etf.dao;

public class DAOFactory {

	private static DAOFactory daoFactory;

	public static DAOFactory getDAOFactory() {
		if (daoFactory == null)
			daoFactory = new DAOFactory();
		return daoFactory;
	}

	private DAOFactory() {
	}

	public EvidencijaDAO getEvidencijaDAO() {
		return EvidencijaDAO.getEvidencijaDAO();
	}
	
	public TerminalDAO getTerminalDAO() {
		return TerminalDAO.getTerminalDAO();
	}

	public ProlazDAO getProlazDAO() {
		return ProlazDAO.getProlazDAO();
	}

	public KorisnikDAO getKorisnikDAO() {
		return KorisnikDAO.getKorisnikDAO();
	}

}
