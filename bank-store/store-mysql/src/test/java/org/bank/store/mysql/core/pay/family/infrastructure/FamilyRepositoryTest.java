package org.bank.store.mysql.core.pay.family.infrastructure;

import org.assertj.core.api.Assertions;
import org.bank.core.auth.AuthClaims;
import org.bank.core.cash.Money;
import org.bank.pay.core.domain.familly.Family;
import org.bank.pay.core.domain.familly.MemberClaims;
import org.bank.pay.fixture.FamilyFixture;
import org.bank.pay.fixture.UserFixture;
import org.bank.store.mysql.core.pay.unit.FamilyRepositoryUnitTest;
import org.bank.store.mysql.core.pay.unit.init.TestInitializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = FamilyRepositoryUnitTest.class)
class FamilyRepositoryTest {


    @Autowired
    private FamilyCommandRepository familyCommandRepository;
    @Autowired
    private FamilyQueryRepository familyQueryRepository;
    @Autowired
    private TestInitializer testInitializer;


    private AuthClaims leader = UserFixture.authenticated();
    private Family family;

    @BeforeEach
    public void setUp() {
        testInitializer.owner(leader);
        family = new Family(leader);

        familyCommandRepository.saveFamily(family);
    }

    @AfterEach
    public void tearDown() {
        familyCommandRepository.deleteFamily(family);
    }

    @Test
    void 캐시를_공유할_그룹을_생성합니다() {

        Family family = new Family(FamilyFixture.leader("leader", "family-leader"));
        Assertions.assertThatCode(() -> familyCommandRepository.saveFamily(family))
                .doesNotThrowAnyException();
    }

    @Test
    void 캐시를_공유할_그룹을_찾을_수_있습니다() {
        assertThat(familyQueryRepository.findById(family.getFamilyId())).isPresent();

        familyQueryRepository.findById(family.getFamilyId()).ifPresent(family -> {
            assertAll(
                    () -> assertThat(family.getLeader()).isEqualTo(MemberClaims.of(leader)),
                    () -> assertThat(family.getParticipants().isEmpty()).isFalse(),
                    () -> assertThat(family.getFamilyCard()).isNotNull(),
                    () -> assertThat(family.getFamilyCard().getCash().getCredit()).isEqualTo(new Money(0))
            );
        });
    }

    @Test
    void 데이터베이스에서_리더_유저가_속한_패밀리를_조회할_수_있습니다() {
        assertThat(familyQueryRepository.findByUserIsLeader(leader).isPresent()).isTrue();
    }
}