package org.bank.pay.core.family;

import org.assertj.core.api.Assertions;
import org.bank.core.auth.AuthClaims;
import org.bank.pay.core.domain.familly.Family;
import org.bank.pay.core.domain.familly.FamilyService;
import org.bank.pay.core.domain.familly.MemberClaims;
import org.bank.pay.core.domain.familly.repository.FamilyEventReader;
import org.bank.pay.core.domain.familly.repository.FamilyStore;
import org.bank.pay.core.domain.owner.OwnerClaims;
import org.bank.pay.core.event.family.FamilyEventStatus;
import org.bank.pay.core.event.family.FamilyInvitation;
import org.bank.pay.core.event.family.FamilyPayment;
import org.bank.pay.dto.service.request.FamilyEventRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RoleBasedFamilyEventFacadeTest.TestConfig.class)
class RoleBasedFamilyEventFacadeTest {

    @Autowired
    private FamilyService familyService;

    @Configuration
    @ComponentScan(basePackages = {
            "org.bank.pay.core",
            "org.bank.pay.global",
            "org.bank.store.mysql.core.pay"

    })
    static class TestConfig { }

    @Autowired
    private RoleBasedFamilyEventFacade roleBasedFamilyEventFacade;
    @Autowired
    private FamilyStore familyStore;
    @MockBean
    private FamilyEventReader familyEventReader;


    private final AuthClaims leader = new OwnerClaims("leader", "leader", "leader@email.com");

    private final FamilyInvitation invitation = FamilyInvitation.builder()
            .status(FamilyEventStatus.PENDING)
            .to(MemberClaims.of(leader))
            .expiryDate(LocalDateTime.now().plusDays(7))
            .build();

    private final FamilyPayment payment = FamilyPayment.builder()
            .from(MemberClaims.of(leader))
            .status(FamilyEventStatus.PENDING)
            .requestDate(LocalDateTime.now())
            .build();

    private Family family;
    private FamilyEventRequest acceptFamilyEventRequest;
    private FamilyEventRequest rejectFamilyEventRequest;

    @BeforeEach
    void setUp() {
        when(familyEventReader.findInvitationEventById(any())).thenReturn(Optional.ofNullable(invitation));
        when(familyEventReader.findPaymentRequestEventByFamilyAndId(any(), any())).thenReturn(Optional.ofNullable(payment));

        family = familyService.createFamily(leader);

        acceptFamilyEventRequest = new FamilyEventRequest(family.getFamilyId(), UUID.randomUUID(), FamilyEventRequest.Decision.ACCEPT);
        rejectFamilyEventRequest = new FamilyEventRequest(family.getFamilyId(), UUID.randomUUID(), FamilyEventRequest.Decision.REJECT);
    }

    @AfterEach
    void tearDown() {
        familyStore.deleteFamily(family);
    }


    @Test
    @DisplayName("패밀리 초대 요청을 수락합니다.")
    void 패밀리_초대_요청을_수락합니다() {
        Assertions.assertThatCode(() -> roleBasedFamilyEventFacade.handleInvitation(leader, acceptFamilyEventRequest))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("패밀리 초대 요청을 거절합니다.")
    void 패밀리_초대_요청을_거절합니다() {
        Assertions.assertThatCode(() -> roleBasedFamilyEventFacade.handleInvitation(leader, rejectFamilyEventRequest))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("패밀리 결제 요청을 수락합니다.")
    void 패밀리_결제_요청을_수락합니다() {
        Assertions.assertThatCode(() -> roleBasedFamilyEventFacade.handlePayment(leader, acceptFamilyEventRequest))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("패밀리 결제 요청을 거절합니다.")
    void 패밀리_결제_요청을_거절합니다() {
        Assertions.assertThatCode(() -> roleBasedFamilyEventFacade.handleInvitation(leader, rejectFamilyEventRequest))
                .doesNotThrowAnyException();
    }

}