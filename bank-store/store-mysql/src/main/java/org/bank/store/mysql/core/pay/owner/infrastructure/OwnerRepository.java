package org.bank.store.mysql.core.pay.owner.infrastructure;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.domain.DomainNames;
import org.bank.pay.core.domain.owner.OwnerClaims;
import org.bank.pay.core.domain.owner.PayOwner;
import org.bank.pay.core.domain.owner.PaymentCard;
import org.bank.pay.core.domain.owner.repository.PayOwnerReader;
import org.bank.pay.core.domain.owner.repository.PayOwnerStore;
import org.bank.store.mysql.core.pay.owner.JpaClaimsRepository;
import org.bank.store.source.DataSourceType;
import org.bank.store.source.NamedRepositorySource;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@NamedRepositorySource(domain = DomainNames.PAY, type = DataSourceType.READWRITE)
@RequiredArgsConstructor
public class OwnerRepository implements PayOwnerReader, PayOwnerStore {

    private final JpaClaimsRepository jpaClaimsRepository;

    @Override
    public Optional<PayOwner> findByUserClaims(OwnerClaims claims) {
        return jpaClaimsRepository.findByClaimsFromOwner(claims);
    }

    @Override
    public Optional<PayOwner> findByUserClaims(AuthClaims claims) {
        return findByUserClaims(OwnerClaims.of(claims));
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
    public List<PaymentCard> findPaymentCardsByUser(AuthClaims user) {
        return findByUserClaims(OwnerClaims.of(user)).map(PayOwner::getPaymentCards)
                .orElseThrow(() -> new IllegalArgumentException("결제 카드가 존재하지 않습니다."));
    }

    @Override
    public Optional<PaymentCard> findPaymentCardByOwnerAndCard(AuthClaims user, UUID cardId) {
        return findPaymentCardsByUser(user).stream()
                .filter(card -> card.getCardId().equals(cardId)).findFirst();
    }

    @Override
    public Optional<PaymentCard> findPaymentCardByOwnerAndCard(PayOwner payOwner, UUID cardId) {
        List<PaymentCard> cards = findAllPaymentCardsByOwner(payOwner);

        return cards.stream()
                .filter(card -> card.getCardId().equals(cardId)).findFirst();
    }

    @Override
    public void save(PayOwner payOwner) {
        jpaClaimsRepository.save(payOwner);
    }

    @Override
    public void deleteByOwnerAndCard(PayOwner payOwner, UUID cardId) {
        List<PaymentCard> cards = findAllPaymentCardsByOwner(payOwner);
        cards.removeIf(card -> card.getCardId().equals(cardId));
    }
}
