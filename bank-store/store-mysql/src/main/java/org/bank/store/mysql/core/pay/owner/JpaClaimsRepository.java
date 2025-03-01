package org.bank.store.mysql.core.pay.owner;

import org.bank.pay.core.domain.owner.OwnerClaims;
import org.bank.pay.core.domain.owner.PayOwner;
import org.bank.store.mysql.global.jpa.JpaBaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface JpaClaimsRepository extends JpaBaseRepository<PayOwner, UUID>{

    @Query("SELECT o FROM PayOwner o WHERE o.claims.userid = :#{#owner.userid}")
    Optional<PayOwner> findByClaimsFromOwner(@Param("owner") OwnerClaims owner);
}

