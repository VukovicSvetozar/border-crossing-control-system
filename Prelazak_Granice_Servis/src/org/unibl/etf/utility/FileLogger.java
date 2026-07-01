package org.unibl.etf.utility;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileLogger {

	private static final Logger lOGGER = Logger.getLogger(Logger.class.getName());
	private static final String RESURSI_DIREKTORIJUM = PropertiesUtil.vratiSvojstvo("RESURSI_DIREKTORIJUM",
			String.class);
	private static final String LOG_DIREKTORIJUM = PropertiesUtil.vratiSvojstvo("LOG_DIREKTORIJUM", String.class);
	private static final String LOG_DATOTEKA = PropertiesUtil.vratiSvojstvo("LOG_DATOTEKA", String.class);

	public static void log(Level level, String msg, Throwable thrown) {
		FileHandler fh = null;
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			URI uri = classLoader
					.getResource(RESURSI_DIREKTORIJUM + File.separator + LOG_DIREKTORIJUM + File.separator + LOG_DATOTEKA)
					.toURI();
			fh = new FileHandler(Paths.get(uri).toString(), true);
			lOGGER.setUseParentHandlers(false);
			lOGGER.addHandler(fh);
			lOGGER.log(level, msg, thrown);
		} catch (IOException | SecurityException | URISyntaxException e) {
			lOGGER.log(Level.SEVERE, "logging", e);
		}
	}

}
