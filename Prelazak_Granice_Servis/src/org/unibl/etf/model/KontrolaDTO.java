package org.unibl.etf.model;

import java.io.*;
import java.util.*;

public class KontrolaDTO implements Serializable {

	private static final long serialVersionUID = -2451187649754065043L;

	private String id;
	private String idTerminal;
	private String status;
	private List<ProlazakDTO> prolasci;

	public KontrolaDTO() {
		super();
	}

	public KontrolaDTO(String id, String idTerminal, String status) {
		super();
		this.id = id;
		this.idTerminal = idTerminal;
		this.status = status;
		prolasci = new ArrayList<>();
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<ProlazakDTO> getProlasci() {
		return prolasci;
	}

	public void setProlasci(List<ProlazakDTO> prolasci) {
		this.prolasci = prolasci;
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
		KontrolaDTO other = (KontrolaDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "KontrolaDTO [id=" + id + ", idTerminal=" + idTerminal + ", status=" + status + ", prolasci=" + prolasci
				+ "]";
	}

}
