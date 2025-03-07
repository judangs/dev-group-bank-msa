package org.bank.consumer.core.family;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.cash.Money;
import org.bank.pay.core.domain.card.PayCard;
import org.bank.pay.core.domain.cash.Cash;
import org.bank.pay.core.domain.cash.service.CashChargeSerivce;
import org.bank.pay.core.domain.cash.service.CashPayService;
import org.bank.pay.core.domain.familly.repository.FamilyReader;
import org.bank.pay.core.domain.card.PaymentCard;
import org.bank.pay.core.domain.familly.service.FamilyPaymentService;
import org.bank.pay.core.domain.owner.repository.PayOwnerReader;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FamilyPaymentConsumerSupportService implements FamilyPaymentService {

    private final CashChargeSerivce cashChargeSerivce;
    private final CashPayService cashPayService;

    private final PayOwnerReader payOwnerReader;
    private final FamilyReader familyReader;

    @Transactional
    public void deposit(UUID familyId, AuthClaims from, PaymentCard card, Money amount) {
        familyReader.findById(familyId).ifPresent(family -> cashChargeSerivce.charge(family.getFamilyCard(), amount));
        cashPayService.pay(card, amount);
    }

    @Transactional
    public void withdraw(UUID familyId, Money amount) {
        familyReader.findById(familyId).ifPresent(family -> cashPayService.pay(family.getFamilyCard(), amount));
    }

    @Override
    public Cash check(AuthClaims user, UUID cardId) {
        return payOwnerReader.findPaymentCardByOwnerAndCard(user, cardId).map(PayCard::getCash)
                .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public Cash check(UUID familyId) {
        return familyReader.findById(familyId).map(family -> family.getFamilyCard().getCash())
                .orElseThrow(IllegalArgumentException::new);
    }

}
