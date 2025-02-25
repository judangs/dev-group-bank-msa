package org.bank.pay.core.cash;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.cash.Money;
import org.bank.core.cash.PaymentProcessingException;
import org.bank.core.dto.response.ResponseDto;
import org.bank.core.dto.response.ResponseDtoV2;
import org.bank.pay.core.domain.cash.service.CashChargeSerivce;
import org.bank.pay.core.domain.owner.PaymentCard;
import org.bank.pay.core.domain.owner.service.PayCardService;
import org.bank.pay.core.payment.PaymentService;
import org.bank.pay.dto.service.request.ChargeRequest;
import org.bank.pay.dto.service.request.ReservedChargeRequest;
import org.bank.pay.dto.service.response.PaymentReserveResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CashFacade {

    private final CashChargeSerivce cashChargeSerivce;
    private final PayCardService payCardService;

    private final PaymentService paymentService;

    public ResponseDtoV2 purchase(AuthClaims user, ChargeRequest request) {

        try {

            String reserve = paymentService.cache(user, request.getPaymentCardId(), new Money(request.getChargeAmount()));
            return new PaymentReserveResponse(reserve);

        } catch (PaymentProcessingException e) {
            return ResponseDtoV2.fail(e.getMessage());
        }
    }


    public ResponseDtoV2 reserveReCharge(AuthClaims user, ReservedChargeRequest request) {

        PaymentCard paymentCard = payCardService.get(user, request.getPaymentCardId());
        BigDecimal chargeAmount = request.getChargeAmount();

        if(request.getDate() != null) {
            cashChargeSerivce.reserve(user, paymentCard, chargeAmount, request.getDate());
            return ResponseDtoV2.success("예약 충전 설정을 완료했습니다.");
        }
        if(request.getBalance() != null) {
            cashChargeSerivce.reserve(user, paymentCard, chargeAmount, request.getBalance());
            return ResponseDtoV2.success("예약 충전 설정을 완료했습니다.");
        }
        else
            return ResponseDtoV2.fail("조건을 설정해야 합니다.");
    }

    public ResponseDto cancelReCharge(UUID scheduledId) {
        cashChargeSerivce.cancel(scheduledId);
        return ResponseDto.success("예약 설정을 취소했습니다.");
    }
}
