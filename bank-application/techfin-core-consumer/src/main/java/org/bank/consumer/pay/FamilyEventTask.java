package org.bank.consumer.pay;

import lombok.RequiredArgsConstructor;
import org.bank.core.cash.PaymentProcessingException;
import org.bank.pay.core.cash.Cash;
import org.bank.pay.core.familly.FamilyService;
import org.bank.pay.core.familly.event.FamilyInvitation;
import org.bank.pay.core.familly.event.FamilyPaymentRequest;
import org.bank.pay.core.familly.event.kafka.CashConversionEvent;
import org.bank.pay.core.familly.event.kafka.InviteEvent;
import org.bank.pay.core.familly.event.kafka.RequestPaymentEvent;
import org.bank.pay.core.familly.repository.FamilyEventStore;
import org.bank.pay.core.history.HistoryService;
import org.bank.pay.core.history.TransferPayHistory;
import org.bank.pay.core.onwer.PayOwner;
import org.bank.pay.core.onwer.repository.PayOwnerReader;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class FamilyEventTask {

    private final FamilyService familyService;
    private final HistoryService historyService;

    private final PayOwnerReader payOwnerReader;
    private final FamilyEventStore familyEventStore;

    public void processInvitation(InviteEvent event) {
        FamilyInvitation invitation = FamilyInvitation.of(event);
        familyEventStore.store(invitation);
    }

    @Transactional
    public void processConversion(CashConversionEvent event) {

        try {
            PayOwner remitter = payOwnerReader.findByUserClaims(event.getFrom()).orElseThrow();
            Cash remitterCredit = remitter.getCash();
            remitterCredit.pay(event.getAmount());

            familyService.depositCashToFamily(event.getFamilyId(), event.getFrom(), event.getAmount());

            TransferPayHistory history = TransferPayHistory.of(event);
            historyService.saveHistory(history);

        } catch (PaymentProcessingException e) {

            TransferPayHistory rollbackHistory = TransferPayHistory.of(event, true);
            historyService.saveHistory(rollbackHistory);

        }
    }

    public void processRequestPayment(RequestPaymentEvent event) {
        FamilyPaymentRequest paymentRequest = FamilyPaymentRequest.of(event);
        familyEventStore.store(paymentRequest);
    }
}
