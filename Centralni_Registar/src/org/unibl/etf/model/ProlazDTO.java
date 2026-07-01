package org.unibl.etf.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class ProlazDTO implements Serializable {

	private static final long serialVersionUID = -2451187649754065043L;

	private static AtomicInteger brojac = new AtomicInteger(0);
	private String id;
	private String idTerminal;
	private String tipProlaza;
	private KorisnikDTO[] korisnici;

	public ProlazDTO(String idTerminal, String tipProlaza, KorisnikDTO[] korisnici) {
		super();
		this.id = String.format("%03d", brojac.incrementAndGet());
		this.idTerminal = idTerminal;
		this.tipProlaza = tipProlaza;
		this.korisnici = korisnici;
	}

	public ProlazDTO() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdTerminal() {
		return idTerminal;
	}

	public void setIdTerminal(String idTerminal) {
		this.idTerminal = idTerminal;
	}

	public String getTipProlaza() {
		return tipProlaza;
	}

	public void setTipProlaza(String tipProlaza) {
		this.tipProlaza = tipProlaza;
	}

	public KorisnikDTO[] getKorisnici() {
		return korisnici;
	}

	public void setKorisnici(KorisnikDTO[] korisnici) {
		this.korisnici = korisnici;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		ProlazDTO other = (ProlazDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ProlazDTO [id=" + id + ", idTerminal=" + idTerminal + ", tipProlaza=" + tipProlaza + ", korisnici="
				+ Arrays.toString(korisnici) + "]";
	}

}
