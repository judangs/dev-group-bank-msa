package org.bank.user.core.auth.domain;

import lombok.Builder;
import lombok.Getter;
import org.bank.user.core.user.domain.credential.RoleClassification;

import java.util.Date;
import java.util.List;

@Getter
@Builder
public class TokenPayload {
    private String subject;
    private String issuer;
    private String username;
    private String email;
    private List<RoleClassification.UserRole> roles;
    private Date issuedAt;
    private Date expiresAt;

    public void addRole(RoleClassification.UserRole role) {

        this.roles.add(role);
    }

    public void setTokenValidityWithDate(String issuer, Date issueAt, Date expireAt) {

        this.issuer = issuer;
        this.issuedAt = issueAt;
        this.expiresAt = expireAt;

    }
}