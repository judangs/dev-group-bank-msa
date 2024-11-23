package org.bank.user.core.auth.application.provider;

import exception.MissingHeaderException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.bank.common.constants.auth.AuthHeaders;
import org.bank.common.constants.auth.TokenConstants;
import org.bank.user.core.auth.domain.TokenEncoder;
import org.bank.user.core.auth.domain.TokenPayload;
import org.bank.user.core.user.domain.credential.UserCredential;
import org.bank.user.global.http.HeaderAttribute;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final TokenEncoder tokenEncoder;

    public String generate(UserCredential credential, String tokenType) {

        TokenPayload tokenPayload = TokenPayload.builder()
                .subject(credential.getUserid())
                .username(credential.getUsername())
                .email(credential.getUserProfile().getEmail())
                .roles(Arrays.asList(credential.getUserType()))
                .build();

        return tokenEncoder.encode(tokenPayload, tokenType);
    }

    public void addJwtToResponseHeader(HttpServletResponse response, String token) {
        response.addHeader(HeaderAttribute.AUTHORIZATION, TokenConstants.BEARER_PREFIX + token);
    }

    public String getTokenFromRequest(HttpServletRequest request) throws MissingHeaderException {

        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(!StringUtils.hasText(token)) {
            throw new MissingHeaderException();
        }

        return token;
    }

    public String getUseridFromRequest(HttpServletRequest request) throws MissingHeaderException {
        String userid = request.getHeader(AuthHeaders.USER_ID);
        if(!StringUtils.hasText(userid)) {
            throw new MissingHeaderException();
        }

        return userid;
    }
}

