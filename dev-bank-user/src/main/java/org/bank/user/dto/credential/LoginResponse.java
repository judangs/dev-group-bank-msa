package org.bank.user.dto.credential;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.bank.user.dto.ResponseDto;

@Getter
@SuperBuilder
public class LoginResponse extends ResponseDto {

    private Token data;


    @Getter
    @Builder
    public static class Token {
        private String refresh;
        private String access;
    }

}
