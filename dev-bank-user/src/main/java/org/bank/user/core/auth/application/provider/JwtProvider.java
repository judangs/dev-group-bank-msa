package org.bank.user.core.auth.application.provider;

import exception.TokenExpiredException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.bank.common.constants.auth.TokenConstants;
import org.bank.user.core.auth.domain.TokenDecoder;
import org.bank.user.core.auth.domain.TokenEncoder;
import org.bank.user.core.auth.domain.TokenPayload;
import org.bank.user.core.user.domain.credential.UserCredential;
import org.bank.user.global.http.HeaderAttribute;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final TokenEncoder tokenEncoder;
    private final TokenDecoder tokenDecoder;

    public String generate(UserCredential credential, String tokenType) {

        TokenPayload tokenPayload = TokenPayload.builder()
                .subject(credential.getUserid())
                .username(credential.getUsername())
                .email(credential.getUserProfile().getEmail())
                .roles(Arrays.asList(credential.getUserType()))
                .build();

        return tokenEncoder.encode(tokenPayload, tokenType);
    }

    // 토큰 재발급을 위한 메서드 오버라이딩
    public String generate(TokenPayload payload) {
        return tokenEncoder.encode(payload, TokenConstants.ACCESS);
    }

    public TokenPayload decode(String token) throws TokenExpiredException {
        return tokenDecoder.decode(token);
    }

    public void addJwtToResponseHeader(HttpServletResponse response, String token) {
        response.addHeader(HeaderAttribute.AUTHORIZATION, TokenConstants.BEARER_PREFIX + token);
    }

    private boolean isWithinValidity(String token) {
        return tokenDecoder.validate(token);
    }
}

