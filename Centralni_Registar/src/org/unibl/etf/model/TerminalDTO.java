package org.unibl.etf.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public final class TerminalDTO implements Serializable {

	private static final long serialVersionUID = -1570651538769208427L;

	private static AtomicInteger brojac = new AtomicInteger(4);
	private String id;
	private String naziv;
	private ProlazDTO[] prolazi;
	private String tipSerijalizacije;

	public TerminalDTO(String naziv, ProlazDTO[] prolazi) {
		super();
		this.id = String.format("%03d", brojac.incrementAndGet());
		this.prolazi = prolazi;
		this.naziv = naziv;
		odrediTipSerijalizacije();
	}

	public TerminalDTO(String id, String naziv, ProlazDTO[] prolazi) {
		super();
		this.id = id;
		this.prolazi = prolazi;
		this.naziv = naziv;
		odrediTipSerijalizacije();
	}

	public TerminalDTO() {
		super();
	}

	private void odrediTipSerijalizacije() {
		int redniBroj = Integer.parseInt(this.id) % 4;
		switch (redniBroj) {
		case 0:
			tipSerijalizacije = TipSerijalizacije.XML;
			break;
		case 1:
			tipSerijalizacije = TipSerijalizacije.GSON;
			break;
		case 2:
			tipSerijalizacije = TipSerijalizacije.KRYO;
			break;
		case 3:
			tipSerijalizacije = TipSerijalizacije.JAVA;
			break;
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public ProlazDTO[] getProlazi() {
		return prolazi;
	}

	public void setProlazi(ProlazDTO[] prolazi) {
		this.prolazi = prolazi;
	}

	public String getTipSerijalizacije() {
		return tipSerijalizacije;
	}

	public void setTipSerijalizacije(String tipSerijalizacije) {
		this.tipSerijalizacije = tipSerijalizacije;
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
		TerminalDTO other = (TerminalDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TerminalDTO [id=" + id + ", naziv=" + naziv + ", prolazi=" + Arrays.toString(prolazi)
				+ ", tipSerijalizacije=" + tipSerijalizacije + "]";
	}

}
