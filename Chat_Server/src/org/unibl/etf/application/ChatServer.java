package org.unibl.etf.application;

import java.io.File;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;

import javax.net.ssl.*;

import org.unibl.etf.model.ChatKorisnikDTO;
import org.unibl.etf.server.ServerThread;
import org.unibl.etf.utility.ConstantsUtil;
import org.unibl.etf.utility.FileLogger;

import static org.unibl.etf.utility.ConstantsUtil.*;

public class ChatServer {

	public static ArrayList<ChatKorisnikDTO> korisnici = new ArrayList<>();

	public static void main(String[] args) {
		ConstantsUtil.ucitajKonstante();
		System.setProperty("javax.net.ssl.keyStore", POCETNI_DIREKTORIJUM + File.separator + RESURSI_DIREKTORIJUM
				+ File.separator + KEYSTORE_DIREKTORIJUM + File.separator + KEYSTORE_PATH);
		System.setProperty("javax.net.ssl.keyStorePassword", KEYSTORE_PASSWORD);
		System.setProperty("https.protocols", "TLSv1");
		SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
		try {
			SSLServerSocket ss = (SSLServerSocket) ssf.createServerSocket(SERVER_PORT);
			System.out.println("Chat server je pokrenut.");
			while (true) {
				Socket sock = ss.accept();
				new ServerThread(sock).start();
			}
		} catch (Exception ex) {
			FileLogger.log(Level.SEVERE, "Greska pri pokretanju chat servera.", ex);
		}
	}

}
