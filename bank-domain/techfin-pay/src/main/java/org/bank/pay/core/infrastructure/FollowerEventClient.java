package org.bank.pay.core.infrastructure;

import org.bank.core.auth.AuthClaims;
import org.bank.pay.core.domain.familly.Family;

public interface FollowerEventClient {
    void invite(Family family, AuthClaims follower);
}
