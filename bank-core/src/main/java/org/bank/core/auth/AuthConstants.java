package org.bank.core.auth;

public class AuthConstants {

    public static final String BEARER_PREFIX = "Bearer ";

    public enum TokenType {
        ACCESS, REFRESH;
    }

    public static class TokenClaim {
        public static final String EMAIL = "email";
        public static final String USERNAME = "username";
        public static final String ROLE = "role";
    }

    public static class HeaderField {
        public static final String X_AUTH_CLAIMS = "X-Auth-Claims";
    }
}