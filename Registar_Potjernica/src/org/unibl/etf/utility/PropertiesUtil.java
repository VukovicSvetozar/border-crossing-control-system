package org.unibl.etf.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;

public class PropertiesUtil {

	public static <T> T vratiSvojstvo(String nazivSvojstva, Class<T> tip) {
		String putanja = "properties" + File.separator + "config.properties";
		T vrijednostSvojstva = null;
		Properties properties = new Properties();
		try (InputStream ulaz = new FileInputStream(putanja)) {
			properties.load(ulaz);
			if ("String".equals(tip.getSimpleName()))
				vrijednostSvojstva = tip.cast(properties.getProperty(nazivSvojstva));
			else
				vrijednostSvojstva = tip.cast(Integer.parseInt(properties.getProperty(nazivSvojstva)));
		} catch (IOException ex) {
			FileLogger.log(Level.SEVERE, "Nije pronadjen fajl na navedenoj putanji.", ex);
		}
		return vrijednostSvojstva;
	}

}
