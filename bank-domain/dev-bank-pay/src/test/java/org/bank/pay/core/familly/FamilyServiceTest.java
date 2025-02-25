package org.bank.pay.core.familly;

import org.bank.core.auth.AuthClaims;
import org.bank.pay.core.domain.familly.Family;
import org.bank.pay.core.domain.familly.FamilyService;
import org.bank.pay.core.domain.familly.MemberClaims;
import org.bank.pay.core.domain.familly.repository.FamilyReader;
import org.bank.pay.core.domain.familly.repository.FamilyStore;
import org.bank.pay.core.domain.owner.OwnerClaims;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FamilyServiceTest {

    @Mock
    private FamilyReader familyReader;
    @Mock
    private FamilyStore familyStore;

    private FamilyService familyService;

    private final AuthClaims leader = new OwnerClaims("fixture-id", "fixture", "fixture@email.com");
    private final AuthClaims follower = new MemberClaims("follower", "follower", "follower@email.com");

    private Family family;

    @BeforeAll
    public void setup() {
        MockitoAnnotations.openMocks(this);
        familyService = new FamilyService(familyReader, familyStore);

        family = new Family(leader);

        doNothing().when(familyStore).saveFamily(any(Family.class));
        when(familyReader.findById(family.getFamilyId())).thenReturn(Optional.of(family));
    }

    @Test
    @DisplayName("패밀리를 생성합니다")
    void 패밀리를_생성합니다() {

        MemberClaims familyLeader = new MemberClaims("new", "new", "new@email.com");
        Family newFamily = familyService.createFamily(familyLeader);
        assertThat(newFamily.getLeader()).isEqualTo(familyLeader);
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