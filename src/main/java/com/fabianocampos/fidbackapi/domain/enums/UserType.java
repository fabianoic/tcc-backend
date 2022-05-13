package com.fabianocampos.fidbackapi.domain.enums;

public enum UserType {

	ADMIN(1, "Administrador"), USER(2, "Usuário"), DEVELOPER(3, "Desevolvedor");

	private Integer cod;
	private String description;

	private UserType() {
	}

	private UserType(Integer cod, String description) {
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

}
