package org.bank.core.auth;

import jakarta.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@MappedSuperclass
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
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
}