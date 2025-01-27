package org.bank.pay.core.familly;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FamilyTest {

    private final MemberClaims leader = new MemberClaims("fixture-id", "fixture", "fixture@bank.com");

    @Test
    @DisplayName("family를 생성합니다")
    void family를_생성합니다() {
        Family family = new Family(leader);

        assertAll(
                () -> assertEquals(leader, family.getLeader()),
                () -> assertFalse(family.getParticipants().isEmpty())
        );
    }
}