package org.unibl.etf.service.chat;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.stream.Collectors;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.unibl.etf.model.ChatKorisnikDTO;
import org.unibl.etf.utility.FileLogger;

import javafx.scene.control.TextArea;

import static org.unibl.etf.utility.ConstantsUtil.*;

public class ChatServis {

	public BufferedReader in;
	public PrintWriter out;
	public Socket sock;

	private static ChatServis chatServis;

	public static ChatServis getChatServis() {
		if (chatServis == null)
			chatServis = new ChatServis();
		return chatServis;
	}

	public void ukljuciChat() {
		try {
			System.setProperty("javax.net.ssl.trustStore", POCETNI_DIREKTORIJUM + File.separator + RESURSI_DIREKTORIJUM
					+ File.separator + KEYSTORE_DIREKTORIJUM + File.separator + TRUSTSTORE_PATH);
			System.setProperty("javax.net.ssl.trustStorePassword", TRUSTSTORE_PASSWORD);
			System.setProperty("https.protocols", "TLSv1");

			InetAddress addr = InetAddress.getByName(SERVER_ADRESA);
			SSLSocketFactory sf = (SSLSocketFactory) SSLSocketFactory.getDefault();
			sock = (SSLSocket) sf.createSocket(addr, SERVER_PORT);
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sock.getOutputStream())), true);
		} catch (Exception e) {
			FileLogger.log(Level.SEVERE, "Greska pri konekciji.", e);
		}
	}

	public static boolean prijavaNaChat(String korisnickoIme) {
		boolean uspjesno = false;
		try {
			ChatServis.getChatServis().out.println(LOGIN + SEPARATOR + korisnickoIme);
			String response = ChatServis.getChatServis().in.readLine();
			if (OK.equals(response))
				uspjesno = true;
		} catch (IOException e) {
			FileLogger.log(Level.SEVERE, "Greska prilikom prijave na chat!", e);
		}
		return uspjesno;
	}

	public void upisiSeUChatListu() {
		ChatServis.getChatServis().out.println(CHAT_LIST_ADD);
	}

	public void odjaviSeIzChatListe() {
		ChatServis.getChatServis().out.println(CHAT_LIST_REMOVE);
	}

	public static void posaljiChatPoruku(String korisnickoImePrimaoca, String poruka) {
		chatServis.out.println(CHAT_MESSAGE + SEPARATOR + korisnickoImePrimaoca + SEPARATOR + poruka);
	}

	public static void posaljiBroadcastPoruku(String poruka) {
		chatServis.out.println(BROADCAST_MESSAGE + SEPARATOR + poruka);
	}

	public void prekiniChat() {
		out.println(END);
	}

	public void zatvoriResurse() {
		try {
			in.close();
			out.close();
			sock.close();
		} catch (IOException e) {
			FileLogger.log(Level.SEVERE, "Greska pri diskonekciji.", e);
		}
	}

	public void prikaziDetalje(ChatKorisnikDTO chatKorisnik, TextArea taChatPoruke,
			HashMap<ChatKorisnikDTO, ArrayList<String>> mapaChatPoruka) {
		if (chatKorisnik != null && mapaChatPoruka.containsKey(chatKorisnik)) {
			String poruke = mapaChatPoruka.get(chatKorisnik).stream().collect(Collectors.joining("\n"));
			taChatPoruke.setText(poruke);
		} else
			taChatPoruke.setText("");
	}

}
