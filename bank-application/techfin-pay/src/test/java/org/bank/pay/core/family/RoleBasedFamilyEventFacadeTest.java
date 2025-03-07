package org.bank.pay.core.family;

import org.assertj.core.api.Assertions;
import org.bank.core.auth.AuthClaims;
import org.bank.pay.core.domain.familly.Family;
import org.bank.pay.core.domain.familly.MemberClaims;
import org.bank.pay.core.event.family.FamilyEventStatus;
import org.bank.pay.core.event.family.FamilyInvitation;
import org.bank.pay.core.event.family.FamilyPayment;
import org.bank.pay.core.integration.FamilyEventIntegrationTest;
import org.bank.pay.dto.service.request.FamilyEventRequest;
import org.bank.pay.dto.service.request.FamilyEventRequest.Decision;
import org.bank.pay.fixture.FamilyFixture;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RoleBasedFamilyEventFacadeTest extends FamilyEventIntegrationTest {

    @Autowired
    private RoleBasedFamilyEventFacade roleBasedFamilyEventFacade;

    private final AuthClaims leader = FamilyFixture.leader();
    private final AuthClaims member = FamilyFixture.follower();
    private final AuthClaims follower = FamilyFixture.follower("fixture", "초대 받는 사람");


    private Family family;

    private FamilyInvitation invitation;
    private FamilyPayment payment;


    @BeforeAll
    void setUp() {
        init(leader, member);
        family = find(leader);


        FamilyInvitation invitation = FamilyInvitation.builder()
                .familyId(family.getFamilyId())
                .to(MemberClaims.of(follower))
                .status(FamilyEventStatus.PENDING)
                .expiryDate(LocalDateTime.now().plusDays(7))
                .build();

        FamilyPayment payment = FamilyPayment.builder()
                .familyId(family.getFamilyId())
                .from(MemberClaims.of(follower))
                .status(FamilyEventStatus.PENDING)
                .requestDate(LocalDateTime.now())
                .build();



        familyEventStore.store(invitation);
        familyEventStore.store(payment);

        this.invitation = invitation;
        this.payment = payment;
    }


    @Test
    @DisplayName("패밀리 초대 요청을 수락합니다.")
    void 패밀리_초대_요청을_수락합니다() {
        FamilyEventRequest accept = new FamilyEventRequest(family.getFamilyId(), invitation.getId(), Decision.ACCEPT);
        Assertions.assertThatCode(() -> roleBasedFamilyEventFacade.handleInvitation(follower, accept))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("패밀리 초대 요청을 거절합니다.")
    void 패밀리_초대_요청을_거절합니다() {
        FamilyEventRequest reject = new FamilyEventRequest(family.getFamilyId(), invitation.getId(), Decision.REJECT);
        Assertions.assertThatCode(() -> roleBasedFamilyEventFacade.handleInvitation(follower, reject))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("패밀리 결제 요청을 수락합니다.")
    void 패밀리_결제_요청을_수락합니다() {
        FamilyEventRequest accept = new FamilyEventRequest(family.getFamilyId(), payment.getId(), Decision.ACCEPT);
        Assertions.assertThatCode(() -> roleBasedFamilyEventFacade.handlePayment(leader, accept))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("패밀리 결제 요청을 거절합니다.")
    void 패밀리_결제_요청을_거절합니다() {
        FamilyEventRequest reject = new FamilyEventRequest(family.getFamilyId(), payment.getId(), Decision.REJECT);
        Assertions.assertThatCode(() -> roleBasedFamilyEventFacade.handlePayment(leader, reject))
                .doesNotThrowAnyException();
    }

}