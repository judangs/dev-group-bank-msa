package org.bank.store.mysql.core.pay.cash;

import org.bank.core.cash.Money;
import org.bank.pay.core.cash.Cash;
import org.bank.pay.core.onwer.OwnerClaims;
import org.bank.store.mysql.global.infrastructure.JpaBaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaCashRepository extends JpaBaseRepository<Cash, UUID> {

    @Query("SELECT c FROM Cash c WHERE c.payOwner.claims.userid = :#{#ownerClaims.userid}")
    Cash findByClaimsFromPayOwner(@Param("ownerClaims")OwnerClaims ownerClaims);

    @Query("SELECT c.credit FROM Cash c WHERE c.payOwner.claims.userid = :#{#ownerClaims.userid}")
    Money findBalanceByClaimsFromPayOwner(@Param("ownerClaims")OwnerClaims ownerClaims);

}
