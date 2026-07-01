package org.unibl.etf.rmi.file;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FileInterface extends Remote {

	public String slanjeFajla(String imeDirektorijuma, File zipFajl) throws RemoteException;

}
