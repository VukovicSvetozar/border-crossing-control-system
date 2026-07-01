package org.unibl.etf.rmi.potjernice;

import java.io.File;
import java.rmi.registry.*;
import java.util.logging.Level;

import org.unibl.etf.utility.FileLogger;

import static org.unibl.etf.utility.ConstantsUtil.*;

public class PotjerniceKlijent {

	private static PotjerniceKlijent potjerniceKlijent;
	private PotjerniceInterface potjernice;

	public static PotjerniceKlijent getPotjerniceKlijent() {
		if (potjerniceKlijent == null)
			potjerniceKlijent = new PotjerniceKlijent();
		return potjerniceKlijent;
	}

	public PotjerniceKlijent() {
		System.setProperty("java.security.policy", POCETNI_DIREKTORIJUM + File.separator + RESURSI_DIREKTORIJUM
				+ File.separator + POLICY_DIREKTORIJUM + File.separator + POLICY_DATOTEKA);
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		try {
			Registry registry = LocateRegistry.getRegistry(RMI_PORT_POTJERNICA);
			potjernice = (PotjerniceInterface) registry.lookup(RMI_NAME_POTJERNICA);
		} catch (Exception ex) {
			FileLogger.log(Level.SEVERE, "Greska pri koriscenju RMI servera.", ex);
		}
	}

	public PotjerniceInterface getPotjernice() {
		return potjernice;
	}

	public void setPotjernice(PotjerniceInterface potjernice) {
		this.potjernice = potjernice;
	}

}
