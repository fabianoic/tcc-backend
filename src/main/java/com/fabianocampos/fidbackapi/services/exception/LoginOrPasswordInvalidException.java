package com.fabianocampos.fidbackapi.services.exception;

public class LoginOrPasswordInvalidException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public LoginOrPasswordInvalidException(String msg) {
		super(msg);
	}

}
