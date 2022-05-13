package com.fabianocampos.fidbackapi.services.exception;

public class PermissionInvalidException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PermissionInvalidException(String msg) {
		super(msg);
	}

}
