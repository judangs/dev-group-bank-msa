package org.bank.store.mysql.core.persistence.transaction;

import org.bank.core.domain.DomainNames;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Component
public class DomainTransactionConverter {

    public DomainNames convert(PlatformTransactionManager platformTransactionManager) {
        if(platformTransactionManager instanceof JpaTransactionManager jpaTransactionManager) {
            return DomainNames.valueOf(jpaTransactionManager.getPersistenceUnitName());
        }

        return DomainNames.NONE;
    }
}
