package org.bank.pay.global.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.bank.core.auth.AuthClaims;
import org.bank.core.common.Base64Converter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AuthClaimsConverter implements Converter<String, AuthClaims> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    @Override
    public AuthClaims convert(String source) {
        return objectMapper.readValue(Base64Converter.decode(source), AuthClaims.class);
    }
}
