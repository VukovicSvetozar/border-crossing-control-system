package org.unibl.etf.model;

public class DokumentDTO {

	private String id;
	private String info;

	public DokumentDTO() {
		super();
	}

	public DokumentDTO(String id, String info) {
		super();
		this.id = id;
		this.info = info;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	@Override
	public String toString() {
		return id;
	}

}
