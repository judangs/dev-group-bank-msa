package org.bank.consumer.family;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.bank.consumer.core.family.FamilyEventConsumer;
import org.bank.consumer.integration.init.TestInitializer;
import org.bank.core.cash.InsufficientBalanceException;
import org.bank.core.cash.Money;
import org.bank.core.kafka.KafkaEvent;
import org.bank.pay.core.domain.familly.Family;
import org.bank.pay.core.domain.familly.repository.FamilyEventReader;
import org.bank.pay.core.domain.familly.repository.FamilyReader;
import org.bank.pay.core.domain.familly.repository.FamilyStore;
import org.bank.pay.core.domain.owner.repository.PayOwnerReader;
import org.bank.pay.core.event.family.FamilyEventStatus;
import org.bank.pay.core.event.family.PaymentProduct;
import org.bank.pay.core.event.family.kafka.CashConversionEvent;
import org.bank.pay.core.event.family.kafka.InviteEvent;
import org.bank.pay.core.event.family.kafka.PaymentEvent;
import org.bank.pay.fixture.FamilyEventFixture;
import org.bank.pay.fixture.FamilyFixture;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;


@Tag("카프카를 활용하지 않은 패밀리 그룹 이벤트의 통합 테스트")
@ActiveProfiles({"test"})
@Import(TestInitializer.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class FamilyEventConsumerIntegrationTest {

    @Autowired
    private TestInitializer testInitializer;

    @Autowired
    private FamilyEventConsumer familyEventConsumer;
    @Autowired
    private FamilyReader familyReader;
    @Autowired
    private FamilyEventReader familyEventReader;
    @Autowired
    private FamilyStore familyStore;
    @Autowired
    private PayOwnerReader payOwnerReader;

    private Family family;


    @BeforeAll
    void setUp() {
        testInitializer.init(FamilyFixture.leader());
        testInitializer.init(FamilyFixture.follower());

        family = testInitializer.getFamily(FamilyFixture.leader());
        family.getParticipants().add(FamilyFixture.follower());

        familyStore.saveFamily(family);
    }

    @Test
    void 사용자를_패밀리_멤버로_초대하는_이벤트를_데이터베이스에_저장합니다() {
        InviteEvent inviteEvent = FamilyEventFixture.invite(family.getFamilyId(), FamilyFixture.follower());
        ConsumerRecord<String, KafkaEvent> record = new ConsumerRecord<>("family.invitation", 0, 0, UUID.randomUUID().toString(), inviteEvent);

        familyEventConsumer.consume(record);

        assertThat(familyEventReader.findInvitationEventByUser(FamilyFixture.follower())).isPresent();
        familyEventReader.findInvitationEventByUser(FamilyFixture.follower()).ifPresent(event ->
                assertAll(
                        () -> assertThat(event.getFamilyId()).isEqualTo(family.getFamilyId()),
                        () -> assertThat(event.getTo()).isEqualTo(FamilyFixture.follower()),
                        () -> assertThat(event.getStatus()).isEqualTo(FamilyEventStatus.PENDING)
                )
        );
    }

    @Test
    void 패밀리_그룹원이_리더에게_결제를_요청하는_이벤트를_데이터베이스에_저장합니다() {
        PaymentEvent paymentEvent = FamilyEventFixture.pay(family.getFamilyId(), FamilyFixture.follower(), List.of(new PaymentProduct("테스트 결제", new Money(10000), 1)));
        ConsumerRecord<String, KafkaEvent> record = new ConsumerRecord<>("family.payment.request", 0, 0, UUID.randomUUID().toString(), paymentEvent);

        familyEventConsumer.request(record);

        assertThat(familyEventReader.findPaymentRequestEventsByFamily(family.getFamilyId())).isNotEmpty();
        assertThat(familyEventReader.findPaymentRequestEventsByFamily(family.getFamilyId()))
                .first().satisfies(event -> {
                    assertAll(
                            () -> assertThat(event.getFamilyId()).isEqualTo(family.getFamilyId()),
                            () -> assertThat(event.getFrom()).isEqualTo(FamilyFixture.follower()),
                            () -> assertThat(event.getProducts()).containsExactlyInAnyOrderElementsOf(List.of(new PaymentProduct("테스트 결제", new Money(10000), 1))),
                            () -> assertThat(event.getStatus()).isEqualTo(FamilyEventStatus.PENDING)
                    );
                });
    }

    @Test
    void 패밀리_그룹원이_개인_캐시로_패밀리_캐시로_전환하는_이벤트를_처리합니다() {
        Money charge = new Money(10000);
        CashConversionEvent cashConversionEvent = FamilyEventFixture.cashConversion(family.getFamilyId(), testInitializer.getCard(FamilyFixture.follower()).getCardId(), FamilyFixture.follower(), charge);
        ConsumerRecord<String, KafkaEvent> record = new ConsumerRecord<>("family.cash.conversion", 0, 0, UUID.randomUUID().toString(), cashConversionEvent);

        familyEventConsumer.conversion(record);

        assertThat(familyReader.findById(family.getFamilyId())).isPresent();
        familyReader.findById(family.getFamilyId()).ifPresent(family ->
                assertAll(
                        () -> assertThat(family.find(FamilyFixture.follower().getUserid())).isNotNull(),
                        () -> assertThat(family.getFamilyCard().getCash().getCredit()).isEqualTo(charge)
                )
        );

        assertThat(payOwnerReader.findByUserClaims(FamilyFixture.follower())).isPresent();
        payOwnerReader.findByUserClaims(FamilyFixture.follower()).ifPresent(owner -> {
            assertAll(
                    () -> assertThat(owner.match(testInitializer.getCard(FamilyFixture.follower()).getCardId())).isPresent(),
                    () -> owner.match(testInitializer.getCard(FamilyFixture.follower()).getCardId())
                            .ifPresent(card -> assertThat(card.getCash().getCredit()).isEqualTo(new Money(0)))
            );
        });
    }


    @Test
    void 패밀리_그룹원이_잔액이_부족한_상태에서_패밀리_캐시_전환_이벤트를_처리합니다() {
        Money charge = new Money(20000);
        CashConversionEvent cashConversionEvent = FamilyEventFixture.cashConversion(family.getFamilyId(), testInitializer.getCard(FamilyFixture.follower()).getCardId(), FamilyFixture.follower(), charge);
        ConsumerRecord<String, KafkaEvent> record = new ConsumerRecord<>("family.cash.conversion", 0, 0, UUID.randomUUID().toString(), cashConversionEvent);

        assertThatThrownBy(() -> familyEventConsumer.conversion(record))
                .isInstanceOf(InsufficientBalanceException.class);
    }
}