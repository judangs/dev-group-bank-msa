package org.bank.pay.core.fixture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bank.core.auth.AuthClaims;

import java.util.Base64;

public class UserClaimsFixture {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final String USERID = "userid";
    private static final String PASSWORD = "password";
    private static final String USERNAME = "username";
    private static final String EMAIL = "email";

    public static AuthClaims authenticated() {
        return new AuthClaims.ConcreteAuthClaims(USERID, PASSWORD, EMAIL);
    }

    public static String header() throws JsonProcessingException {
        String claims =  objectMapper.writeValueAsString(authenticated());
        String encode = Base64.getEncoder().encodeToString(claims.getBytes());
        return encode;
    }

    public static String userid() {
        return USERID;
    }

    public static String password() {
        return PASSWORD;
    }

    public static String username() {
        return USERNAME;
    }

    public static String email() {
        return EMAIL;
    }
}
