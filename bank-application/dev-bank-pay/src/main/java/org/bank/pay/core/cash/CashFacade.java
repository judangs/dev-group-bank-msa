package org.bank.pay.core.cash;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.cash.Money;
import org.bank.core.dto.pay.ChargeResponse;
import org.bank.core.dto.response.ResponseCode;
import org.bank.core.dto.response.ResponseDto;
import org.bank.pay.client.CashClient;
import org.bank.pay.core.history.HistoryService;
import org.bank.pay.core.onwer.PaymentCard;
import org.bank.pay.core.onwer.PaymentCardService;
import org.bank.pay.dto.service.request.ChargeRequest;
import org.bank.pay.dto.service.request.ReservedChargeRequest;
import org.bank.pay.global.exception.PaymentException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CashFacade {

    private final CashChargeService cashChargeService;
    private final PaymentCardService paymentCardService;
    private final HistoryService historyService;

    private final CashClient mockCashClient;

    public ResponseDto chargeCash(AuthClaims authClaims, ChargeRequest request) {
        PaymentCard paymentCard = paymentCardService.getRegisteredCard(authClaims, request.getPaymentCardId());
        BigDecimal chargeAmount = request.getChargeAmount();

        try {

            ChargeResponse chargeResponse = mockCashClient.processPayment(paymentCard, new Money(chargeAmount));
            if (!chargeResponse.getCode().equals(ResponseCode.SUCCESS)) {
                throw new PaymentException(chargeResponse.getMessage());
            }

            cashChargeService.chargeCash(authClaims, paymentCard, chargeAmount);
            historyService.saveReChargeHistory(authClaims, chargeResponse, paymentCard.getCardNumber());

        } catch (Exception e) {
            return ResponseDto.fail(e.getMessage());
        }

        return ResponseDto.success("캐시 충전에 성공했습니다.");
    }


    public ResponseDto reserveReCharge(AuthClaims authClaims, ReservedChargeRequest request) {

        PaymentCard paymentCard = paymentCardService.getRegisteredCard(authClaims, request.getPaymentCardId());
        BigDecimal chargeAmount = request.getChargeAmount();

        if(request.getDate() != null) {
            cashChargeService.reserveCharge(authClaims, paymentCard, chargeAmount, request.getDate());
            return ResponseDto.success("예약 충전 설정을 완료했습니다.");
        }
        if(request.getBalance() != null) {
            cashChargeService.reserveCharge(authClaims, paymentCard, chargeAmount, request.getBalance());
            return ResponseDto.success("예약 충전 설정을 완료했습니다.");
        }
        else
            return ResponseDto.fail("조건을 설정해야 합니다.");
    }

    public ResponseDto cancelReCharge(UUID scheduledId) {
        cashChargeService.removeReservedCharge(scheduledId);
        return ResponseDto.success("예약 설정을 취소했습니다.");
    }
}
