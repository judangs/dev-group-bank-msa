package org.bank.pay.core.domain.owner;

import jakarta.persistence.Embeddable;
import lombok.*;
import org.bank.core.auth.AuthClaims;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class OwnerClaims extends AuthClaims {
    private String roles;

    public OwnerClaims(String userid, String username, String email) {
        super(userid, username, email);
    }

    public OwnerClaims(String userid, String username, String email, String roles) {
        super(userid, username, email);
        this.roles = roles;
    }

    public static OwnerClaims of(AuthClaims claims) {
        return new OwnerClaims(claims.getUserid(), claims.getUsername(), claims.getEmail());
    }

    public static OwnerClaims of(AuthClaims claims, String roles) {
        return new OwnerClaims(claims.getUserid(), claims.getUsername(), claims.getEmail(), roles);
    }

}