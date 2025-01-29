package org.bank.pay.core.domain.familly;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.pay.core.domain.onwer.OwnerClaims;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class MemberClaims extends AuthClaims {


    private UUID memberId;
    private LocalDateTime enrollmentDate;
    private LocalDateTime terminateDate;

    private boolean withdrawal;

    public MemberClaims(String userid, String username, String email) {
        super(userid, username, email);

        this.memberId = UUID.randomUUID();
        this.enrollmentDate = LocalDateTime.now();
        this.terminateDate = LocalDate.of(9999, 12, 31).atTime(23, 59);

        withdrawal = false;
    }

    public static MemberClaims of(OwnerClaims ownerClaims) {
        return new MemberClaims(ownerClaims.getUserid(), ownerClaims.getUsername(), ownerClaims.getEmail());
    }

    public static MemberClaims of(AuthClaims authClaims) {
        return new MemberClaims(authClaims.getUserid(), authClaims.getUsername(), authClaims.getEmail());
    }
}
