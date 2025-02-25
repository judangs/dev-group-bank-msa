package org.bank.store.mysql.core.pay.cash;

import org.bank.pay.core.domain.cash.Cash;
import org.bank.pay.core.domain.owner.OwnerClaims;
import org.bank.store.mysql.global.jpa.JpaBaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaCashRepository extends JpaBaseRepository<Cash, UUID> {

    @Query("SELECT pc.cash FROM PaymentCard pc JOIN pc.payOwner po WHERE po.claims.userid = :#{#owner.userid} AND pc.cardId = :cardId")
    Cash findByClaimsFromPayOwnerAndCardId(@Param("owner")OwnerClaims owner, UUID cardId);

    @Query("SELECT pc.cash FROM PaymentCard pc JOIN pc.payOwner po WHERE pc.payOwner.claims.userid = :#{#owner.userid} AND pc.cardId = :cardId")
    Cash findBalanceByClaimsFromPayOwner(@Param("owner")OwnerClaims owner, UUID cardId);

}
