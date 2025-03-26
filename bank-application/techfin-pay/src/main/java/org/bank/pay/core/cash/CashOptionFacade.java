package org.bank.pay.core.cash;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.cash.Money;
import org.bank.core.dto.response.ResponseDtoV2;
import org.bank.pay.core.domain.cash.Cash;
import org.bank.pay.core.domain.cash.repository.CashReader;
import org.bank.pay.core.domain.cash.service.CashLimitService;
import org.bank.pay.dto.service.request.CashLimitRequest;
import org.bank.pay.dto.service.response.CashBalanceResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CashOptionFacade {

    private final CashLimitService cashLimitService;
    private final CashReader cashReader;

    public ResponseDtoV2 applyLimit(AuthClaims user, UUID cardId, CashLimitRequest request) {

        try {
            Cash cash = cashReader.findByClaimsAndCardId(user, cardId);
            cashLimitService.limit(cash, request.getEach(), request.getDaily());

            return ResponseDtoV2.success("한도 설정을 완료했습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseDtoV2.fail(e.getMessage());
        }
    }


    public ResponseDtoV2 checkBalance(AuthClaims user, UUID cardId) {
        Money balance = cashReader.findBalanceByOwnerClaims(user, cardId);
        return new CashBalanceResponse(balance);
    }

}
