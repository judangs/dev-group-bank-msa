package org.bank.pay.core.onwer.repository;

import org.bank.pay.core.onwer.PayOwner;

import java.util.UUID;

public interface PayOwnerStore {

    void save(PayOwner payOwner);
    void deleteByOwnerAndCard(PayOwner payOwner, UUID cardId);
}
