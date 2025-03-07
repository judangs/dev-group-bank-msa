package org.bank.pay.core.event.family.service;

import org.bank.core.auth.AuthClaims;
import org.bank.core.cash.Money;
import org.bank.core.dto.response.ResponseCodeV2;
import org.bank.core.payment.Product;

import java.util.UUID;

public interface FamilyEventService {
    ResponseCodeV2 invite(UUID familyId, AuthClaims leader, String userid);
    ResponseCodeV2 order(UUID familyId, String userid, Product product);
    ResponseCodeV2 charge(AuthClaims user, UUID familyId, UUID cardId, Money amount);
}