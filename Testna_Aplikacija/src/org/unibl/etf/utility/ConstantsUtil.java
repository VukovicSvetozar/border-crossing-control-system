package org.unibl.etf.utility;

public class ConstantsUtil {

	public static String POCETNI_DIREKTORIJUM;
	public static String RESURSI_DIREKTORIJUM;
	public static String KEYSTORE_DIREKTORIJUM;
	public static String LOG_DIREKTORIJUM;
	public static String LOG_DATOTEKA;
	public static String REST_BASE_URL;
	public static int SERVER_PORT;
	public static String SERVER_ADRESA;
	public static String TRUSTSTORE_PATH;
	public static String TRUSTSTORE_PASSWORD;
	public static String END;
	public static String MULTICAST_ADRESA;
	public static int MULTICAST_PORT;
	public static String MULTICAST_END;
	public static int MAX_VELICINA;
	public static String CHAT_MESSAGE;
	public static String BROADCAST_MESSAGE;
	public static String SEPARATOR;
	public static String CHAT_LIST_ADD;
	public static String CHAT_LIST_REMOVE;
	public static String SEPARATOR_KORISNIK;
	public static String LOGIN;
	public static String OK;
	public static String SEPARATOR_MC;
	public static String OBAVJESTENJE_ADRESA;
	public static int OBAVJESTENJE_PORT;
	public static String OBAVJESTENJE_INFO;
	public static String OBAVJESTENJE_END;

	public static void ucitajKonstante() {
		POCETNI_DIREKTORIJUM = PropertiesUtil.vratiSvojstvo("POCETNI_DIREKTORIJUM", String.class);
		RESURSI_DIREKTORIJUM = PropertiesUtil.vratiSvojstvo("RESURSI_DIREKTORIJUM", String.class);
		KEYSTORE_DIREKTORIJUM = PropertiesUtil.vratiSvojstvo("KEYSTORE_DIREKTORIJUM", String.class);
		LOG_DIREKTORIJUM = PropertiesUtil.vratiSvojstvo("LOG_DIREKTORIJUM", String.class);
		LOG_DATOTEKA = PropertiesUtil.vratiSvojstvo("LOG_DATOTEKA", String.class);
		REST_BASE_URL = PropertiesUtil.vratiSvojstvo("REST_BASE_URL", String.class);
		SERVER_PORT = PropertiesUtil.vratiSvojstvo("SERVER_PORT", Integer.class);
		SERVER_ADRESA = PropertiesUtil.vratiSvojstvo("SERVER_ADRESA", String.class);
		TRUSTSTORE_PATH = PropertiesUtil.vratiSvojstvo("TRUSTSTORE_PATH", String.class);
		TRUSTSTORE_PASSWORD = PropertiesUtil.vratiSvojstvo("TRUSTSTORE_PASSWORD", String.class);
		END = PropertiesUtil.vratiSvojstvo("END", String.class);
		MULTICAST_ADRESA = PropertiesUtil.vratiSvojstvo("MULTICAST_ADRESA", String.class);
		MULTICAST_PORT = PropertiesUtil.vratiSvojstvo("MULTICAST_PORT", Integer.class);
		MULTICAST_END = PropertiesUtil.vratiSvojstvo("MULTICAST_END", String.class);
		MAX_VELICINA = PropertiesUtil.vratiSvojstvo("MAX_VELICINA", Integer.class);
		CHAT_MESSAGE = PropertiesUtil.vratiSvojstvo("CHAT_MESSAGE", String.class);
		BROADCAST_MESSAGE = PropertiesUtil.vratiSvojstvo("BROADCAST_MESSAGE", String.class);
		SEPARATOR = PropertiesUtil.vratiSvojstvo("SEPARATOR", String.class);
		CHAT_LIST_ADD = PropertiesUtil.vratiSvojstvo("CHAT_LIST_ADD", String.class);
		CHAT_LIST_REMOVE = PropertiesUtil.vratiSvojstvo("CHAT_LIST_REMOVE", String.class);
		SEPARATOR_KORISNIK = PropertiesUtil.vratiSvojstvo("SEPARATOR_KORISNIK", String.class);
		LOGIN = PropertiesUtil.vratiSvojstvo("LOGIN", String.class);
		OK = PropertiesUtil.vratiSvojstvo("OK", String.class);
		SEPARATOR_MC = PropertiesUtil.vratiSvojstvo("SEPARATOR_MC", String.class);
		OBAVJESTENJE_ADRESA = PropertiesUtil.vratiSvojstvo("OBAVJESTENJE_ADRESA", String.class);
		OBAVJESTENJE_PORT = PropertiesUtil.vratiSvojstvo("OBAVJESTENJE_PORT", Integer.class);
		OBAVJESTENJE_INFO = PropertiesUtil.vratiSvojstvo("OBAVJESTENJE_INFO", String.class);
		OBAVJESTENJE_END = PropertiesUtil.vratiSvojstvo("OBAVJESTENJE_END", String.class);
	}
}
