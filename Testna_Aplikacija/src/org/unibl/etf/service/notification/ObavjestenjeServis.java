package org.unibl.etf.service.notification;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.logging.Level;

import org.unibl.etf.utility.FileLogger;

import static org.unibl.etf.utility.ConstantsUtil.*;

public class ObavjestenjeServis {

	public static MulticastSocket obavjestenjeSocket;
	public static InetAddress obavjestenjeSocketAddress;
	public static int obavjestenjePort;

	public static void omoguciObavjestenje(int idTerminal) {
		try {
			obavjestenjePort = OBAVJESTENJE_PORT + idTerminal;
			obavjestenjeSocketAddress = InetAddress.getByName(OBAVJESTENJE_ADRESA);
			obavjestenjeSocket = new MulticastSocket(obavjestenjePort);
			obavjestenjeSocket.joinGroup(obavjestenjeSocketAddress);
		} catch (IOException e) {
			FileLogger.log(Level.SEVERE, "Greska pri pridruzivanju multicast grupi.", e);
		}
	}

	private static void posaljiObavjestenje(String poruka) {
		String porukaZaSlanje = poruka.replace("\n", SEPARATOR_MC);
		byte[] buf = porukaZaSlanje.getBytes();
		DatagramPacket paket = new DatagramPacket(buf, buf.length, obavjestenjeSocketAddress, obavjestenjePort);
		try {
			obavjestenjeSocket.send(paket);
		} catch (IOException e) {
			FileLogger.log(Level.SEVERE, "Greska pri slanju multicast poruke.", e);
		}
	}

	public static void zatvoriMulticastSocket() {
		try {
			ObavjestenjeThread.pokrenut = false;
			ObavjestenjeServis.posaljiObavjestenje(OBAVJESTENJE_END);
			obavjestenjeSocket.leaveGroup(obavjestenjeSocketAddress);
			obavjestenjeSocket.close();
		} catch (IOException e) {
			FileLogger.log(Level.SEVERE, "Greska pri zatvaranju multicast soketa.", e);
		}
	}

}
