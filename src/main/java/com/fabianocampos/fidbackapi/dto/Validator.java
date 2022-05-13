package com.fabianocampos.fidbackapi.dto;

import com.fabianocampos.fidbackapi.resources.exception.ValidationException;

import java.security.Principal;

public interface Validator<I> {

    void validate(I input, Principal principal) throws ValidationException;

}
