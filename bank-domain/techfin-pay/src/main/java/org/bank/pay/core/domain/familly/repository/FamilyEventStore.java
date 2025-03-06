package org.bank.pay.core.domain.familly.repository;

import org.bank.pay.core.event.family.FamilyEventEntity;

public interface FamilyEventStore {

    void store(FamilyEventEntity event);
}
