package org.unibl.etf.utility;

public class ConstantsUtil {

	public static String POCETNI_DIREKTORIJUM;
	public static String RESURSI_DIREKTORIJUM;
	public static String LOG_DIREKTORIJUM;
	public static String LOG_DATOTEKA;
	public static String KEYSTORE_DIREKTORIJUM;
	public static String KEYSTORE_PATH;
	public static String KEYSTORE_PASSWORD;
	public static int SERVER_PORT;
	public static String SEPARATOR;
	public static String SEPARATOR_KORISNIK;
	public static String END;
	public static String LOGIN;
	public static String OK;
	public static String CHAT_LIST_ADD;
	public static String CHAT_LIST_REMOVE;
	public static String CHAT_MESSAGE;
	public static String BROADCAST_MESSAGE;
	public static String INVALID_LOGIN;
	public static String INVALID_REQUEST;

	public static void ucitajKonstante() {
		POCETNI_DIREKTORIJUM = PropertiesUtil.vratiSvojstvo("POCETNI_DIREKTORIJUM", String.class);
		RESURSI_DIREKTORIJUM = PropertiesUtil.vratiSvojstvo("RESURSI_DIREKTORIJUM", String.class);
		LOG_DIREKTORIJUM = PropertiesUtil.vratiSvojstvo("LOG_DIREKTORIJUM", String.class);
		LOG_DATOTEKA = PropertiesUtil.vratiSvojstvo("LOG_DATOTEKA", String.class);
		KEYSTORE_DIREKTORIJUM = PropertiesUtil.vratiSvojstvo("KEYSTORE_DIREKTORIJUM", String.class);
		KEYSTORE_PATH = PropertiesUtil.vratiSvojstvo("KEYSTORE_PATH", String.class);
		KEYSTORE_PASSWORD = PropertiesUtil.vratiSvojstvo("KEYSTORE_PASSWORD", String.class);
		SERVER_PORT = PropertiesUtil.vratiSvojstvo("SERVER_PORT", Integer.class);
		SEPARATOR = PropertiesUtil.vratiSvojstvo("SEPARATOR", String.class);
		SEPARATOR_KORISNIK = PropertiesUtil.vratiSvojstvo("SEPARATOR_KORISNIK", String.class);
		END = PropertiesUtil.vratiSvojstvo("END", String.class);
		LOGIN = PropertiesUtil.vratiSvojstvo("LOGIN", String.class);
		OK = PropertiesUtil.vratiSvojstvo("OK", String.class);
		CHAT_LIST_ADD = PropertiesUtil.vratiSvojstvo("CHAT_LIST_ADD", String.class);
		CHAT_LIST_REMOVE = PropertiesUtil.vratiSvojstvo("CHAT_LIST_REMOVE", String.class);
		CHAT_MESSAGE = PropertiesUtil.vratiSvojstvo("CHAT_MESSAGE", String.class);
		BROADCAST_MESSAGE = PropertiesUtil.vratiSvojstvo("BROADCAST_MESSAGE", String.class);
		INVALID_LOGIN = PropertiesUtil.vratiSvojstvo("INVALID_LOGIN", String.class);
		INVALID_REQUEST = PropertiesUtil.vratiSvojstvo("INVALID_REQUEST", String.class);
	}
}
