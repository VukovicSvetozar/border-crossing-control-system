package org.unibl.etf.server;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.unibl.etf.application.ChatServer;
import org.unibl.etf.model.ChatKorisnikDTO;
import org.unibl.etf.utility.FileLogger;

import static org.unibl.etf.utility.ConstantsUtil.*;

public class ServerThread extends Thread {

	private Socket sock;
	private BufferedReader in;
	private PrintWriter out;
	private ChatKorisnikDTO korisnik;

	public ServerThread(Socket sock) {
		this.sock = sock;
		try {
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sock.getOutputStream())), true);
		} catch (Exception ex) {
			FileLogger.log(Level.SEVERE, "Greska pri kreiranju chat serverske niti.", ex);
		}
	}

	@Override
	public void run() {
		String zahtjev;
		try {
			while (!END.equals(zahtjev = in.readLine())) {
				try {
					if (zahtjev == null) {
						zahtjev = "";
					} else if (zahtjev.startsWith(LOGIN)) {
						String[] parametri = zahtjev.split(SEPARATOR);
						boolean status = false;
						if (parametri.length == 2) {
							status = prijava(parametri[1]);
						}
						if (!status) {
							out.println(INVALID_LOGIN);
						} else {
							out.println(OK);
						}
					} else if (CHAT_LIST_ADD.startsWith(zahtjev)) {
						posaljiListuChatKorisnika(true);
					} else if (CHAT_LIST_REMOVE.startsWith(zahtjev)) {
						posaljiListuChatKorisnika(false);
					} else if (zahtjev.startsWith(CHAT_MESSAGE)) {
						String[] parametri = zahtjev.split(SEPARATOR);
						if (parametri.length == 3) {
							posaljiChatPoruku(korisnik.getKorisnickoIme(), parametri[1], parametri[2]);
						} else {
							out.println(INVALID_REQUEST);
						}
					} else if (zahtjev.startsWith(BROADCAST_MESSAGE)) {
						String[] parametri = zahtjev.split(SEPARATOR);
						if (parametri.length == 2) {
							posaljiBroadcastPoruku(korisnik.getKorisnickoIme(), parametri[1]);
						} else {
							out.println(INVALID_REQUEST);
						}
					}
				} catch (Exception ex) {
					FileLogger.log(Level.SEVERE, "Greska u protokolu.", ex);
				}
			}
			odjava();
		} catch (IOException ex) {
			FileLogger.log(Level.SEVERE, "Greska pri zatvaranju chat konekcije.", ex);
		}

	}

	private boolean prijava(String korisnickoIme) {
		korisnik = new ChatKorisnikDTO(korisnickoIme, out);
		if (!ChatServer.korisnici.contains(korisnik)) {
			ChatServer.korisnici.add(korisnik);
			return true;
		}
		return false;
	}

	private void posaljiListuChatKorisnika(boolean prijava) {
		String tipPoruke;
		if (prijava) {
			tipPoruke = CHAT_LIST_ADD;
		} else {
			tipPoruke = CHAT_LIST_REMOVE;
			ChatServer.korisnici.remove(korisnik);
		}
		String imenaChatKorisnika = vratiListuKorisnickihImena();
		for (ChatKorisnikDTO primalac : ChatServer.korisnici) {
			PrintWriter out = primalac.getPrintWriter();
			out.println(tipPoruke + SEPARATOR + imenaChatKorisnika);
		}
	}

	private String vratiListuKorisnickihImena() {
		return ChatServer.korisnici.stream().map(chatKorisnik -> chatKorisnik.toString())
				.collect(Collectors.joining(SEPARATOR_KORISNIK));
	}

	public void posaljiChatPoruku(String posiljalac, String primalac, String poruka) {
		int index = ChatServer.korisnici.indexOf(new ChatKorisnikDTO(primalac));
		if (index > -1) {
			ChatKorisnikDTO korisnik = ChatServer.korisnici.get(index);
			PrintWriter out = korisnik.getPrintWriter();
			out.println(CHAT_MESSAGE + SEPARATOR + posiljalac + SEPARATOR + poruka);
		}
	}

	private void posaljiBroadcastPoruku(String korisnickoImePosiljaoca, String tekstPoruke) {
		String trenutnoVrijeme = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).toString().replace("T", "_");
		String poruka = korisnickoImePosiljaoca + " (" + trenutnoVrijeme + "): " + tekstPoruke;
		for (ChatKorisnikDTO primalac : ChatServer.korisnici) {
			PrintWriter out = primalac.getPrintWriter();
			out.println(BROADCAST_MESSAGE + SEPARATOR + poruka);
		}
	}

	private void odjava() throws IOException {
		out.println(END);
		in.close();
		out.close();
		sock.close();
	}

}
