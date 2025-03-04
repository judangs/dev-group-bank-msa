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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@NamedRepositorySource(domain = DomainNames.PAY, type = DataSourceType.READWRITE)
@RequiredArgsConstructor
public class OwnerRepository implements PayOwnerReader, PayOwnerStore {

    private final JpaClaimsRepository jpaClaimsRepository;

    @Override
    public Optional<PayOwner> findByUserId(String userid) {
        return jpaClaimsRepository.findByUserIdOfClaims(userid);
    }

    @Override
    public Optional<PayOwner> findByUserClaims(AuthClaims user) {
        return jpaClaimsRepository.findByClaimsFromOwner(OwnerClaims.of(user));
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
    @Transactional
    public void save(PayOwner payOwner) {
        jpaClaimsRepository.save(payOwner);
    }

    @Override
    public void deleteByOwnerAndCard(PayOwner payOwner, UUID cardId) {
        payOwner.getPaymentCards()
                .removeIf(card -> card.getCardId().equals(cardId));
    }
}
