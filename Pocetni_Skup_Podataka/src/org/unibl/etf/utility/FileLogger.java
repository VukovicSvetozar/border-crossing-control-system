package org.unibl.etf.utility;

import java.io.*;

import java.util.logging.*;

public class FileLogger {

	private static final Logger logger = Logger.getLogger(Logger.class.getName());
	private static final String POCETNI_DIREKTORIJUM = PropertiesUtil.vratiSvojstvo("POCETNI_DIREKTORIJUM",
			String.class);
	private static final String RESURSI_DIREKTORIJUM = PropertiesUtil.vratiSvojstvo("RESURSI_DIREKTORIJUM",
			String.class);
	private static final String logDirektorijum = PropertiesUtil.vratiSvojstvo("LOG_DIREKTORIJUM", String.class);
	private static final String logDatoteka = PropertiesUtil.vratiSvojstvo("LOG_DATOTEKA", String.class);

	public static void log(Level level, String msg, Throwable thrown) {
		FileHandler fh = null;
		try {
			fh = new FileHandler(POCETNI_DIREKTORIJUM + File.separator + RESURSI_DIREKTORIJUM + File.separator
					+ logDirektorijum + File.separator + logDatoteka, true);
			logger.setUseParentHandlers(false);
			logger.addHandler(fh);
			logger.log(level, msg, thrown);
		} catch (IOException | SecurityException e) {
			logger.log(Level.SEVERE, "logging", e);
		}
	}

}
