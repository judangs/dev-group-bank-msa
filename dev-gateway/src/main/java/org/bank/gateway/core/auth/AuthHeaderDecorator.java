package org.bank.gateway.core.auth;

import org.bank.common.constants.auth.AuthHeaders;
import org.bank.common.constants.auth.TokenConstants;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;


@Component
public class AuthHeaderDecorator {

    public ServerHttpRequest addHeaders(ServerWebExchange exchange, Jwt jwt) {

        HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.AUTHORIZATION, jwt.getTokenValue());
        headers.add(AuthHeaders.USER_ID, jwt.getSubject());
        headers.add(AuthHeaders.USER_EMAIL, jwt.getClaimAsString(TokenConstants.CLAIM_EMAIL));
        headers.add(AuthHeaders.USER_ROLE, jwt.getClaimAsString(TokenConstants.CLAIM_ROLE));


        return decorate(exchange, headers);

    }

    public ServerHttpRequest addHeaders(ServerWebExchange exchange, String key, String value) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(key, value);
        return decorate(exchange, headers);

    }

    private ServerHttpRequest decorate(ServerWebExchange exchange, HttpHeaders headers) {
        return new ServerHttpRequestDecorator(exchange.getRequest()) {
            @Override
            public HttpHeaders getHeaders() {
                return headers;
            }
        };
    }


}
