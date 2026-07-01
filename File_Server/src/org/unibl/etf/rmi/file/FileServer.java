package org.unibl.etf.rmi.file;

import static org.unibl.etf.utility.ConstantsUtil.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.unibl.etf.utility.ConstantsUtil;
import org.unibl.etf.utility.FileLogger;

public class FileServer implements FileInterface {

	public FileServer() throws RemoteException {
		super();
	}

	@Override
	public String slanjeFajla(String putanjaDirektorijuma, File zipFajl) throws RemoteException {
		File folderRoot = new File(DOCUMENTS_FOLDER);
		if (!folderRoot.exists()) {
			folderRoot.mkdir();
		}
		File folderLokacija = new File(DOCUMENTS_FOLDER + File.separator + putanjaDirektorijuma);
		if (!folderLokacija.exists()) {
			folderLokacija.mkdir();
		}
		try {
			FileInputStream fis = null;
			FileOutputStream fos = null;
			fis = new FileInputStream(zipFajl);
			File noviFajl = new File(folderLokacija + File.separator + zipFajl.getName());
			fos = new FileOutputStream(noviFajl);
			byte[] buffer = new byte[5 * 1024 * 1024];
			int duzina = 0;
			while ((duzina = fis.read(buffer)) > 0) {
				fos.write(buffer, 0, duzina);
			}
			unzipFile(DOCUMENTS_FOLDER + File.separator + putanjaDirektorijuma, zipFajl);
			fis.close();
			fos.close();
			return OK_MESSAGE;
		} catch (IOException e) {
			FileLogger.log(Level.SEVERE, "Greska u radu sa I/O.", e);
		}
		return ERROR_MESSAGE;
	}

	private void unzipFile(String putanjaDirektorijuma, File zipFajl) throws IOException {
		byte[] buffer = new byte[1024];
		ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFajl));
		ZipEntry zipEntry = zis.getNextEntry();
		while (zipEntry != null) {
			File newFile = newFile(new File(putanjaDirektorijuma), zipEntry);
			File parent = newFile.getParentFile();
			if (!parent.isDirectory() && !parent.mkdirs()) {
				FileLogger.log(Level.SEVERE, "Greska pri kreiranju direktorijuma: " + parent, null);
			}
			FileOutputStream fos = new FileOutputStream(newFile);
			int len;
			while ((len = zis.read(buffer)) > 0) {
				fos.write(buffer, 0, len);
			}
			fos.close();
			zipEntry = zis.getNextEntry();
		}
		zis.closeEntry();
		zis.close();
	}

	private static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
		File destFile = new File(destinationDir, zipEntry.getName());
		String destDirPath = destinationDir.getCanonicalPath();
		String destFilePath = destFile.getCanonicalPath();
		if (!destFilePath.startsWith(destDirPath + File.separator))
			FileLogger.log(Level.SEVERE, "Unos je izvan ciljnog direktorijuma: " + zipEntry.getName(), null);
		return destFile;
	}

	public static void main(String[] args) {
		ConstantsUtil.ucitajKonstante();

		System.setProperty("java.security.policy", POLICY_FOLDER + File.separator + POLICY_FILE);
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		try {
			FileServer server = new FileServer();
			FileInterface stub = (FileInterface) UnicastRemoteObject.exportObject(server, 0);
			Registry registry = LocateRegistry.createRegistry(RMI_PORT);
			registry.rebind(RMI_NAME, stub);
			System.out.println("RMI server \"file\" je pokrenut.");
		} catch (Exception ex) {
			FileLogger.log(Level.SEVERE, "Greska pri pokretanju RMI servera.", ex);
		}
	}

}
