package org.bank.pay.core.domain.familly.repository;

import org.bank.pay.core.domain.familly.Family;

public interface FamilyStore {

    void saveFamily(Family family);
    void deleteFamily(Family family);
}
