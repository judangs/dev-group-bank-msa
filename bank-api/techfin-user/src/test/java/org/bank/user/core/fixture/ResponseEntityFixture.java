package org.bank.user.core.fixture;

import org.bank.core.dto.response.ResponseDtoV2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseEntityFixture {

    public static ResponseEntity<? extends ResponseDtoV2> ok(ResponseDtoV2 response) {
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
