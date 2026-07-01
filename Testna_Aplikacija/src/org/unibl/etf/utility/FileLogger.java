package org.unibl.etf.utility;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.unibl.etf.utility.ConstantsUtil.*;

public class FileLogger {

	private static final Logger LOGGER = Logger.getLogger(Logger.class.getName());

	public static void log(Level level, String msg, Throwable thrown) {
		FileHandler fh = null;
		try {
			fh = new FileHandler(POCETNI_DIREKTORIJUM + File.separator + RESURSI_DIREKTORIJUM + File.separator
					+ LOG_DIREKTORIJUM + File.separator + LOG_DATOTEKA, true);
			LOGGER.setUseParentHandlers(false);
			LOGGER.addHandler(fh);
			LOGGER.log(level, msg, thrown);
		} catch (IOException | SecurityException e) {
			e.printStackTrace();
			LOGGER.log(Level.SEVERE, "logging", e);
		}
	}

}
