package org.unibl.etf.dao;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.unibl.etf.model.ProlazakDTO;
import org.unibl.etf.utility.FileLogger;
import org.unibl.etf.utility.PropertiesUtil;
import org.unibl.etf.utility.SerializationUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EvidencijaDAO {

	private static EvidencijaDAO evidencijaDAO;
	private static String putanjaDirektorijuma;

	public static EvidencijaDAO getEvidencijaDAO() {
		if (evidencijaDAO == null)
			evidencijaDAO = new EvidencijaDAO();
		return evidencijaDAO;
	}

	public EvidencijaDAO() {
		putanjaDirektorijuma = PropertiesUtil.vratiSvojstvo("RESURSI_PUTANJA", String.class);
	}

	public void evidentirajProlazak(ProlazakDTO prolazak) {
		final String PROLASCI_DIREKTORIJUM = PropertiesUtil.vratiSvojstvo("PROLASCI_DIREKTORIJUM", String.class);
		final String SEPARATOR = PropertiesUtil.vratiSvojstvo("SEPARATOR", String.class);
		;
		String imeFajla = prolazak.getId().split(SEPARATOR)[0];
		String putanja = putanjaDirektorijuma + File.separator + PROLASCI_DIREKTORIJUM + File.separator + imeFajla
				+ PropertiesUtil.vratiSvojstvo("PROLASCI_TIP_DATOTEKE", String.class);
		;
		StringBuilder sb = new StringBuilder();
		sb.append(" id:         " + prolazak.getId() + "\n");
		sb.append(" id osoba:   " + prolazak.getIdOsoba() + "\n");
		sb.append(" informacije:" + "\n" + prolazak.getInformacije() + "\n\n");
		try {
			Files.write(Paths.get(putanja), sb.toString().getBytes(), StandardOpenOption.APPEND,
					StandardOpenOption.CREATE);
		} catch (IOException e) {
			FileLogger.log(Level.SEVERE, "Greska pri otvaranju direktorijuma!", e);
		}
	}

	public void evidentirajPotjernicu(ProlazakDTO prolazak) {
		final String POTJERNICE_DIREKTORIJUM = PropertiesUtil.vratiSvojstvo("POTJERNICE_DIREKTORIJUM", String.class);
		String imeFajla = prolazak.getId() + PropertiesUtil.vratiSvojstvo("SERIJALIZACIJA_TIP_DATOTEKE", String.class);
		String putanja = putanjaDirektorijuma + File.separator + POTJERNICE_DIREKTORIJUM + File.separator + imeFajla;
		SerializationUtil.serializeWithXML(prolazak, putanja, ProlazakDTO.class);
	}

	public void evidentirajDokumente(ProlazakDTO prolazak) {
		final String DOKUMENTI_DIREKTORIJUM = PropertiesUtil.vratiSvojstvo("DOKUMENTI_DIREKTORIJUM", String.class);
		String imeFajla = prolazak.getId() + PropertiesUtil.vratiSvojstvo("SERIJALIZACIJA_TIP_DATOTEKE", String.class);
		String putanja = putanjaDirektorijuma + File.separator + DOKUMENTI_DIREKTORIJUM + File.separator + imeFajla;
		SerializationUtil.serializeWithXML(prolazak, putanja, ProlazakDTO.class);
	}

	public ObservableList<ProlazakDTO> vratiProcesiranePotjernice() {
		ObservableList<ProlazakDTO> prolasci = null;
		try {
			String putanja = putanjaDirektorijuma + File.separator
					+ PropertiesUtil.vratiSvojstvo("POTJERNICE_DIREKTORIJUM", String.class);
			List<ProlazakDTO> listaProlazaka = Files.walk(Paths.get(putanja)).filter(p -> p.toFile().isFile())
					.map(p -> SerializationUtil.deserializeWithXML(p.toString(), ProlazakDTO.class))
					.collect(Collectors.toList());
			prolasci = FXCollections.observableList(listaProlazaka);
		} catch (Exception e) {
			FileLogger.log(Level.SEVERE, "Greska u radu sa SOAP servisom!", e);
		}
		return prolasci;
	}

	public ObservableList<ProlazakDTO> vratiEvidentiraneDokumente() {
		ObservableList<ProlazakDTO> prolasci = null;
		try {
			String putanja = putanjaDirektorijuma + File.separator
					+ PropertiesUtil.vratiSvojstvo("DOKUMENTI_DIREKTORIJUM", String.class);
			List<ProlazakDTO> listaProlazaka = Files.walk(Paths.get(putanja)).filter(p -> p.toFile().isFile())
					.map(p -> SerializationUtil.deserializeWithXML(p.toString(), ProlazakDTO.class))
					.collect(Collectors.toList());
			prolasci = FXCollections.observableList(listaProlazaka);
		} catch (Exception e) {
			FileLogger.log(Level.SEVERE, "Greska u radu sa SOAP servisom!", e);
		}
		return prolasci;
	}

}
