package org.bank.pay.core.onwer;

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

}