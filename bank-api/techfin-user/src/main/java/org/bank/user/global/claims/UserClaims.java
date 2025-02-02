package org.bank.user.global.claims;

import lombok.Getter;
import org.bank.core.auth.AuthClaims;

@Getter
public class UserClaims extends AuthClaims {
    public UserClaims(String userid, String username, String email) {
        super(userid, username, email);
    }
}
