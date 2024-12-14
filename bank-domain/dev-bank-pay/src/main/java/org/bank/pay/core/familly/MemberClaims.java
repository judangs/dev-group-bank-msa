package org.bank.pay.core.familly;

import jakarta.persistence.Embeddable;
import lombok.*;
import org.bank.core.auth.AuthClaims;
import org.bank.pay.core.onwer.OwnerClaims;

import java.time.Instant;
import java.util.UUID;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class MemberClaims extends AuthClaims {

    private UUID memberId;
    private Instant enrollmentDate;
    private Instant terminateDate;
    private boolean withdrawal;

    public MemberClaims(String userid, String username, String email, Instant enrollmentDate) {
        super(userid, username, email);
        this.memberId = UUID.randomUUID();
        this.enrollmentDate = enrollmentDate;
        this.terminateDate = Instant.MIN;

        withdrawal = false;
    }

    public static MemberClaims of(OwnerClaims ownerClaims) {
        return new MemberClaims(ownerClaims.getUserid(), ownerClaims.getUsername(), ownerClaims.getEmail(), Instant.now());
    }

    public static MemberClaims of(AuthClaims authClaims) {
        return new MemberClaims(authClaims.getUserid(), authClaims.getUsername(), authClaims.getEmail(), Instant.now());
    }
}
