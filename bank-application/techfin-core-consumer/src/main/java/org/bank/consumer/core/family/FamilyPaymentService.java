package org.bank.consumer.core.family;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.cash.Money;
import org.bank.pay.core.domain.cash.service.CashChargeSerivce;
import org.bank.pay.core.domain.cash.service.CashPayService;
import org.bank.pay.core.domain.familly.repository.FamilyReader;
import org.bank.pay.core.domain.owner.PaymentCard;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FamilyPaymentService {

    private final CashChargeSerivce cashChargeSerivce;
    private final CashPayService cashPayService;

    private final FamilyReader familyReader;

    @Transactional
    public void depositCashToFamily(UUID familyId, AuthClaims from, PaymentCard card, Money amount) {
        familyReader.findById(familyId).ifPresent(family -> cashChargeSerivce.charge(family.getFamilyCard(), amount));
        cashPayService.pay(card, amount);
    }

    @Transactional
    public void withdrawCashFromFamily(UUID familyId, Money cashToTransfer) {
        familyReader.findById(familyId).ifPresent(family -> cashPayService.pay(family.getFamilyCard(), cashToTransfer));
    }
}
