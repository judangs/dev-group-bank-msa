package org.bank.pay.core;

import org.bank.core.auth.AuthenticationException;
import org.bank.core.dto.response.ResponseDtoV2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
public class AuthenticationExceptionController {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<? super ResponseDtoV2> permissionException(AuthenticationException error) {

        if(Objects.nonNull(error.getMessage())) {
            ResponseDtoV2 response = ResponseDtoV2.unauthorized(error.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        ResponseDtoV2 response = ResponseDtoV2.unauthorized("이메일 유효성 검증에 실패했습니다. 다시 시도해주세요.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}
