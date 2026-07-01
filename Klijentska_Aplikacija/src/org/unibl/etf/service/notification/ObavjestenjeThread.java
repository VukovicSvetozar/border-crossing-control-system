package org.unibl.etf.service.notification;

import java.net.DatagramPacket;
import java.util.logging.Level;

import org.unibl.etf.controller.GlavnaStranaCarina;
import org.unibl.etf.controller.GlavnaStranaPolicija;
import org.unibl.etf.model.TipKontrole;
import org.unibl.etf.utility.FileLogger;

import static org.unibl.etf.utility.ConstantsUtil.*;
import static org.unibl.etf.controller.Prijava.*;


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
	
	private void azurirajObavjestenje(String obavjestenje) {
		try {
			switch (obavjestenje) {
			case "OTVOREN":
				otvorenTerminal = true;
				if (tipKontrole == TipKontrole.POLICIJSKA)
					GlavnaStranaPolicija.ispisiObavjestenje("Terminal je aktivan.");
				else
					GlavnaStranaCarina.ispisiObavjestenje("Terminal je aktivan.");
				break;
			case "ZATVOREN":
				otvorenTerminal = false;
				if (tipKontrole == TipKontrole.POLICIJSKA)
					GlavnaStranaPolicija.ispisiObavjestenje("Terminal je zatvoren.");
				else
					GlavnaStranaCarina.ispisiObavjestenje("Terminal je zatvoren.");
				break;
			default:
				break;
			}
		} catch (Exception e) {
			FileLogger.log(Level.SEVERE, "Greska pri radu sa SOAP servisom!", e);
		}
	}

}
