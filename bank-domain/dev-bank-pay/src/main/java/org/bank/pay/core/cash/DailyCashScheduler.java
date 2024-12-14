package org.bank.pay.core.cash;

import lombok.RequiredArgsConstructor;
import org.bank.pay.core.cash.repository.DaillySpentStore;
import org.bank.pay.core.cash.repository.ReservedCashReader;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DailyCashScheduler {

    private final CashChargeService cashChargeService;

    private final DaillySpentStore daillySpentStore;
    private final ReservedCashReader reservedCashReader;

    @Scheduled(cron = "0 0 0 * * ?")
    public void resetDailyCashUsage() {
        daillySpentStore.resetDailySpent();
    }


    @Scheduled(cron = "0 0 0 * * ?")
    public void isEligibleForDateBasedCharge() {
        List<ReservedCharge> reservedCharges = reservedCashReader.findAllReservedCharges();
        reservedCharges.stream()
                .filter((reservedCharge) -> isEligibleForDateBasedCharge(reservedCharge))
                .forEach(reservedCharge -> {
                    cashChargeService.chargeCash(reservedCharge.getCash(), reservedCharge.getCard(), reservedCharge.getAmount());
                });
    }

    @Scheduled(fixedRate = 60000) // 1분마다 실행
    public void checkAndTriggerCharges() {
        List<ReservedCharge> reservedCharges = reservedCashReader.findAllReservedCharges();
        reservedCharges.stream()
                .filter((reservedCharge) ->isEligibleForBalanceBasedCharge(reservedCharge))
                .forEach(reservedCharge -> {
                    cashChargeService.chargeCash(reservedCharge.getCash(), reservedCharge.getCard(), reservedCharge.getAmount());
                });
    }

    private boolean isEligibleForBalanceBasedCharge(ReservedCharge charge) {
        return charge.getTriggerBalance() != null && charge.getCash().getCredit().getBalance()
                .compareTo(charge.getTriggerBalance()) <= 0;
    }

    private boolean isEligibleForDateBasedCharge(ReservedCharge charge) {
        return charge.getScheduledDate() != null && charge.getScheduledDate().equals(LocalDate.now());
    }

}
