package org.unibl.etf.model;

import java.io.Serializable;

public class KredencijaliDTO implements Serializable {

	private static final long serialVersionUID = -1570651538769208427L;

	private String korisnickoIme;
	private String enkodovanaLozinka;
	private String salt;

	public KredencijaliDTO() {
	}

	public KredencijaliDTO(String korisnickoIme, String enkodovanaLozinka, String salt) {
		super();
		this.korisnickoIme = korisnickoIme;
		this.enkodovanaLozinka = enkodovanaLozinka;
		this.salt = salt;
	}

	public String getKorisnickoIme() {
		return korisnickoIme;
	}

	public void setKorisnickoIme(String korisnickoIme) {
		this.korisnickoIme = korisnickoIme;
	}

	public String getEnkodovanaLozinka() {
		return enkodovanaLozinka;
	}

	public void setEnkodovanaLozinka(String enkodovanaLozinka) {
		this.enkodovanaLozinka = enkodovanaLozinka;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
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
		KredencijaliDTO other = (KredencijaliDTO) obj;
		if (korisnickoIme == null) {
			if (other.korisnickoIme != null)
				return false;
		} else if (!korisnickoIme.equals(other.korisnickoIme))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "KredencijaiDTO [korisnickoIme=" + korisnickoIme + ", enkodovanaLozinka=" + enkodovanaLozinka + "]";
	}

}
