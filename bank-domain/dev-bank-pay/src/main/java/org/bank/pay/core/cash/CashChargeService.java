package org.bank.pay.core.cash;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.pay.core.cash.repository.CashReader;
import org.bank.pay.core.cash.repository.CashStore;
import org.bank.pay.core.cash.repository.ReservedCashStore;
import org.bank.pay.core.onwer.OwnerClaims;
import org.bank.pay.core.onwer.PayOwner;
import org.bank.pay.core.onwer.PaymentCard;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class CashChargeService {


    private final CashReader cashReader;
    private final CashStore cashStore;

    private final ReservedCashStore reservedCashStore;

    @Transactional
    public void initializeCash(AuthClaims claims) {
        PayOwner payOwner = new PayOwner(OwnerClaims.of(claims));
        Cash cash = new Cash(payOwner);
        cashStore.save(cash);
    }

    @Transactional
    public void chargeCash(AuthClaims claims, PaymentCard card, BigDecimal amount) {
        validateCharge(card, amount);
        Cash cash = cashReader.findByOwnerClaims(OwnerClaims.of(claims));
        cash.charge(amount);
    }

    @Transactional
    public void chargeCash(Cash cash, PaymentCard card, BigDecimal amount) {
        validateCharge(card, amount);
        cash.charge(amount);
    }

    @Transactional
    public void reserveCharge(AuthClaims authClaims, PaymentCard card, BigDecimal amount, LocalDateTime scheduled) {
        Cash cash = cashReader.findByOwnerClaims((OwnerClaims) authClaims);
        ReservedCharge reservedCharge = ReservedCharge.of(cash, card, amount, ReservationType.DATE, scheduled.toLocalDate());
        validateReservation(reservedCharge);
        reservedCashStore.save(reservedCharge);
    }

    @Transactional
    public void reserveCharge(AuthClaims authClaims, PaymentCard card, BigDecimal amount, BigDecimal balance) {
        Cash cash = cashReader.findByOwnerClaims((OwnerClaims) authClaims);
        ReservedCharge reservedCharge = ReservedCharge.of(cash, card, amount, ReservationType.DATE, balance);
        validateReservation(reservedCharge);
        reservedCashStore.save(reservedCharge);
    }

    public void removeReservedCharge(UUID scheduledId) throws IllegalArgumentException{
        reservedCashStore.deleteByScheduledId(scheduledId);

    }

    @Transactional
    public void refreshDaillyCurrency(Cash cash) {
        cash.refreshDailyCurrency();
    }

    private void validateCharge(PaymentCard card, BigDecimal amount) {
        if (card == null) {
            throw new IllegalArgumentException("유효하지 않은 카드입니다.");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("충전 금액은 0보다 커야 합니다.");
        }
    }

    private void validateReservation(ReservedCharge reservedCharge) {
        if(reservedCharge.getCard() == null || reservedCharge.getCash() == null) {
            throw new IllegalArgumentException("카드와 사용자 정보가 필요합니다.");
        }
        if(reservedCharge.getTriggerBalance() == null && reservedCharge.getScheduledDate() == null) {
            throw new IllegalArgumentException("예약 조건이 부적절합니다.");
        }
    }

}
