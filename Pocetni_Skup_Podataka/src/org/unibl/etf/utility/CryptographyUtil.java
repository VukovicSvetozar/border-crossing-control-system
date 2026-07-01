package org.unibl.etf.utility;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;
import java.util.logging.Level;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class CryptographyUtil {

	private static final int BROJ_ITERACIJA = 10000;
	private static final int DUZINA_KLJUCA = 256;
	private static final Random RANDOM = new SecureRandom();
	private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

	public static boolean verifikacijaKorisnickeLozinke(String unesenaLozinka, String zasticenaLozinka, String salt) {
		boolean povratnaVrijednost = false;
		byte[] hesVrijednost = vratiHes(unesenaLozinka.toCharArray(), salt.getBytes());
		String novaZasticenaLozinka = Base64.getEncoder().encodeToString(hesVrijednost);
		povratnaVrijednost = novaZasticenaLozinka.equalsIgnoreCase(zasticenaLozinka);
		return povratnaVrijednost;
	}

	private static byte[] vratiHes(char[] lozinka, byte[] salt) {
		PBEKeySpec specifikacija = new PBEKeySpec(lozinka, salt, BROJ_ITERACIJA, DUZINA_KLJUCA);
		Arrays.fill(lozinka, Character.MIN_VALUE);
		try {
			SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
			return skf.generateSecret(specifikacija).getEncoded();
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			FileLogger.log(Level.SEVERE, "Greska se javlja tokom hesovanja lozinke!", e);
			return null;
		} finally {
			specifikacija.clearPassword();
		}
	}

	public static String getSalt(int length) {
		StringBuilder returnValue = new StringBuilder(length);
		for (int i = 0; i < length; i++)
			returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
		return new String(returnValue);
	}

	public static String getSigurnaLozinka(String lozinka, String salt) {
		String povratnaVrijednost = null;
		byte[] sigurnaLozinka = hash(lozinka.toCharArray(), salt.getBytes());
		povratnaVrijednost = Base64.getEncoder().encodeToString(sigurnaLozinka);
		return povratnaVrijednost;
	}

	private static byte[] hash(char[] lozinka, byte[] salt) {
		PBEKeySpec spec = new PBEKeySpec(lozinka, salt, BROJ_ITERACIJA, DUZINA_KLJUCA);
		Arrays.fill(lozinka, Character.MIN_VALUE);
		try {
			SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
			return skf.generateSecret(spec).getEncoded();
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new AssertionError("Greska tokom hesovanja lozinke: " + e.getMessage(), e);
		} finally {
			spec.clearPassword();
		}
	}

}
