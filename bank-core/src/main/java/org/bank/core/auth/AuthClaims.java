package org.bank.core.auth;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@MappedSuperclass
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonDeserialize(as = AuthClaims.ConcreteAuthClaims.class)
public abstract class AuthClaims {

    @EqualsAndHashCode.Include
    protected String userid;
    protected String username;
    protected String email;

    public AuthClaims(String userid, String username, String email) {
        this.userid = userid;
        this.username = username;
        this.email = email;
    }

    public AuthClaims() {
    }


    @NoArgsConstructor
    public static class ConcreteAuthClaims extends AuthClaims {
        public ConcreteAuthClaims(String userid, String username, String email) {
            super(userid, username, email);
        }
    }
}