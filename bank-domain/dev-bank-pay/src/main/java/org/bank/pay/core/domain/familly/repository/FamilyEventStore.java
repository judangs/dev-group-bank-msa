package org.bank.pay.core.domain.familly.repository;

import org.bank.pay.core.event.family.FamilyInvitation;
import org.bank.pay.core.event.family.FamilyPayment;

public interface FamilyEventStore {
    void store(FamilyInvitation invitation);
    void store(FamilyPayment paymentRequest);
}
