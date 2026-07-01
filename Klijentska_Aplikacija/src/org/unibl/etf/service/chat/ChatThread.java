package org.unibl.etf.service.chat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import org.unibl.etf.controller.Prijava;
import org.unibl.etf.model.ChatKorisnikDTO;
import org.unibl.etf.utility.FileLogger;

import static org.unibl.etf.utility.ConstantsUtil.*;

public class ChatThread extends Thread {

	private static boolean pokrenut;
	private ListView<ChatKorisnikDTO> listaChatKorisnika;
	private TextArea listaChatPoruka;
	private ListView<String> listaBroadcastPoruka;
	public HashMap<ChatKorisnikDTO, ArrayList<String>> sveChatPoruke;
	private ArrayList<String> sveBroadcastPoruke;

	public ChatThread(ListView<ChatKorisnikDTO> listaKorisnika, TextArea listaChatPoruka,
			HashMap<ChatKorisnikDTO, ArrayList<String>> sveChatPoruke, ListView<String> listaBroadcastPoruka) {
		pokrenut = true;
		this.listaChatKorisnika = listaKorisnika;
		this.listaChatPoruka = listaChatPoruka;
		this.listaBroadcastPoruka = listaBroadcastPoruka;
		this.sveChatPoruke = sveChatPoruke;
		sveBroadcastPoruke = new ArrayList<>();
	}

	@Override
	public void run() {
		ChatServis.getChatServis().upisiSeUChatListu();
		while (pokrenut) {
			String odgovor;
			try {
				odgovor = ChatServis.getChatServis().in.readLine();
				if (odgovor == null) {
					odgovor = "";
				}
				if (odgovor.startsWith(CHAT_LIST_ADD) || odgovor.startsWith(CHAT_LIST_REMOVE)) {
					String[] parametri = odgovor.split(SEPARATOR);
					if (parametri.length == 2) {
						azurirajListuChatKorisnika(parametri[1]);
					}
				} else if (odgovor.startsWith(CHAT_MESSAGE)) {
					String[] parametri = odgovor.split(SEPARATOR);
					if (parametri.length == 3) {
						azurirajListuChatPoruka(parametri[1], parametri[2]);
					}
				} else if (odgovor.startsWith(BROADCAST_MESSAGE)) {
					String[] parametri = odgovor.split(SEPARATOR);
					if (parametri.length == 2) {
						azurirajListuBroadcastPoruka(parametri[1]);
					}
				} else if (odgovor.startsWith(END)) {
					pokrenut = false;
					ChatServis.getChatServis().zatvoriResurse();
				}
			} catch (IOException e) {
				FileLogger.log(Level.SEVERE, "Greska prilikom protokola!", e);
			}
		}
	}

	private void azurirajListuChatKorisnika(String odgovor) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				ArrayList<ChatKorisnikDTO> korisnici = vratiChatKorisnike(odgovor);
				listaChatKorisnika.setItems(FXCollections.observableArrayList(korisnici));
				if (korisnici.isEmpty()) {
					listaChatKorisnika.setPlaceholder(new Label("Nema korisnika."));
					listaChatPoruka.setPromptText("Nema poruka");
				}
			}
		});
	}

	private static ArrayList<ChatKorisnikDTO> vratiChatKorisnike(String odgovor) {
		ArrayList<ChatKorisnikDTO> listaKorisnika = new ArrayList<>();
		String[] imenaKorisnika = odgovor.split(SEPARATOR_KORISNIK);
		for (String korisnickoIme : imenaKorisnika)
			if (!korisnickoIme.equals(Prijava.aktivniKorisnik.getKorisnickoIme()))
				listaKorisnika.add(new ChatKorisnikDTO(korisnickoIme));
		return listaKorisnika;
	}

	public void azurirajListuChatPoruka(String korisnickoIme, String poruka) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				ChatKorisnikDTO korisnik = new ChatKorisnikDTO(korisnickoIme);
				if (!sveChatPoruke.containsKey(korisnik)) {
					sveChatPoruke.put(korisnik, new ArrayList<String>());
				}
				sveChatPoruke.get(korisnik).add(poruka);
				ChatServis.getChatServis().prikaziDetalje(korisnik, listaChatPoruka, sveChatPoruke);
			}
		});
	}

	private void azurirajListuBroadcastPoruka(String poruka) {
		sveBroadcastPoruke.add(poruka);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (sveBroadcastPoruke != null) {
					listaBroadcastPoruka.setItems(FXCollections.observableArrayList(sveBroadcastPoruke));
				}
			}
		});
	}

}
