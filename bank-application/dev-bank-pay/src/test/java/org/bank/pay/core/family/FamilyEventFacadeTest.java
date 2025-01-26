package org.bank.pay.core.family;

import org.assertj.core.api.Assertions;
import org.bank.core.auth.AuthClaims;
import org.bank.core.payment.Product;
import org.bank.pay.core.cash.UserRegisterTask;
import org.bank.pay.core.familly.Family;
import org.bank.pay.core.familly.FamilyService;
import org.bank.pay.core.familly.MemberClaims;
import org.bank.pay.core.familly.repository.FamilyReader;
import org.bank.pay.core.onwer.OwnerClaims;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = FamilyEventFacadeTest.TestConfig.class)
class FamilyEventFacadeTest {

    @Configuration
    @ComponentScan(basePackages = {
            "org.bank.pay.core",
            "org.bank.pay.global",
            "org.bank.store.mysql.core.pay"

    })
    static class TestConfig { }

    @Autowired
    private FamilyEventFacade familyEventFacade;
    @Autowired
    private FamilyService familyService;
    @Autowired
    private UserRegisterTask userRegisterTask;
    @Autowired
    private FamilyReader familyReader;

    private final AuthClaims leader = new OwnerClaims("fixture-id", "fixture", "fixture@email.com");
    private final MemberClaims follower = new MemberClaims("follower", "follower", "follower@email.com");

    private Family family;

    @BeforeAll
    public void setup() {
        userRegisterTask.initialize(leader);
        familyService.createFamily((OwnerClaims) leader);

        MemberClaims familyLeader = new MemberClaims("fixture-id", "fixture", "fixture@email.com");
        family = familyReader.findByUserIsLeader(familyLeader).get();
    }

    @Test
    @DisplayName("새로운_멤버를_초대합니다.")
    void 새로운_멤버를_초대합니다() {

        Assertions.assertThat(family).isNotNull();
        Assertions.assertThatCode(() -> familyEventFacade.inviteMember(family.getFamilyId(), follower))
                        .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("가족 구성원이 결제를 요청합니다")
    void 가족_구성원이_결제를_요청합니다() {

        MemberClaims participant = new MemberClaims("request", "participant", "participant@email.com");
        familyService.addMemberToFamily(family.getFamilyId(), participant);

        Assertions.assertThat(family).isNotNull();
        Assertions.assertThatCode(() -> familyEventFacade.requestPayment(family.getFamilyId(), participant.getUserid(), new Product("예제 상품", 1000, 1)))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("가족 구성원이 가족 계정 잔액을 충전합니다.")
    void 가족_구성원이_가족_계정_잔액을_충전합니다() {

        MemberClaims participant = new MemberClaims("request", "participant", "participant@email.com");
        familyService.addMemberToFamily(family.getFamilyId(), participant);

        Assertions.assertThat(family).isNotNull();
        Assertions.assertThatCode(() -> familyEventFacade.requestPayment(family.getFamilyId(), participant.getUserid(), new Product("예제 상품", 1000, 1)))
                .doesNotThrowAnyException();
    }
}