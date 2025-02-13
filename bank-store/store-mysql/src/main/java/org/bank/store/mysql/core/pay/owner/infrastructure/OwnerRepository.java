package org.bank.store.mysql.core.pay.owner.infrastructure;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.domain.DomainNames;
import org.bank.pay.core.domain.cash.repository.CashStore;
import org.bank.pay.core.domain.onwer.OwnerClaims;
import org.bank.pay.core.domain.onwer.PayOwner;
import org.bank.pay.core.domain.onwer.PaymentCard;
import org.bank.pay.core.domain.onwer.repository.PayOwnerReader;
import org.bank.pay.core.domain.onwer.repository.PayOwnerStore;
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
    private final CashStore cashStore;

    @Override
    public Optional<PayOwner> findByUserClaims(OwnerClaims claims) {
        Optional<PayOwner> payOwner = jpaClaimsRepository.findByClaimsFromOwner(claims);
        if(payOwner.isEmpty()) {
            throw new EntityNotFoundException("정보를 찾을 수 없습니다.");
        }
        return payOwner;
    }

    @Override
    public Optional<PayOwner> findByUserClaims(AuthClaims claims) {
        return findByUserClaims(OwnerClaims.of(claims));
    }

    @Override
    public Optional<PayOwner> findByUserClaimsAndRoles(AuthClaims claims, String roles) {
        return jpaClaimsRepository.findByClaimsFromOwner(OwnerClaims.of(claims, roles));
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
