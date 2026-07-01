package org.unibl.etf.model;

import java.io.*;

public class ChatKorisnikDTO implements Serializable {

	private static final long serialVersionUID = -3496565946163344747L;

	private String korisnickoIme;
	private PrintWriter printWriter;

	public ChatKorisnikDTO() {
	}

	public ChatKorisnikDTO(String korisnickoIme, PrintWriter printWriter) {
		super();
		this.korisnickoIme = korisnickoIme;
		this.printWriter = printWriter;
	}

	public ChatKorisnikDTO(String korisnickoIme) {
		super();
		this.korisnickoIme = korisnickoIme;
	}

	public String getKorisnickoIme() {
		return korisnickoIme;
	}

	public void setKorisnickoIme(String korisnickoIme) {
		this.korisnickoIme = korisnickoIme;
	}

	public PrintWriter getPrintWriter() {
		return printWriter;
	}

	public void setPrintWriter(PrintWriter printWriter) {
		this.printWriter = printWriter;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((korisnickoIme == null) ? 0 : korisnickoIme.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChatKorisnikDTO other = (ChatKorisnikDTO) obj;
		if (korisnickoIme == null) {
			if (other.korisnickoIme != null)
				return false;
		} else if (!korisnickoIme.equals(other.korisnickoIme))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return korisnickoIme;
	}

}
