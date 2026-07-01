package org.unibl.etf.utility;

import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class PropertiesUtil {

	public static <T> T vratiSvojstvo(String nazivSvojstva, Class<T> tip) {
		T vrijednostSvojstva = null;
		ResourceBundle rb = PropertyResourceBundle.getBundle("resources.properties.config");
		if ("String".equals(tip.getSimpleName()))
			vrijednostSvojstva = tip.cast(rb.getString(nazivSvojstva));
		else
			vrijednostSvojstva = tip.cast(Integer.parseInt(rb.getString(nazivSvojstva)));
		return vrijednostSvojstva;
	}

}
