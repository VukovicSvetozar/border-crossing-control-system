package org.unibl.etf.dao;

import java.util.stream.Stream;

import org.unibl.etf.model.ProlazDTO;

public class ProlazDAO {

	private static ProlazDAO prolazDAO;

	public static ProlazDAO getProlazDAO() {
		if (prolazDAO == null)
			prolazDAO = new ProlazDAO();
		return prolazDAO;
	}

	public ProlazDAO() {
	}

	public ProlazDTO[] prolazi() {
		return Stream.of(DAOFactory.getDAOFactory().getTerminalDAO().terminali())
				.flatMap(t -> Stream.of(t.getProlazi())).toArray(ProlazDTO[]::new);
	}

	public boolean aktivanProlaz(String idProlaz) {
		return Stream.of(prolazi()).filter(p -> idProlaz.equals(p.getId())).count() != 0
				&& Stream.of(prolazi()).filter(p -> idProlaz.equals(p.getId()))
						.flatMap(p -> Stream.of(p.getKorisnici())).allMatch(k -> k.getAktivan());
	}

}
