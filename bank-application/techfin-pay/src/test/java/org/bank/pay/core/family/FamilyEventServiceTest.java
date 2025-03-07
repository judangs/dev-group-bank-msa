package org.bank.pay.core.family;


import org.bank.core.cash.Money;
import org.bank.core.dto.response.ResponseCodeV2;
import org.bank.core.payment.Product;
import org.bank.pay.core.domain.familly.Family;
import org.bank.pay.core.domain.familly.MemberClaims;
import org.bank.pay.core.domain.familly.service.FamilyService;
import org.bank.pay.core.event.family.service.FamilyEventService;
import org.bank.pay.core.integration.FamilyEventIntegrationTest;
import org.bank.pay.fixture.FamilyFixture;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FamilyEventServiceTest extends FamilyEventIntegrationTest {

    @Autowired
    private FamilyEventService familyEventService;
    @Autowired
    private FamilyService familyService;

    private final MemberClaims leader = FamilyFixture.leader();
    private final MemberClaims follower = FamilyFixture.follower();

    private Family family;


    @BeforeAll
    public void setup() {
        family = init(leader);
        integrationTestInitializer.init(follower);
    }

    @Test
    void 회원초대_이벤트를_처리할_때_회원을_확인하고_이벤트를_처리한_뒤_응답_코드를_반환합니다() {
        assertThat(familyEventService.invite(family.getFamilyId(), leader, follower.getUserid()))
                .isEqualTo(ResponseCodeV2.SUCCESS);
    }

    @Test
    void 패밀리_구성원의_상품_결제에_대한_요청에_대해_이벤트를_처리하고_응답_코드를_반환합니다() {
        familyService.addMemberToFamily(family.getFamilyId(), follower);
        assertThat(familyEventService.order(family.getFamilyId(), follower.getUserid(), new Product("테스트 결제", new Money(10000), 1)))
                .isEqualTo(ResponseCodeV2.SUCCESS);
    }

    @Test
    void 패밀리_구성원이_아닌_사용자의_상품_결제에_대한_요청에_대해_이벤트를_처리하고_응답_코드를_반환합니다() {
        assertThat(familyEventService.order(family.getFamilyId(), follower.getUserid(), new Product("테스트 결제", new Money(10000), 1)))
                .isEqualTo(ResponseCodeV2.NOT_FOUND);
    }

    @Test
    void 패밀리_구성원의_캐시_전환에_대한_요청에_대해_이벤트를_처리하고_완료_응답_코드를_반환합니다() {
        familyService.addMemberToFamily(family.getFamilyId(), follower);

        integrationTestInitializer.card(follower).ifPresent(card ->
                assertThat(familyEventService.charge(follower, family.getFamilyId(), card.getCardId(), new Money(1000)))
                        .isEqualTo(ResponseCodeV2.SUCCESS));
    }

    @Test
    void 결제_금액이_부족한_패밀리_구성원의_캐시_전환에_대한_요청에_대해_이벤트를_처리하고_응답_코드를_반환합니다() {
        familyService.addMemberToFamily(family.getFamilyId(), follower);

        integrationTestInitializer.card(follower).ifPresent(card ->
                assertThat(familyEventService.charge(follower, family.getFamilyId(), card.getCardId(), new Money(100000)))
                        .isEqualTo(ResponseCodeV2.FAIL));
    }

    @Test
    void 패밀리_구성원이_아닌_사용자의_캐시_전환에_대한_요청에_대해_이벤트를_처리하고_응답_코드를_반환합니다() {
        integrationTestInitializer.card(follower).ifPresent(card ->
                assertThat(familyEventService.charge(follower, family.getFamilyId(), card.getCardId(), new Money(1000)))
                        .isEqualTo(ResponseCodeV2.NOT_FOUND));
    }


}
