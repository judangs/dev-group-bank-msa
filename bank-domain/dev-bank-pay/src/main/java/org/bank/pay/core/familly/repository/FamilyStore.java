package org.bank.pay.core.familly.repository;

import org.bank.pay.core.familly.Family;

public interface FamilyStore {

    void saveFamily(Family family);
    void deleteFamily(Family family);
}
