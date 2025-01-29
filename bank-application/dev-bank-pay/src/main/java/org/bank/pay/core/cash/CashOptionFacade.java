package org.bank.pay.core.cash;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.cash.Money;
import org.bank.core.dto.response.ResponseCode;
import org.bank.core.dto.response.ResponseDto;
import org.bank.core.dto.response.ResponseMessage;
import org.bank.pay.core.domain.cash.CashLimitService;
import org.bank.pay.core.domain.cash.repository.CashReader;
import org.bank.pay.core.domain.onwer.OwnerClaims;
import org.bank.pay.dto.service.request.CashLimitRequest;
import org.bank.pay.dto.service.response.CashBalanceResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CashOptionFacade {

    private final CashLimitService cashLimitService;
    private final CashReader cashReader;

    public ResponseDto applyCashLimit(AuthClaims authClaims, CashLimitRequest request) {

        if(request.getDaily().equals(BigDecimal.ZERO) && request.getEach().equals(BigDecimal.ZERO)) {
            cashLimitService.clearPaymentLimits(authClaims);
        }
        else {
            cashLimitService.setPaymentLimits(authClaims, request.getEach(), request.getDaily());
        }

        return ResponseDto.success("");
    }


    public ResponseDto checkBalance(AuthClaims authClaims) {
        Money balance = cashReader.findBalanceByOwnerClaims((OwnerClaims) authClaims);
        return new CashBalanceResponse(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, balance);
    }

}
