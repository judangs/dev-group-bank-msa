package org.bank.pay.core.domain.owner.repository;

import org.bank.pay.core.domain.owner.PayOwner;

import java.util.UUID;

public interface PayOwnerStore {

    void save(PayOwner payOwner);
    void deleteByOwnerAndCard(PayOwner payOwner, UUID cardId);
}
