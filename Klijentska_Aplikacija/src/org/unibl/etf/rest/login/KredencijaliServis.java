package org.unibl.etf.rest.login;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.util.logging.Level;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.unibl.etf.model.KorisnikDTO;
import org.unibl.etf.utility.FileLogger;

import static org.unibl.etf.utility.ConstantsUtil.*;

public class KredencijaliServis {

	private static String procitajPodatke(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	public static JSONObject korisnik(String korisnickoIme) throws IOException, JSONException {
		InputStream is = new URL(REST_BASE_URL + korisnickoIme).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = procitajPodatke(rd);
			JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			is.close();
		}
	}

	public static JSONArray sviKorisnici() throws IOException, JSONException {
		InputStream is = new URL(REST_BASE_URL).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = procitajPodatke(rd);
			JSONArray json = new JSONArray(jsonText);
			return json;
		} finally {
			is.close();
		}
	}

	public static JSONArray korisniciTerminala(String idTerminal) throws IOException, JSONException {
		InputStream is = new URL(REST_BASE_URL + "terminali" + File.separator + idTerminal).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = procitajPodatke(rd);
			JSONArray json = new JSONArray(jsonText);
			return json;
		} finally {
			is.close();
		}
	}

	public static JSONArray korisniciProlaza(String idProlaz) throws IOException, JSONException {
		InputStream is = new URL(REST_BASE_URL + "prolazi" + File.separator + idProlaz).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = procitajPodatke(rd);
			JSONArray json = new JSONArray(jsonText);
			return json;
		} finally {
			is.close();
		}
	}

	public static String provjeraKredencijala(KorisnikDTO korisnik) {
		String poruka = null;
		try {
			URL url = new URL(REST_BASE_URL + "autentifikacija");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");

			JSONObject input = new JSONObject(korisnik);
			OutputStream os = conn.getOutputStream();
			os.write(input.toString().getBytes());
			os.flush();

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			poruka = br.readLine();

			os.close();
			br.close();
			conn.disconnect();
		} catch (Exception e) {
			FileLogger.log(Level.SEVERE, "Greska pri provjeri kredencijala!", e);
		}
		return poruka;
	}

}
