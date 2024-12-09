package org.bank.pay.core.onwer;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bank.core.auth.AuthClaims;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OwnerClaims extends AuthClaims {
    private String roles;
}