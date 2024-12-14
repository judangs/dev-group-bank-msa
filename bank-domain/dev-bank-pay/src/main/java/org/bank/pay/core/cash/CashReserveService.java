package org.bank.pay.core.cash;

import lombok.RequiredArgsConstructor;
import org.bank.pay.core.cash.repository.ReservedCashReader;
import org.bank.pay.core.cash.repository.ReservedCashStore;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CashReserveService {

    private final ReservedCashStore reservedCashStore;
    private final ReservedCashReader reservedCashReader;

    public ReservedCharge createReservedCharge(ReservedCharge reservedCharge,  BigDecimal triggerBalance) throws IllegalArgumentException{
        reservedCharge.setTriggerBalance(triggerBalance);
        validateReservation(reservedCharge);
        reservedCashStore.save(reservedCharge);

        return reservedCharge;
    }


    public ReservedCharge createReservedCharge(ReservedCharge reservedCharge, LocalDate scheduledDate) throws IllegalArgumentException{
        reservedCharge.setScheduledDate(scheduledDate);
        validateReservation(reservedCharge);
        reservedCashStore.save(reservedCharge);

        return reservedCharge;
    }

    public void removeReservedCharge(UUID scheduledId) throws IllegalArgumentException{
        reservedCashStore.deleteByScheduledId(scheduledId);

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
