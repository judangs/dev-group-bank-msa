package org.bank.pay.core.family;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.cash.Money;
import org.bank.core.dto.response.ResponseCodeV2;
import org.bank.core.dto.response.ResponseDtoV2;
import org.bank.core.payment.Product;
import org.bank.pay.core.event.family.service.FamilyEventService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FamilyEventFacade {

    private final FamilyEventService familyEventService;

    public ResponseDtoV2 invite(UUID familyId, AuthClaims leader, String userid) {

        ResponseCodeV2 code = familyEventService.invite(familyId, leader, userid);
        return switch (code) {
            case SUCCESS -> ResponseDtoV2.success("패밀리에 초대를 요청했습니다.");
            case NOT_FOUND -> ResponseDtoV2.of(code, "유저를 찾을 수 없습니다. 다시 시도해주세요.");
            default -> ResponseDtoV2.fail("멤버 초대에 실패했습니다.");
        };
    }

    public ResponseDtoV2 purchaseProductToFamilyLeader(UUID familyId, String userid, Product product) {
        ResponseCodeV2 code = familyEventService.order(familyId, userid, product);
        return switch (code) {
            case SUCCESS -> ResponseDtoV2.success("패밀리에 초대를 요청했습니다.");
            case NOT_FOUND -> ResponseDtoV2.of(code, "해당 유저는 패밀리 멤버가 아닙니다.");
            default -> ResponseDtoV2.fail("패밀리 결제 요청에 실패했습니다.");
        };
    }

    public ResponseDtoV2 convertPersonalCashToFamilyCash(AuthClaims user, UUID familyId, UUID cardId, Money amount) {
        ResponseCodeV2 code = familyEventService.charge(user, familyId, cardId, amount);
        return switch (code) {
            case SUCCESS -> ResponseDtoV2.success("온라인 캐시를 패밀리 캐시로 전환합니다.");
            case NOT_FOUND -> ResponseDtoV2.of(code, "해당 유저는 패밀리 멤버가 아닙니다.");
            default -> ResponseDtoV2.fail("패밀리 캐시 충전에 실패했습니다.");
        };
    }
}
