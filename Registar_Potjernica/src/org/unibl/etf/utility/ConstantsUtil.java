package org.unibl.etf.utility;

public class ConstantsUtil {

	public static String POLICY_FOLDER;
	public static String POLICY_FILE;
	public static String RESOURCES_FOLDER;
	public static String RESOURCES_FILE;
	public static Integer RMI_PORT;
	public static String RMI_NAME;

	public static void ucitajKonstante() {
		POLICY_FOLDER = PropertiesUtil.vratiSvojstvo("POLICY_FOLDER", String.class);
		POLICY_FILE = PropertiesUtil.vratiSvojstvo("POLICY_FILE", String.class);
		RESOURCES_FOLDER = PropertiesUtil.vratiSvojstvo("RESOURCES_FOLDER", String.class);
		RESOURCES_FILE = PropertiesUtil.vratiSvojstvo("RESOURCES_FILE", String.class);
		RMI_PORT = PropertiesUtil.vratiSvojstvo("RMI_PORT", Integer.class);
		RMI_NAME = PropertiesUtil.vratiSvojstvo("RMI_NAME", String.class);
	}

}
