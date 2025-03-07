package org.bank.pay.global.http;

import org.bank.core.dto.response.ResponseCodeV2;
import org.bank.core.dto.response.ResponseDtoV2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class HttpResponseEntityStatusConverter {

    public ResponseEntity<? extends ResponseDtoV2> convert(ResponseDtoV2 response) {

        HttpStatus status = convert(response.getCode());

        return ResponseEntity.status(status).body(response);
    }

    public HttpStatus convert(ResponseCodeV2 code) {
        return switch (code) {
            case SUCCESS -> HttpStatus.OK;
            case FAIL -> HttpStatus.BAD_REQUEST;
            case DUPLICATE -> HttpStatus.CONFLICT;
            case INVALID_REQUEST -> HttpStatus.BAD_REQUEST;
            case FORBIDDEN -> HttpStatus.FORBIDDEN;
            case UNAUTHORIZED -> HttpStatus.UNAUTHORIZED;
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
        };
    }
}
