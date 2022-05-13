package com.fabianocampos.fidbackapi.resources.exception;

import javax.servlet.http.HttpServletRequest;

import com.fabianocampos.fidbackapi.services.exception.PermissionInvalidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fabianocampos.fidbackapi.services.exception.LoginOrPasswordInvalidException;
import com.fabianocampos.fidbackapi.services.exception.ObjectAlreadyExistsException;
import com.fabianocampos.fidbackapi.services.exception.ObjectNotFoundException;

@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<StandardError> objectNotFound(ObjectNotFoundException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(System.currentTimeMillis(), status.value(), "Não encontrado",
                e.getMessage());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<StandardError> objectNotFound(ValidationException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(System.currentTimeMillis(), status.value(), "Não encontrado", e.getMessage());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(LoginOrPasswordInvalidException.class)
    public ResponseEntity<StandardError> loginOrPasswordInvalid(LoginOrPasswordInvalidException e,
                                                                HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(System.currentTimeMillis(), status.value(), "Não encontrado",
                e.getMessage());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(ObjectAlreadyExistsException.class)
    public ResponseEntity<StandardError> objectAlreadyExists(ObjectAlreadyExistsException e,
                                                             HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(System.currentTimeMillis(), status.value(), "Objeto já existente",
                e.getMessage());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(PermissionInvalidException.class)
    public ResponseEntity<StandardError> permissionInvalid(PermissionInvalidException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        StandardError err = new StandardError(System.currentTimeMillis(), status.value(), "Usuário sem permissão para esta ação.",
                e.getMessage());
        return ResponseEntity.status(status).body(err);
    }
}
