package org.bank.user.common.exception;


import org.bank.user.dto.ExceptionResponse;
import org.bank.user.exception.credential.PermissionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PermissionException.class)
    public ResponseEntity<ExceptionResponse> permissionException(PermissionException error) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error.toResponse());
    }

}
