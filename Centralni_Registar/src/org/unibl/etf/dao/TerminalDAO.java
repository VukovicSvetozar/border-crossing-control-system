package org.unibl.etf.dao;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.unibl.etf.model.TerminalDTO;
import org.unibl.etf.utility.*;

public class TerminalDAO {

	private static TerminalDAO terminalDAO;
	private static String putanjaDirektorijuma;

	public static TerminalDAO getTerminalDAO() {
		if (terminalDAO == null)
			terminalDAO = new TerminalDAO();
		return terminalDAO;
	}

	public TerminalDAO() {
		putanjaDirektorijuma = PropertiesUtil.vratiSvojstvo("TERMINALI_PUTANJA", String.class);
	}

	public TerminalDTO terminal(String idTerminala) {
		return SerializationUtil.getInstance().deserijalizacija(idTerminala);
	}

	public TerminalDTO[] terminali() {
		Set<String> listaIdTerminala = null;
		try {
			listaIdTerminala = Files.list(Paths.get(putanjaDirektorijuma)).map(p -> p.getFileName().toString())
					.map(s -> s.substring(3, 6)).collect(Collectors.toSet());
		} catch (IOException e) {
			FileLogger.log(Level.SEVERE, "Greska pri otvaranju direktorijuma!", e);
		}
		List<TerminalDTO> retVal = new ArrayList<TerminalDTO>();
		listaIdTerminala.stream().forEach(idTerminala -> {
			retVal.add(SerializationUtil.getInstance().deserijalizacija(idTerminala));
		});
		return retVal.toArray(new TerminalDTO[retVal.size()]);
	}

	public void dodajTerminal(TerminalDTO terminal) {
		SerializationUtil.getInstance().serijalizacija(terminal);
	}

	public boolean obrisiTerminal(String idTerminala) {
		boolean obrisan = false;
		Path putanjaFajla = null;
		try {
			putanjaFajla = Files.list(Paths.get(putanjaDirektorijuma)).filter(p -> p.toString().contains(idTerminala))
					.findAny().get();
			obrisan = Files.deleteIfExists(putanjaFajla);
		} catch (IOException e) {
			FileLogger.log(Level.SEVERE, "Greska pri otvaranju direktorijuma!", e);
		}
		return obrisan;
	}

	public boolean izmjeniTerminal(TerminalDTO terminal) {
		boolean uspjesnaIzmjena = false;
		if (terminal(terminal.getId()) != null) {
			SerializationUtil.getInstance().serijalizacija(terminal);
			uspjesnaIzmjena = true;
		}
		return uspjesnaIzmjena;
	}

	public boolean provjeriTerminal(String nazivTeminala) {
		boolean postoji = false;
		for (TerminalDTO terminal : terminali()) {
			if (nazivTeminala.equals(terminal.getNaziv())) {
				postoji = true;
				break;
			}
		}
		return postoji;
	}

}
