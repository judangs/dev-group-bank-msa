package org.bank.pay.core.familly;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class FamillyTest {

    private Familly familly = new Familly();
    private MemberClaims leader = new MemberClaims("fixture-id", "fixture", "fixture@bank.com", Instant.now());

    @Test
    @DisplayName("패밀리를 생성합니다.")
    void createFamilly() {

        familly.createFamilly(leader);

        assertAll(
                () -> assertEquals(leader, familly.getLeader()),
                () -> assertFalse(familly.getParticipants().isEmpty())
        );
    }

    @Test
    @DisplayName("패밀리 멤버를 추가합니다.")
    void addMember() {
        MemberClaims newMeber = new MemberClaims("new-fuxture", "new", "new@bank.com", Instant.now());

        int participants = familly.getParticipants().size();
        familly.addMember(newMeber);

        assertEquals(familly.getParticipants().size(), participants + 1);
    }

    @Test
    @DisplayName("패밀리 리더를 변경합니다.")
    void changeLeader() {

        MemberClaims newMeber = new MemberClaims("new-fuxture", "new", "new@bank.com", Instant.now());

        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> familly.changeLeader(newMeber)),
                () -> {
                    familly.addMember(newMeber);
                    familly.changeLeader(newMeber);
                    assertEquals(familly.getLeader(), newMeber);
                }
        );
    }

}