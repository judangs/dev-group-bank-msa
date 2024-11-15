package org.bank.user.global.exception;

import org.bank.user.dto.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserControllerAdvice {

    @ExceptionHandler(PermissionException.class)
    public ResponseEntity<ExceptionResponse> permissionException(PermissionException error) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error.toResponse());
    }

    @ExceptionHandler(InvalidArgumentException.class)
    public ResponseEntity<ExceptionResponse> invalidArgumentException(InvalidArgumentException error) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.toResponse());
    }

}
