package org.bank.user.presentation.http;

import org.bank.user.exception.credential.PermissionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserControllerAdvice {

    @ExceptionHandler(PermissionException.class)
    public ResponseEntity<ErrorResponse> permissionException(PermissionException error) {

        return null;
    }

}
