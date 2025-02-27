package org.bank.user.global.http;

import jakarta.servlet.http.HttpServletResponse;
import org.bank.core.auth.AuthConstants;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationHeaderResolver {

    public void addTokenToResponseHeader(HttpServletResponse httpServletResponse, String token) {
        httpServletResponse.addHeader(HttpHeaders.AUTHORIZATION, AuthConstants.BEARER_PREFIX + token);
    }

    public String parseRequestHeaderToToken(String authorization) {
        return authorization.substring(AuthConstants.BEARER_PREFIX.length());
    }
}
