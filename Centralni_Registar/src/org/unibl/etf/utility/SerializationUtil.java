package org.unibl.etf.utility;

import java.beans.*;
import java.io.*;
//import java.net.*;
import java.nio.file.*;
import java.util.logging.Level;

import org.unibl.etf.model.TerminalDTO;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.google.gson.Gson;

public class SerializationUtil {

	private static SerializationUtil instance;
	private static String putanjaDirektorijuma;

	public static SerializationUtil getInstance() {
		if (instance == null) {
			instance = new SerializationUtil();
		}
		return instance;
	}

	private SerializationUtil() {

// 1. nacin

//		putanjaDirektorijuma = "terminals";
//		System.out.println("---: " + new File(putanjaDirektorijuma).getAbsolutePath());
// main(+):		F:\Edukacija\Programiranje\MDP\Projektni\Aktivno\Centralni_Registar\resources\terminals
// soap(-):		D:\Program Files\Oracle\eclipse-jee-2020-03-R-incubation-win32-x86_64\eclipse\resources\terminals

// 2. nacin

//		String resursiDirektorijum = PropertiesUtil.vratiSvojstvo("RESURSI_DIREKTORIJUM", String.class);
//		String terminaliDirektorijum = PropertiesUtil.vratiSvojstvo("TERMINALI_DIREKTORIJUM", String.class);
//		putanjaDirektorijuma = resursiDirektorijum + File.separator + terminaliDirektorijum;

// 3. nacin

		putanjaDirektorijuma = PropertiesUtil.vratiSvojstvo("TERMINALI_PUTANJA", String.class);

// 4. nacin (za serijalizaciju)

//		String imeFajla = "uMetodi_.out";
//		String resursiDirektorijum = PropertiesUtil.vratiSvojstvo("RESURSI_DIREKTORIJUM", String.class);
//		String terminaliDirektorijum = PropertiesUtil.vratiSvojstvo("TERMINALI_DIREKTORIJUM", String.class);
//		putanjaDirektorijuma = resursiDirektorijum + File.separator + terminaliDirektorijum;
//		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//
//// 4a.
//		URI uri = null;
//		try {
//			uri = classLoader.getResource(putanjaDirektorijuma).toURI();
//		} catch (URISyntaxException e) {
//			e.printStackTrace();
//		}
//		String putanjaDatoteke = Paths.get(uri).toString() + File.separator + imeFajla + ".xml";
//		File datoteka = new File(putanjaDatoteke);
//
//// 4b		
//		URI uri2 = null;
//		try {
//			uri2 = classLoader.getResource(putanjaDirektorijuma + File.separator + imeFajla + ".xml").toURI();
//			FileInputStream in = new FileInputStream(new File(Paths.get(uri2).toString()));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//// 4c.		
//		URL url = getClass().getClassLoader().getResource(putanjaDirektorijuma + File.separator + imeFajla);
//		String putanja = url.getPath().toString();

	}

	public static String vratiPutanjuDirektorijuma() {
		return putanjaDirektorijuma;
	}

	public void serijalizacija(TerminalDTO terminal) {
		String SERIJALIZACIJA_SEPARATOR = PropertiesUtil.vratiSvojstvo("SERIJALIZACIJA_SEPARATOR", String.class);
		String SERIJALIZACIJA_TIP_DATOTEKE = PropertiesUtil.vratiSvojstvo("SERIJALIZACIJA_TIP_DATOTEKE", String.class);
		String imeFajla = "id_" + terminal.getId() + SERIJALIZACIJA_SEPARATOR + terminal.getTipSerijalizacije()
				+ SERIJALIZACIJA_TIP_DATOTEKE;
		String putanja = putanjaDirektorijuma + File.separator + imeFajla;

		switch (terminal.getTipSerijalizacije()) {
		case "GSON":
			serializeWithGson(terminal, putanja);
			break;
		case "KRYO":
			serializeWithKryo(terminal, putanja);
			break;
		case "JAVA":
			serializeWithJava(terminal, putanja);
			break;
		case "XML":
			serializeWithXML(terminal, putanja, TerminalDTO.class);
			break;
		}
	}

