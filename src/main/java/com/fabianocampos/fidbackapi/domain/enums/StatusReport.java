package com.fabianocampos.fidbackapi.domain.enums;

public enum StatusReport {

	OPEN(0, "Aberto"), CONCLUDED(2, "Concluido"), IN_PROGRESS(3, "Em Progresso"), AVALIATION(1, "Avaliacao"), ARCHIVED(4, "Arquivado");

	private Integer cod;
	private String description;

	private StatusReport() {
	}

	private StatusReport(Integer cod, String description) {
		this.cod = cod;
		this.description = description;
	}

	public Integer getCod() {
		return cod;
	}

	public void setCod(Integer cod) {
		this.cod = cod;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static StatusReport findByDescription(String description) {
		for (StatusReport status : StatusReport.values()) {
			if (status.getDescription().equals(description)) {
				return status;
			}
		}
		return null;
	}
}
