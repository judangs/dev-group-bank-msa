package org.bank.pay.core.familly.repository;

import org.bank.pay.core.familly.event.FamilyInvitation;
import org.bank.pay.core.familly.event.FamilyPaymentRequest;

public interface FamilyEventStore {
    void store(FamilyInvitation invitation);
    void store(FamilyPaymentRequest paymentRequest);
}