	public TerminalDTO deserijalizacija(String idTerminala) {
		TerminalDTO terminal = null;
		String putanja = null;

		try {
			putanja = Files.list(Paths.get(putanjaDirektorijuma)).filter(p -> p.toString().contains(idTerminala))
					.findAny().get().toString();
			if (putanja.contains("GSON"))
				terminal = deserializeWithGson(putanja);
			else if (putanja.contains("KRYO"))
				terminal = deserializeWithKryo(putanja);
			else if (putanja.contains("JAVA"))
				terminal = deserializeWithJava(putanja);
			else if (putanja.contains("XML"))
				terminal = deserializeWithXML(putanja, TerminalDTO.class);
		} catch (IOException e) {
			FileLogger.log(Level.SEVERE, "Greska pri otvaranju direktorijuma!", e);
		}
		return terminal;
	}

	private static void serializeWithGson(TerminalDTO data, String putanja) {
		Gson gson = new Gson();
		try {
			FileWriter out = new FileWriter(new File(putanja));
			out.write(gson.toJson(data));
			out.close();
		} catch (IOException e) {
			FileLogger.log(Level.SEVERE, "Greska tokom Gson serijalizacije!", e);
		}
	}

	private static TerminalDTO deserializeWithGson(String putanja) {
		TerminalDTO terminal = null;
		Gson gson = new Gson();
		try {
			BufferedReader in = new BufferedReader(new FileReader(new File(putanja)));
			terminal = gson.fromJson(in.readLine(), TerminalDTO.class);
			in.close();
		} catch (IOException e) {
			FileLogger.log(Level.SEVERE, "Greska tokom Gson deserijalizacije!", e);
		}
		return terminal;
	}

	private static void serializeWithKryo(TerminalDTO data, String putanja) {
		Kryo kryo = new Kryo();
		kryo.register(TerminalDTO.class);
		try {
			Output out = new Output(new FileOutputStream(new File(putanja)));
			kryo.writeClassAndObject(out, data);
			out.close();
		} catch (Exception e) {
			FileLogger.log(Level.SEVERE, "Greska tokom Kryo serijalizacije!", e);
		}
	}

	private static TerminalDTO deserializeWithKryo(String putanja) {
		TerminalDTO terminal = null;
		Kryo kryo = new Kryo();
		kryo.register(TerminalDTO.class);
		try {
			Input in = new Input(new FileInputStream(new File(putanja)));
			terminal = (TerminalDTO) kryo.readClassAndObject(in);
			in.close();
		} catch (Exception e) {
			FileLogger.log(Level.SEVERE, "Greska tokom Kryo deserijalizacije!", e);
		}
		return terminal;
	}

	private static void serializeWithJava(TerminalDTO data, String putanja) {
		try {
			FileOutputStream fileOut = new FileOutputStream(putanja);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(data);
			out.close();
		} catch (Exception e) {
			FileLogger.log(Level.SEVERE, "Greska tokom Java serijalizacije!", e);
		}
	}

	private static TerminalDTO deserializeWithJava(String putanja) {
		TerminalDTO terminal = null;
		try {
			FileInputStream fileIn = new FileInputStream(putanja);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			terminal = (TerminalDTO) in.readObject();
			in.close();
		} catch (Exception e) {
			FileLogger.log(Level.SEVERE, "Greska tokom Java deserijalizacije!", e);
		}
		return terminal;
	}

	public static <T> void serializeWithXML(Object data, String putanja, Class<T> tip) {
		try {
			XMLEncoder encoder = new XMLEncoder(new FileOutputStream(new File(putanja)));
			encoder.writeObject(data);
			encoder.close();
		} catch (Exception e) {
			FileLogger.log(Level.SEVERE, "Greska tokom XML serijalizacije!", e);
		}
	}

	public static <T> T deserializeWithXML(String putanja, Class<T> tip) {
		T data = null;
		try {
			XMLDecoder decoder = new XMLDecoder(new FileInputStream(new File(putanja)));
			data = tip.cast(decoder.readObject());
			decoder.close();
		} catch (Exception e) {
			FileLogger.log(Level.SEVERE, "Greska tokom XML deserijalizacije!", e);
		}
		return data;
	}

}
