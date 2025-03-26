package org.bank.consumer.core.family;

import lombok.RequiredArgsConstructor;
import org.bank.core.cash.PaymentProcessingException;
import org.bank.pay.core.domain.familly.repository.FamilyEventStore;
import org.bank.pay.core.domain.history.HistoryService;
import org.bank.pay.core.domain.history.TransferPayHistory;
import org.bank.pay.core.domain.owner.repository.PayOwnerReader;
import org.bank.pay.core.event.family.FamilyInvitation;
import org.bank.pay.core.event.family.FamilyPayment;
import org.bank.pay.core.event.family.kafka.CashConversionEvent;
import org.bank.pay.core.event.family.kafka.InviteEvent;
import org.bank.pay.core.event.family.kafka.PaymentEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class FamilyEventTask {

    private final FamilyPaymentConsumerSupportService familyPaymentConsumerSupportService;
    private final HistoryService historyService;

    private final PayOwnerReader payOwnerReader;
    private final FamilyEventStore familyEventStore;

    public void processInvitation(InviteEvent event) {
        FamilyInvitation invitation = FamilyInvitation.of(event);
        familyEventStore.store(invitation);
    }

    @Transactional(transactionManager = "payTransactionManager")
    public void processConversion(CashConversionEvent event) throws PaymentProcessingException{

        try {
            payOwnerReader.findPaymentCardByOwnerAndCard(event.getFrom(), event.getCardId())
                    .ifPresent(card -> familyPaymentConsumerSupportService.deposit(event.getFamilyId(), event.getFrom(), card, event.getAmount()));


            TransferPayHistory history = TransferPayHistory.of(event);
            historyService.saveHistory(history);

        } catch (PaymentProcessingException e) {

            TransferPayHistory rollbackHistory = TransferPayHistory.of(event, true);
            historyService.saveHistory(rollbackHistory);
            throw e;
        }
    }

    public void processRequestPayment(PaymentEvent event) {
        FamilyPayment paymentRequest = FamilyPayment.of(event);
        familyEventStore.store(paymentRequest);
    }
}
