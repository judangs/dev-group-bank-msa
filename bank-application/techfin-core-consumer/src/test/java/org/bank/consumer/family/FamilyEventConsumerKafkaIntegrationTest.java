package org.bank.consumer.family;

import org.bank.consumer.integration.init.TestInitializer;
import org.bank.consumer.integration.kafka.ConsumerIntegrationTest;
import org.bank.core.cash.Money;
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

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertAll;

@Tag("카프카를 활용한 패밀리 그룹 이벤트의 통합 테스트")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FamilyEventConsumerKafkaIntegrationTest extends ConsumerIntegrationTest {

    @Autowired
    private TestInitializer testInitializer;
    @Autowired
    private FamilyStore familyStore;
    @Autowired
    private FamilyEventReader familyEventReader;
    @Autowired
    private FamilyReader familyReader;
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
    public void 카프카로부터_패밀리_멤버를_초대하는_이벤트를_받아_처리합니다()  {
        InviteEvent inviteEvent = FamilyEventFixture.invite(family.getFamilyId(), FamilyFixture.follower());
        kafkaTemplate.send("family.invitation", inviteEvent);

        await().atMost(20, TimeUnit.SECONDS)
                .untilAsserted(() ->
                        kafkaTopicMonitor.acks("family.invitation", "family-event-group")
                );

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
    public void 카프카로부터_패밀리_구성원의_결제_요청_이벤트를_받아_처리합니다() {
        PaymentEvent paymentEvent = FamilyEventFixture.pay(family.getFamilyId(), FamilyFixture.follower(), List.of(new PaymentProduct("테스트 결제", new Money(10000), 1)));
        kafkaTemplate.send("family.payment.request", paymentEvent);

        await().atMost(20, TimeUnit.SECONDS)
                .untilAsserted(() ->
                        kafkaTopicMonitor.acks("family.payment.request", "family-event-group")
                );

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
    public void 카프카로부터_패밀리_구성원의_개인_캐시를_패밀리_공유_캐시_전환_이벤트를_받아_처리합니다() {
        Money charge = new Money(10000);
        CashConversionEvent cashConversionEvent = FamilyEventFixture.cashConversion(family.getFamilyId(), testInitializer.getCard(FamilyFixture.follower()).getCardId(), FamilyFixture.follower(), charge);

        kafkaTemplate.send("family.cash.conversion", cashConversionEvent);

        await().atMost(20, TimeUnit.SECONDS)
                .untilAsserted(() ->
                        kafkaTopicMonitor.acks("family.cash.conversion", "family-event-group")
                );

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
}
