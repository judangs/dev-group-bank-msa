package org.bank.consumer.core.product;

import lombok.RequiredArgsConstructor;
import org.bank.core.cash.PayMethod;
import org.bank.pay.core.domain.cash.service.CashChargeSerivce;
import org.bank.pay.core.domain.history.HistoryService;
import org.bank.pay.core.domain.history.OrderPayHistory;
import org.bank.pay.core.domain.history.ReChargePayHistory;
import org.bank.pay.core.domain.owner.repository.PayOwnerReader;
import org.bank.pay.core.event.product.PurchasedEvent;
import org.bank.pay.core.event.product.VirtualCash;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class PurchasedEventTask {

    private final PayOwnerReader payOwnerReader;
    private final CashChargeSerivce cashChargeSerivce;

    private final HistoryService historyService;

    @Transactional(transactionManager = "payTransactionManager")
    public void processCharge(PurchasedEvent event) {

        if(event.getProduct() instanceof VirtualCash virtualCash) {

            payOwnerReader.findPaymentCardByOwnerAndCard(event.getUser(), virtualCash.getCardId())
                            .ifPresent(card -> cashChargeSerivce.charge(event.getUser(), card, new BigDecimal(event.getTotalPayAmount())));
        }

        historyService.saveHistory(ReChargePayHistory.of(event.getUser(), event.getPaymentId(), event.getProduct(), PayMethod.CASH));
    }

    public void processPurchased(PurchasedEvent event) {
        historyService.saveHistory(OrderPayHistory.of(event.getUser(), event.getPaymentId(), event.getProduct()));
    }
}
