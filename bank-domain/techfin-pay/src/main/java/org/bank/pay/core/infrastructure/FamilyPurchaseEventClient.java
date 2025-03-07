package org.bank.pay.core.infrastructure;

import org.bank.core.cash.Money;
import org.bank.core.payment.Product;
import org.bank.pay.core.domain.familly.Family;
import org.bank.pay.core.domain.familly.MemberClaims;
import org.bank.pay.core.event.family.FamilyPayment;
import org.bank.pay.core.domain.card.PayCard;

public interface FamilyPurchaseEventClient {
    void request(Family family, MemberClaims from, Product product);
    void approval(FamilyPayment payment);
    void conversion(Family family, MemberClaims from, PayCard card, Money amount);
}