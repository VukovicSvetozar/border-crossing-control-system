package org.unibl.etf.rest.record;

import java.util.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.unibl.etf.dao.DAOFactory;
import org.unibl.etf.model.*;

@Path("evidencije")
public class RESTServiceEvidencija {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("potjernice")
	public Response evidentiraniPrestupnici() {
		List<ProlazakDTO> prolasci = DAOFactory.getDAOFactory().getEvidencijaDAO().vratiProcesiranePotjernice();
		GenericEntity<List<ProlazakDTO>> entities = new GenericEntity<List<ProlazakDTO>>(prolasci) {
		};
		return Response.ok(entities).build();
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("dokumenti")
	public Response evidentiraniDokumenti() {
		List<ProlazakDTO> prolasci = DAOFactory.getDAOFactory().getEvidencijaDAO().vratiEvidentiraneDokumente();
		GenericEntity<List<ProlazakDTO>> entities = new GenericEntity<List<ProlazakDTO>>(prolasci) {
		};
		return Response.ok(entities).build();
	}

}
