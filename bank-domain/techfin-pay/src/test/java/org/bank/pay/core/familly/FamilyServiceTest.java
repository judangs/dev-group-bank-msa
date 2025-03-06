package org.bank.pay.core.familly;

import org.bank.pay.core.domain.familly.Family;
import org.bank.pay.core.domain.familly.FamilyService;
import org.bank.pay.core.domain.familly.MemberClaims;
import org.bank.pay.core.unit.FamilyUnitTest;
import org.bank.pay.fixture.FamilyFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


class FamilyServiceTest extends FamilyUnitTest {

    @Autowired
    private FamilyService familyService;

    private final MemberClaims leader = FamilyFixture.leader();
    private final MemberClaims follower = FamilyFixture.follower();

    private Family family;

    @BeforeEach
    public void setup() {

        family = new Family(leader);

        doNothing().when(familyStore).saveFamily(any(Family.class));
        when(familyReader.findById(family.getFamilyId())).thenReturn(Optional.of(family));
    }

    @Test
    @DisplayName("패밀리를 생성합니다")
    void 패밀리를_생성합니다() {

        MemberClaims familyLeader = FamilyFixture.leader("leader", "family-leader");
        Family newFamily = familyService.createFamily(familyLeader);
        assertAll(
                () -> assertThat(leader).isEqualTo(newFamily.getLeader()),
                () -> assertThat(newFamily.getParticipants().isEmpty()).isFalse(),
                () -> assertThat(newFamily.getParticipants().contains(leader)).isTrue()
        );
    }

    @Test
    @DisplayName("새로운 멤버를 패밀리에 추가합니다.")
    void 새로운_멤버를_패밀리에_추가합니다() {

        int prevSize = family.getParticipants().size();

        familyService.addMemberToFamily(family.getFamilyId(), follower);
        assertThat(family.getParticipants().size()).isEqualTo(prevSize + 1);
    }

    @Test
    @DisplayName("멤버를 패밀리에서 추방합니다.")
    void 멤버를_패밀리에서_추방합니다() {

        familyService.addMemberToFamily(family.getFamilyId(), FamilyFixture.follower());
        int prevSize = family.getParticipants().size();

        familyService.ejectMemberFromFamily(family.getFamilyId(), follower);
        assertThat(family.getParticipants().size()).isEqualTo(prevSize - 1);
    }

    @Test
    @DisplayName("패밀리의 리더를 변경합니다.")
    void 패밀리의_리더를_변경합니다() {

        familyService.addMemberToFamily(family.getFamilyId(), follower);
        familyService.changeFamilyLeader(family.getFamilyId(), follower);
        System.out.println(family.getLeader().getUserid());
        assertThat(family.getLeader()).isEqualTo(follower);
    }
}