package org.unibl.etf.rmi.potjernice;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PotjerniceInterface extends Remote {

	boolean naPotjernici(String id) throws RemoteException;

	void ukloniSaPotjernice(String id) throws RemoteException;

}
