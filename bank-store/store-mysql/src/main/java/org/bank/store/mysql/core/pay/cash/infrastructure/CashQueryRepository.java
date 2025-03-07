package org.bank.store.mysql.core.pay.cash.infrastructure;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.cash.Money;
import org.bank.core.domain.DomainNames;
import org.bank.pay.core.domain.cash.Cash;
import org.bank.pay.core.domain.cash.repository.CashReader;
import org.bank.pay.core.domain.owner.OwnerClaims;
import org.bank.pay.core.domain.card.PaymentCard;
import org.bank.store.mysql.core.pay.cash.JpaCashRepository;
import org.bank.store.source.DataSourceType;
import org.bank.store.source.NamedRepositorySource;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@NamedRepositorySource(domain = DomainNames.PAY, type = DataSourceType.READWRITE)
@RequiredArgsConstructor
public class CashQueryRepository implements CashReader {

    private final JpaCashRepository jpaCashRepository;


    @Override
    public Cash findByClaimsAndCardId(AuthClaims user, UUID cardId) {
        return jpaCashRepository.findByClaimsFromPayOwnerAndCardId(OwnerClaims.of(user), cardId);
    }

    @Override
    public Cash findByClaimsAndCard(AuthClaims user, PaymentCard card) {
        return jpaCashRepository.findByClaimsFromPayOwnerAndCardId(OwnerClaims.of(user), card.getCardId());
    }


    @Override
    public Money findBalanceByOwnerClaims(AuthClaims user, UUID cardId) {
        return jpaCashRepository.findBalanceByClaimsFromPayOwner(OwnerClaims.of(user), cardId).getCredit();
    }
}
