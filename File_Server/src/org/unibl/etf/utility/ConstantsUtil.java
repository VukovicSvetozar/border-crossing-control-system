package org.unibl.etf.utility;

public class ConstantsUtil {

	public static String POLICY_FOLDER;
	public static String POLICY_FILE;
	public static Integer RMI_PORT;
	public static String RMI_NAME;
	public static String DOCUMENTS_FOLDER;
	public static String OK_MESSAGE;
	public static String ERROR_MESSAGE;

	public static void ucitajKonstante() {
		POLICY_FOLDER = PropertiesUtil.vratiSvojstvo("POLICY_FOLDER", String.class);
		POLICY_FILE = PropertiesUtil.vratiSvojstvo("POLICY_FILE", String.class);
		RMI_PORT = PropertiesUtil.vratiSvojstvo("RMI_PORT", Integer.class);
		RMI_NAME = PropertiesUtil.vratiSvojstvo("RMI_NAME", String.class);
		DOCUMENTS_FOLDER = PropertiesUtil.vratiSvojstvo("DOCUMENTS_FOLDER", String.class);
		OK_MESSAGE = PropertiesUtil.vratiSvojstvo("OK_MESSAGE", String.class);
		ERROR_MESSAGE = PropertiesUtil.vratiSvojstvo("ERROR_MESSAGE", String.class);
	}

}
