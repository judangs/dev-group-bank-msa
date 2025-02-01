package org.bank.user.dto.service.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.bank.core.dto.response.ResponseCodeV2;
import org.bank.core.dto.response.ResponseDtoV2;
import org.bank.core.dto.response.ResponseMessage;

import java.time.LocalDateTime;

@Data
@SuperBuilder
public class LoginResponse extends ResponseDtoV2 {

    private Token token;

    @Getter
    @AllArgsConstructor
    public static class Token {
        private String access;
    }

    public static ResponseDtoV2 success(String token) {
        return LoginResponse.builder()
                .code(ResponseCodeV2.SUCCESS)
                .token(new Token(token))
                .message(ResponseMessage.SUCCESS)
                .completedAt(LocalDateTime.now())
                .build();
    }

}
