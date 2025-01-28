package org.bank.consumer.pay;

import org.bank.core.auth.AuthClaims;
import org.bank.core.cash.InsufficientBalanceException;
import org.bank.core.cash.Money;
import org.bank.core.payment.Product;
import org.bank.pay.core.cash.CashChargeService;
import org.bank.pay.core.familly.Family;
import org.bank.pay.core.familly.FamilyService;
import org.bank.pay.core.familly.MemberClaims;
import org.bank.pay.core.familly.event.kafka.CashConversionEvent;
import org.bank.pay.core.familly.event.kafka.InviteEvent;
import org.bank.pay.core.familly.event.kafka.RequestPaymentEvent;
import org.bank.pay.core.onwer.PayOwner;
import org.bank.pay.core.onwer.repository.PayOwnerReader;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@ComponentScan(basePackages = {"org.bank"})
class FamilyEventTaskTest {

    @Autowired
    private FamilyEventTask familyEventTask;
    @Autowired
    private FamilyService familyService;
    @Autowired
    private CashChargeService cashChargeService;
    @Autowired
    private PayOwnerReader payOwnerReader;


    private final AuthClaims leader = new MemberClaims("leader", "leader", "leader@email.com");
    private final AuthClaims follower = new MemberClaims("follower", "follower", "follower@email.com");

    private Family family;
    private InviteEvent inviteEvent;
    private RequestPaymentEvent requestPaymentEvent;
    private CashConversionEvent cashConversionEvent;

    @BeforeAll
    void setUp() {
        cashChargeService.initializeCash(leader);
        cashChargeService.initializeCash(follower);

        family = familyService.createFamily(leader);

        inviteEvent = new InviteEvent(family.getFamilyId(), follower);
        requestPaymentEvent = new RequestPaymentEvent(family.getFamilyId(), MemberClaims.of(follower), Arrays.asList(new Product("테스트 결제", 10000, 1)));
        cashConversionEvent = new CashConversionEvent(family.getFamilyId(), MemberClaims.of(follower), new Money(1000));
    }

    @Test
    @DisplayName("초대 이벤트를 처리합니다.")
    void 초대_이벤트를_처리합니다() {
        assertThatCode(() -> familyEventTask.processInvitation(inviteEvent))
                        .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("결제 요청 이벤트를 처리합니다")
    void 결제_요청_이벤트를_처리합니다() {
        assertThatCode(() -> familyEventTask.processRequestPayment(requestPaymentEvent))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("송금자의 잔액이 부족한 상태에서 패밀리 캐시 전환 이벤트를 처리합니다.")
    void 송금자의_잔액이_부족한_상태에서_패밀리_캐시_전환_이벤트를_처리합니다() {
        assertThatThrownBy(() -> familyEventTask.processConversion(cashConversionEvent))
                .isInstanceOf(InsufficientBalanceException.class);
    }

    @Test
    @DisplayName("송금자의 잔액을 모두 전환하는 캐시 전환 이벤트를 처리합니다.")
    @Transactional
    void 송금자의_잔액을_모두_전환하는_캐시_전환_이벤트를_처리합니다() {

        PayOwner payOwner = payOwnerReader.findByUserClaims(follower).get();
        payOwner.getCash().charge(new Money(1000));

        assertThatCode(() -> familyEventTask.processConversion(cashConversionEvent))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("송금자의 잔액이 충분한 상태에서 패밀리 캐시 전환 이벤트를 처리합니다.")
    @Transactional
    void 송금자의_잔액이_충분한_상태에서_패밀리_캐시_전환_이벤트를_처리합니다() {

        PayOwner payOwner = payOwnerReader.findByUserClaims(follower).get();
        payOwner.getCash().charge(new Money(10000));

        assertThatCode(() -> familyEventTask.processConversion(cashConversionEvent))
                .doesNotThrowAnyException();
    }
}