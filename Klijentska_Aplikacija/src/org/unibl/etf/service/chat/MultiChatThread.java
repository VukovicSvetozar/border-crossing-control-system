package org.unibl.etf.service.chat;

import java.net.DatagramPacket;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.logging.Level;

import org.unibl.etf.utility.FileLogger;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.ListView;
import static org.unibl.etf.utility.ConstantsUtil.*;

public class MultiChatThread extends Thread {

	public static boolean pokrenut = true;
	private ListView<String> lvMulticast;
	public ArrayList<String> sveMulticastPoruke;

	public MultiChatThread(ListView<String> lvMulticast) {
		this.lvMulticast = lvMulticast;
		this.sveMulticastPoruke = new ArrayList<String>();
	}

	@Override
	public void run() {
		byte[] buf = new byte[MAX_VELICINA];
		while (pokrenut) {
			DatagramPacket paket = new DatagramPacket(buf, buf.length);
			String poruka;
			try {
				MultiChatServis.multicastSocket.receive(paket);
				poruka = new String(buf, 0, paket.getLength(), "UTF-8");
				azurirajListuPoruka(poruka);
			} catch (Exception e) {
				FileLogger.log(Level.SEVERE, "Greska pri prijemu multicast paketa.", e);
			}
		}
	}

	public void azurirajListuPoruka(String primljenaPoruka) {
		String trenutnoVrijeme = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).toString().replace("T", "_");
		String[] parametri = primljenaPoruka.split(SEPARATOR);
		String posiljalac = parametri[0];
		String modifikovaniTekst = parametri[1];
		String originalniTekst = modifikovaniTekst.replace(SEPARATOR_MC, "\n");
		if (!MULTICAST_END.equals(originalniTekst)) {
			String poruka = posiljalac + " (" + trenutnoVrijeme + "): " + originalniTekst;
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					sveMulticastPoruke.add(poruka);
					lvMulticast.setItems(FXCollections.observableArrayList(sveMulticastPoruke));
				}
			});
		}
	}

}
