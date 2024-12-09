package org.bank.pay.core.familly;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bank.core.auth.AuthClaims;

import java.time.Instant;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberClaims extends AuthClaims {

    private Instant enrollmentDate;
    private Instant terminateDate;
    private boolean withdrawal;

    public MemberClaims(String userid, String username, String email, Instant enrollmentDate) {
        super(userid, username, email);
        this.enrollmentDate = enrollmentDate;
        this.terminateDate = Instant.MIN;

        withdrawal = false;
    }
}
