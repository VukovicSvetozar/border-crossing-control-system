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

	public ProlazakDAO getProlazakDAO() {
		return ProlazakDAO.getProlazakDAO();
	}

	public KontrolaDAO getKontrolaDAO() {
		return KontrolaDAO.getKontrolaDAO();
	}

}
