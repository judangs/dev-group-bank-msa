package org.bank.store.mysql.core.pay.owner.infrastructure;

import lombok.RequiredArgsConstructor;
import org.bank.pay.core.cash.repository.CashStore;
import org.bank.pay.core.onwer.OwnerClaims;
import org.bank.pay.core.onwer.PayOwner;
import org.bank.pay.core.onwer.PaymentCard;
import org.bank.pay.core.onwer.repository.PayOwnerReader;
import org.bank.pay.core.onwer.repository.PayOwnerStore;
import org.bank.store.mysql.core.pay.owner.JpaClaimsRepository;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OwnerRepository implements PayOwnerReader, PayOwnerStore {

    private final JpaClaimsRepository jpaClaimsRepository;
    private final CashStore cashStore;

    @Override
    public Optional<PayOwner> findByUserClaims(OwnerClaims claims) {
        return jpaClaimsRepository.findByClaimsFromOwner(claims);
    }

    @Override
    public List<PaymentCard> findAllPaymentCardsByOwner(PayOwner payOwner) {
        Optional<PayOwner> optionalPayOwner = findByUserClaims(payOwner.getClaims());
        if(optionalPayOwner.isEmpty()) {
            return Collections.emptyList();
        }

        return optionalPayOwner.get().getPaymentCards();
    }

    @Override
    public Optional<PaymentCard> findPaymentCardByOwnerAndCard(PayOwner payOwner, UUID cardId) {
        List<PaymentCard> cards = findAllPaymentCardsByOwner(payOwner);

        return cards.stream()
                .filter(card -> card.getCardId().equals(cardId)).findFirst();
    }

    @Override
    public void save(PayOwner payOwner) {
        cashStore.save(payOwner.getCash());
    }

    @Override
    public void deleteByOwnerAndCard(PayOwner payOwner, UUID cardId) {
        List<PaymentCard> cards = findAllPaymentCardsByOwner(payOwner);
        cards.removeIf(card -> card.getCardId().equals(cardId));
    }
}
