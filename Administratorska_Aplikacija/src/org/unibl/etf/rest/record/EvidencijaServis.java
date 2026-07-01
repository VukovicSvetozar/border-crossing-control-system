package org.unibl.etf.rest.record;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONException;
import org.unibl.etf.utility.PropertiesUtil;

public class EvidencijaServis {

	private static String procitajPodatke(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	public static JSONArray evidentiraniPrestupnici() throws IOException, JSONException {
		String REST_BASE_URL = PropertiesUtil.vratiSvojstvo("REST_BASE_URL", String.class);
		String REST_POTJERNICE = PropertiesUtil.vratiSvojstvo("REST_POTJERNICE", String.class);
		InputStream is = new URL(REST_BASE_URL + REST_POTJERNICE).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = procitajPodatke(rd);
			JSONArray json = new JSONArray(jsonText);
			return json;
		} finally {
			is.close();
		}
	}

	public static JSONArray evidentiraniDokumenti() throws IOException, JSONException {
		String REST_BASE_URL = PropertiesUtil.vratiSvojstvo("REST_BASE_URL", String.class);
		String REST_DOKUMENTI = PropertiesUtil.vratiSvojstvo("REST_DOKUMENTI", String.class);
		InputStream is = new URL(REST_BASE_URL + REST_DOKUMENTI).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = procitajPodatke(rd);
			JSONArray json = new JSONArray(jsonText);
			return json;
		} finally {
			is.close();
		}
	}

}
