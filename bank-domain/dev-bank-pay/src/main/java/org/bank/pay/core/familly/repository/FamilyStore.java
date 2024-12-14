package org.bank.pay.core.familly.repository;

import org.bank.pay.core.familly.Family;
import org.bank.pay.core.familly.MemberClaims;

public interface FamilyStore {

    void saveFamily(MemberClaims memberClaims);
    void saveFamily(Family family);
}
