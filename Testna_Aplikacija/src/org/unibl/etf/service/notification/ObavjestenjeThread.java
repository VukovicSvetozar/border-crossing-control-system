package org.unibl.etf.service.notification;

import java.net.DatagramPacket;
import java.util.logging.Level;

import org.unibl.etf.controller.GlavnaStrana;
import org.unibl.etf.utility.FileLogger;

import static org.unibl.etf.utility.ConstantsUtil.*;
import static org.unibl.etf.controller.GlavnaStrana.*;

public class ObavjestenjeThread extends Thread {

	public static boolean pokrenut = true;

	public ObavjestenjeThread() {
	}

	@Override
	public void run() {
		byte[] buf = new byte[MAX_VELICINA];
		while (pokrenut) {
			DatagramPacket paket = new DatagramPacket(buf, buf.length);
			String obavjestenje;
			try {
				ObavjestenjeServis.obavjestenjeSocket.receive(paket);
				obavjestenje = new String(buf, 0, paket.getLength(), "UTF-8");
				azurirajObavjestenje(obavjestenje);
			} catch (Exception e) {
				FileLogger.log(Level.SEVERE, "Greska pri prijemu multicast paketa.", e);
			}
		}
	}

	private synchronized void azurirajObavjestenje(String primljenoObavjestenje) {
		String idProlazak = null;
		if (primljenoObavjestenje.startsWith(OBAVJESTENJE_INFO)) {
			idProlazak = primljenoObavjestenje.split(SEPARATOR_KORISNIK)[1];
			primljenoObavjestenje = primljenoObavjestenje.split(SEPARATOR_KORISNIK)[0];
		}
		try {
			switch (primljenoObavjestenje) {
			case "OTVOREN":
				otvorenTerminal = true;
				obavjestenje = "Prolaz je aktivan.";
				ispisiObavjestenje(obavjestenje);
				break;
			case "ZATVOREN":
				otvorenTerminal = false;
				obavjestenje += "\nTerminal je trenutno zatvoren.";
				ispisiObavjestenje(obavjestenje);
				break;
			case "INFO":
				GlavnaStrana.prikaziDetalje(idProlazak);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			FileLogger.log(Level.SEVERE, "Greska pri radu sa SOAP servisom!", e);
		}
	}

}
