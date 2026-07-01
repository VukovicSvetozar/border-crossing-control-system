package org.unibl.etf.model;

import java.io.Serializable;

public class KorisnikDTO implements Serializable {

	private static final long serialVersionUID = -1570651538769208427L;

	private String korisnickoIme;
	private String lozinka;
	private Boolean aktivan;
	private String idProlaz;
	private String tipKontrole;

	public KorisnikDTO() {
	}

	public KorisnikDTO(String korisnickoIme, String lozinka, Boolean aktivan, String idProlaz, String tipKontrole) {
		super();
		this.korisnickoIme = korisnickoIme;
		this.lozinka = lozinka;
		this.aktivan = aktivan;
		this.idProlaz = idProlaz;
		this.tipKontrole = tipKontrole;
	}

	public String getKorisnickoIme() {
		return korisnickoIme;
	}

	public void setKorisnickoIme(String korisnickoIme) {
		this.korisnickoIme = korisnickoIme;
	}

	public String getLozinka() {
		return lozinka;
	}

	public void setLozinka(String lozinka) {
		this.lozinka = lozinka;
	}

	public Boolean getAktivan() {
		return aktivan;
	}

	public void setAktivan(Boolean aktivan) {
		this.aktivan = aktivan;
	}

	public String getIdProlaz() {
		return idProlaz;
	}

	public void setIdProlaz(String idProlaz) {
		this.idProlaz = idProlaz;
	}

	public String getTipKontrole() {
		return tipKontrole;
	}

	public void setTipKontrole(String tipKontrole) {
		this.tipKontrole = tipKontrole;
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
		KorisnikDTO other = (KorisnikDTO) obj;
		if (korisnickoIme == null) {
			if (other.korisnickoIme != null)
				return false;
		} else if (!korisnickoIme.equals(other.korisnickoIme))
			return false;
		return true;
	}

}
