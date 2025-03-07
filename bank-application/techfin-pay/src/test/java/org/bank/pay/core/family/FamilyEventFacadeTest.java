package org.bank.pay.core.family;

import org.bank.core.auth.AuthClaims;
import org.bank.core.dto.response.ResponseCodeV2;
import org.bank.core.payment.Product;
import org.bank.pay.core.domain.familly.Family;
import org.bank.pay.core.integration.FamilyEventIntegrationTest;
import org.bank.pay.fixture.FamilyFixture;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FamilyEventFacadeTest extends FamilyEventIntegrationTest {

    @Autowired
    private FamilyEventFacade familyEventFacade;

    private final AuthClaims leader = FamilyFixture.leader();
    private final AuthClaims follower = FamilyFixture.follower();

    private Family family;
    private final Product product = new Product("예제 상품", 1000, 1);

    @BeforeAll
    public void setup() {
        family = init(leader, follower);
    }

    @Test
    @DisplayName("새로운_멤버를_초대합니다.")
    void 새로운_멤버를_초대합니다() {
        assertThat(family).isNotNull();
        assertThatCode(() -> familyEventFacade.invite(family.getFamilyId(), leader, follower.getUserid()))
                        .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("가족 구성원이 결제를 요청합니다")
    void 가족_구성원이_결제를_요청합니다() {
        assertThat(family).isNotNull();
        assertThatCode(() -> familyEventFacade.purchaseProductToFamilyLeader(family.getFamilyId(), follower.getUserid(), product))
                .doesNotThrowAnyException();
        assertThat(familyEventFacade.purchaseProductToFamilyLeader(family.getFamilyId(), follower.getUserid(), product).getCode())
                .isEqualTo(ResponseCodeV2.SUCCESS);
    }

    @Test
    @DisplayName("가족 구성원이 가족 계정 잔액을 충전합니다.")
    void 가족_구성원이_가족_계정_잔액을_충전합니다() {
        assertThat(family).isNotNull();
        assertThatCode(() -> familyEventFacade.purchaseProductToFamilyLeader(family.getFamilyId(), follower.getUserid(), product))
                .doesNotThrowAnyException();
        assertThat(familyEventFacade.purchaseProductToFamilyLeader(family.getFamilyId(), follower.getUserid(), product).getCode())
                .isEqualTo(ResponseCodeV2.SUCCESS);
    }
}