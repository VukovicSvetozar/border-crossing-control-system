package org.unibl.etf.rest.user;

import java.util.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.unibl.etf.dao.DAOFactory;
import org.unibl.etf.model.*;
import org.unibl.etf.utility.PropertiesUtil;

@Path("korisnici")
public class RESTServiceKorisnik {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{korisnickoIme}")
	public Response korisnik(@PathParam("korisnickoIme") String korisnickoIme) {
		KorisnikDTO korisnik = DAOFactory.getDAOFactory().getKorisnikDAO().korisnik(korisnickoIme);
		if (korisnik != null) {
			return Response.ok(korisnik).build();
		}
		final String nijePronadjen = PropertiesUtil.vratiSvojstvo("GRESKA_NIJE_PRONADJEN", String.class);
		return Response.status(Response.Status.NOT_FOUND).entity(nijePronadjen).build();
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response sviKorisnici() {
		List<KorisnikDTO> korisnici = DAOFactory.getDAOFactory().getKorisnikDAO().sviKorisnici();
		GenericEntity<List<KorisnikDTO>> entities = new GenericEntity<List<KorisnikDTO>>(korisnici) {
		};
		return Response.ok(entities).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("terminali/{idTerminal}")
	public Response korisniciTerminala(@PathParam("idTerminal") String idTerminal) {
		List<KorisnikDTO> korisnici = DAOFactory.getDAOFactory().getKorisnikDAO().korisniciTerminala(idTerminal);
		GenericEntity<List<KorisnikDTO>> entities = new GenericEntity<List<KorisnikDTO>>(korisnici) {
		};
		return Response.ok(entities).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("prolazi/{idProlaz}")
	public Response korisniciProlaza(@PathParam("idProlaz") String idProlaz) {
		List<KorisnikDTO> korisnici = DAOFactory.getDAOFactory().getKorisnikDAO().korisniciProlaza(idProlaz);
		GenericEntity<List<KorisnikDTO>> entities = new GenericEntity<List<KorisnikDTO>>(korisnici) {
		};
		return Response.ok(entities).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("autentifikacija")
	public Response provjeraKredencijala(KorisnikDTO korisnik) {
		String poruka = DAOFactory.getDAOFactory().getKredencijaliDAO().provjeriKredencijale(korisnik);
		return Response.status(Response.Status.OK).entity(poruka).build();
	}

}
