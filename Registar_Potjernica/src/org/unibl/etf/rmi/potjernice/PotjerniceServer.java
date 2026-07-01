package org.unibl.etf.rmi.potjernice;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.unibl.etf.utility.ConstantsUtil;
import org.unibl.etf.utility.FileLogger;

import java.rmi.registry.Registry;

import static org.unibl.etf.utility.ConstantsUtil.*;

public class PotjerniceServer implements PotjerniceInterface {

	public PotjerniceServer() throws RemoteException {
	}

	@Override
	public boolean naPotjernici(String id) throws RemoteException {
		boolean naPotjernici = false;
		List<String> potjernice = new ArrayList<>();
		try {
			potjernice = Files.readAllLines(Paths.get(RESOURCES_FOLDER, RESOURCES_FILE));
		} catch (IOException e) {
			FileLogger.log(Level.SEVERE, "Greska pri citanju fajla sa potjernicama.", e);
		}
		naPotjernici = potjernice.stream().anyMatch(p -> p.equals(id));
		return naPotjernici;
	}

	@Override
	public void ukloniSaPotjernice(String id) throws RemoteException {
		List<String> potjernice = new ArrayList<>();
		try {
			potjernice = Files.readAllLines(Paths.get(RESOURCES_FOLDER, RESOURCES_FILE));
		} catch (IOException e) {
			FileLogger.log(Level.SEVERE, "Greska pri citanju fajla sa potjernicama.", e);
		}

		potjernice = potjernice.stream().filter(p -> !p.equals(id)).collect(Collectors.toList());

		try {
			Files.write(Paths.get(RESOURCES_FOLDER, RESOURCES_FILE), potjernice);
		} catch (IOException e) {
			FileLogger.log(Level.SEVERE, "Greska pri upisu u fajl sa potjernicama.", e);
		}
	}

	public static void main(String args[]) {

		ConstantsUtil.ucitajKonstante();

		System.setProperty("java.security.policy", POLICY_FOLDER + File.separator + POLICY_FILE);

		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		try {
			PotjerniceServer server = new PotjerniceServer();

			PotjerniceInterface stub = (PotjerniceInterface) UnicastRemoteObject.exportObject(server, 0);

			Registry registry = LocateRegistry.createRegistry(RMI_PORT);

			registry.rebind(RMI_NAME, stub);

			System.out.println("RMI server \"potjernice\" je pokrenut.");

		} catch (Exception ex) {
			FileLogger.log(Level.SEVERE, "Greska pri pokretanju RMI servera.", ex);
		}
	}

}
