package org.bank.pay.fixture;

import org.bank.core.auth.AuthClaims;
import org.bank.pay.core.domain.familly.Family;
import org.bank.pay.core.domain.familly.MemberClaims;

public class FamilyFixture {

    private static final MemberClaims leader = new MemberClaims("leader", "leader", "leader@email.com");
    private static final MemberClaims follower = new MemberClaims("follower", "follower", "follower@email.com");

    public static Family family() {
        return new Family(leader);
    }

    public static MemberClaims leader() {
        return leader;
    }

    public static MemberClaims leader(String userid, String username) {
        return new MemberClaims(userid, username, "username@email.com");
    }

    public static MemberClaims leader(AuthClaims user) {
        return MemberClaims.of(user);
    }

    public static MemberClaims follower() {
        return follower;
    }

    public static MemberClaims follower(String userid, String username) {
        return new MemberClaims(userid, username, "username@email.com");
    }

    public static MemberClaims follower(AuthClaims user) {
        return MemberClaims.of(user);
    }


}
