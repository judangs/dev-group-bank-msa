package org.bank.gateway.core.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bank.core.auth.AuthClaims;
import org.bank.core.auth.AuthenticationException;
import org.bank.core.common.Base64Converter;
import org.springframework.stereotype.Component;

@Component
public class AuthHeaderEncoder implements AuthClaimsEncoder {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String encode(AuthClaims authClaims) {
        try {
            return Base64Converter.encode(objectMapper.writeValueAsString(authClaims));
        } catch (JsonProcessingException e) {
            throw new AuthenticationException("잘못된 접근입니다. 다시 인증해주세요.");
        }
    }
}
