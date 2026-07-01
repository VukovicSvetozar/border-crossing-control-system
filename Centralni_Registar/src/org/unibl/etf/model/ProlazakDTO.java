package org.unibl.etf.model;

import java.io.Serializable;
import java.util.Arrays;

public class ProlazakDTO implements Serializable {

	private static final long serialVersionUID = -1570651538769208427L;

	private String id;
	private String idOsoba;
	private String informacije;
	private String status;
	private String[] odabraniFajlovi;

	public ProlazakDTO() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdOsoba() {
		return idOsoba;
	}

	public void setIdOsoba(String idOsoba) {
		this.idOsoba = idOsoba;
	}

	public String getInformacije() {
		return informacije;
	}

	public void setInformacije(String informacije) {
		this.informacije = informacije;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String[] getOdabraniFajlovi() {
		return odabraniFajlovi;
	}

	public void setOdabraniFajlovi(String[] odabraniFajlovi) {
		this.odabraniFajlovi = odabraniFajlovi;
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
		ProlazakDTO other = (ProlazakDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ProlazakDTO [id=" + id + ", idOsoba=" + idOsoba + ", informacije=" + informacije + ", status=" + status
				+ ", odabraniFajlovi=" + Arrays.toString(odabraniFajlovi) + "]";
	}

}
