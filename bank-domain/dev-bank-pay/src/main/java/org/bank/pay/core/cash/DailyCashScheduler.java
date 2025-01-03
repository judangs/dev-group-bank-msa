package org.bank.pay.core.cash;

import lombok.RequiredArgsConstructor;
import org.bank.pay.core.cash.repository.CashReader;
import org.bank.pay.core.cash.repository.ReservedCashReader;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DailyCashScheduler {

    private final CashChargeService cashChargeService;

    private final CashReader cashReader;
    private final ReservedCashReader reservedCashReader;

    @Scheduled(cron = "0 0 0 * * ?")
    public void resetDailyCashUsage() {

        int page = 0;
        Page<Cash> cashes;
        while((cashes = cashReader.findAll(page++)).hasContent()) {
            cashes.getContent()
                    .forEach(cashChargeService::refreshDaillyCurrency);
        }
    }


    @Scheduled(cron = "0 0 0 * * ?")
    public void isEligibleForDateBasedCharge() {

        int page = 0;
        Page<ReservedCharge> reservedCharges;

        while((reservedCharges = reservedCashReader.findAllReservedCharges(page++)).hasContent()) {

            reservedCharges.getContent().stream()
                    .filter(this::isEligibleForDateBasedCharge)
                    .forEach(reservedCharge -> {
                        cashChargeService.chargeCash(reservedCharge.getCash(), reservedCharge.getCard(), reservedCharge.getAmount());
                    });

        }
    }

    @Scheduled(fixedRate = 60000) // 1분마다 실행
    public void checkAndTriggerCharges() {

        int page = 0;
        Page<ReservedCharge> reservedCharges;

        while((reservedCharges = reservedCashReader.findAllReservedCharges(page++)).hasContent()) {

            reservedCharges.getContent().stream()
                    .filter(this::isEligibleForBalanceBasedCharge)
                    .forEach(reservedCharge -> {
                        cashChargeService.chargeCash(reservedCharge.getCash(), reservedCharge.getCard(), reservedCharge.getAmount());
                    });

        }
    }

    private boolean isEligibleForBalanceBasedCharge(ReservedCharge charge) {
        return charge.getTriggerBalance() != null && charge.getCash().getCredit().getBalance()
                .compareTo(charge.getTriggerBalance()) <= 0;
    }

    private boolean isEligibleForDateBasedCharge(ReservedCharge charge) {
        return charge.getScheduledDate() != null && charge.getScheduledDate().equals(LocalDate.now());
    }

}
