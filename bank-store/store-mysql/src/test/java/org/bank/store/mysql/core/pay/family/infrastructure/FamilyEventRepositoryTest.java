package org.bank.store.mysql.core.pay.family.infrastructure;

import org.assertj.core.api.Assertions;
import org.bank.core.auth.AuthClaims;
import org.bank.pay.core.domain.familly.Family;
import org.bank.pay.core.event.family.FamilyEventStatus;
import org.bank.pay.core.event.family.FamilyInvitation;
import org.bank.pay.core.event.family.FamilyPayment;
import org.bank.pay.fixture.FamilyEventFixture;
import org.bank.pay.fixture.FamilyFixture;
import org.bank.pay.fixture.UserFixture;
import org.bank.store.mysql.core.pay.unit.FamilyRepositoryUnitTest;
import org.bank.store.mysql.core.pay.unit.init.TestInitializer;
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
class FamilyEventRepositoryTest {

    @Autowired
    private FamilyEventRepository familyEventRepository;
    @Autowired
    private FamilyCommandRepository familyCommandRepository;
    @Autowired
    private TestInitializer testInitializer;

    private AuthClaims leader = UserFixture.authenticated();
    private Family family;

    @BeforeEach
    void setup() {
        testInitializer.owner(leader);
        family = new Family(leader);
        familyCommandRepository.saveFamily(family);
    }

    @Test
    void 데이터베이스에_패밀리_멤버_초대를_위해_멤버_초대_이벤트를_저장합니다() {
        Assertions.assertThatCode(() -> familyEventRepository.store(FamilyEventFixture.invitation()))
                .doesNotThrowAnyException();
    }

    @Test
    void 데이터베이스에_상품_구매를_위한_패밀리_결제_요청을_저장합니다() {
        Assertions.assertThatCode(() -> familyEventRepository.store(FamilyEventFixture.payment()))
                .doesNotThrowAnyException();
    }

    @Test
    void 데이터베이스에서_유저_명세를_통해_패밀리_초대_이벤트를_조회할_수_있습니다() {
        Assertions.assertThatCode(() -> familyEventRepository.store(FamilyEventFixture.invitation(family, FamilyFixture.follower())))
                .doesNotThrowAnyException();

        familyEventRepository.findInvitationEventByUser(FamilyEventFixture.invite().getTo())
                .ifPresent(event -> {
                    assertAll(
                            () -> assertThat(event).isExactlyInstanceOf(FamilyInvitation.class),
                            () -> assertThat(event.getFamilyId()).isEqualTo(FamilyEventFixture.invite().getFamilyId()),
                            () -> assertThat(event.getStatus()).isEqualTo(FamilyEventStatus.PENDING)
                    );
                });
    }

    @Test
    void 데이터베이스에서_이벤트_아이디에_해당하는_멤버_초대_이벤트를_조회할_수_있습니다() {
        Assertions.assertThatCode(() -> familyEventRepository.store(FamilyEventFixture.invitation()))
                .doesNotThrowAnyException();

        familyEventRepository.findInvitationEventById(FamilyEventFixture.invite().getEventId())
                .ifPresent(event -> {
                    assertAll(
                            () -> assertThat(event).isExactlyInstanceOf(FamilyInvitation.class),
                            () -> assertThat(event.getFamilyId()).isEqualTo(FamilyEventFixture.invite().getFamilyId()),
                            () -> assertThat(event.getStatus()).isEqualTo(FamilyEventStatus.PENDING)
                    );
                });
    }

    @Test
    void 데이터베이스에서_그룹_아이디에_해당하는_결제_요청_이벤트를_조회할_수_있습니다() {
        Assertions.assertThatCode(() -> familyEventRepository.store(FamilyEventFixture.payment()))
                .doesNotThrowAnyException();

        familyEventRepository.findPaymentRequestEventByFamilyAndId(FamilyEventFixture.pay().getFamilyId(), FamilyEventFixture.pay().getEventId())
                .ifPresent(event -> {
                    assertAll(
                            () -> assertThat(event).isExactlyInstanceOf(FamilyPayment.class),
                            () -> assertThat(event.getFamilyId()).isEqualTo(FamilyEventFixture.invite().getFamilyId()),
                            () -> assertThat(event.getStatus()).isEqualTo(FamilyEventStatus.PENDING)
                    );
                });
    }
}