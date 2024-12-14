package org.bank.pay.core.familly;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class FamilyTest {

    private Family family = new Family();
    private MemberClaims leader = new MemberClaims("fixture-id", "fixture", "fixture@bank.com", Instant.now());

    @Test
    @DisplayName("패밀리를 생성합니다.")
    void createFamilly() {

        family.createFamilly(leader);

        assertAll(
                () -> assertEquals(leader, family.getLeader()),
                () -> assertFalse(family.getParticipants().isEmpty())
        );
    }

    @Test
    @DisplayName("패밀리 멤버를 추가합니다.")
    void addMember() {
        MemberClaims newMeber = new MemberClaims("new-fuxture", "new", "new@bank.com", Instant.now());

        int participants = family.getParticipants().size();
        family.addMember(newMeber);

        assertEquals(family.getParticipants().size(), participants + 1);
    }

    @Test
    @DisplayName("패밀리 리더를 변경합니다.")
    void changeLeader() {

        MemberClaims newMeber = new MemberClaims("new-fuxture", "new", "new@bank.com", Instant.now());

        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> family.changeLeader(newMeber)),
                () -> {
                    family.addMember(newMeber);
                    family.changeLeader(newMeber);
                    assertEquals(family.getLeader(), newMeber);
                }
        );
    }

}