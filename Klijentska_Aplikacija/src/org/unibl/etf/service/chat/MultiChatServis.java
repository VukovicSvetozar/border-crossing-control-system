package org.unibl.etf.service.chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.logging.Level;

import org.unibl.etf.utility.FileLogger;

import static org.unibl.etf.utility.ConstantsUtil.*;

public class MultiChatServis {

	public static MulticastSocket multicastSocket;
	public static InetAddress multicastSocketAddress;
	public static int multicastPort;
	public static String imeKorisnika;

	public static void omoguciMulticast(int idTerminal, String imeKorisnika) {
		try {
			multicastPort = MULTICAST_PORT + idTerminal;
			multicastSocketAddress = InetAddress.getByName(MULTICAST_ADRESA);
			multicastSocket = new MulticastSocket(multicastPort);
			multicastSocket.joinGroup(multicastSocketAddress);
			MultiChatServis.imeKorisnika = imeKorisnika;
		} catch (IOException e) {
			FileLogger.log(Level.SEVERE, "Greska pri pridruzivanju multicast grupi.", e);
		}
	}

	public static void posaljiMulticastPoruku(String poruka) {
		String modifikovanaPoruka = poruka.replace("\n", SEPARATOR_MC);
		String porukaZaSlanje = imeKorisnika + SEPARATOR + modifikovanaPoruka;
		byte[] buf = porukaZaSlanje.getBytes();
		DatagramPacket paket = new DatagramPacket(buf, buf.length, multicastSocketAddress, multicastPort);
		try {
			multicastSocket.send(paket);
		} catch (IOException e) {
			FileLogger.log(Level.SEVERE, "Greska pri slanju multicast poruke.", e);
		}
	}

	public static void zatvoriMulticastSocket() {
		try {
			MultiChatThread.pokrenut = false;
			MultiChatServis.posaljiMulticastPoruku(MULTICAST_END);
			multicastSocket.leaveGroup(multicastSocketAddress);
			multicastSocket.close();
		} catch (IOException e) {
			FileLogger.log(Level.SEVERE, "Greska pri zatvaranju multicast soketa.", e);
		}
	}

}
