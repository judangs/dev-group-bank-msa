package org.bank.store.mysql.core.pay.cash.infrastructure;

import org.bank.pay.core.cash.Cash;
import org.bank.pay.core.onwer.OwnerClaims;
import org.bank.pay.core.onwer.PayOwner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CashRepositoryTest.TestConfig.class)
class CashRepositoryTest {

    @Configuration
    @ComponentScan(basePackages ={
            "org.bank.store.mysql.core.pay.config",
            "org.bank.store.mysql.core.pay.cash"
    })
    static class TestConfig {

    }

    private static PayOwner owner = new PayOwner(new OwnerClaims("fixture", "name", "fixture@bank.com"));
    private static Cash cash = new Cash(owner);

    @Autowired @Qualifier("cashCommandRepository")
    private CashComandRepository cashCommandRepository;

    @Autowired @Qualifier("cashQueryRepository")
    private CashQueryRepository cashQueryRepository;


    @Test
    @DisplayName("[Cash 저장] cash 엔티티를 데이터베이스에 저장할 수 있다.")
    @Order(1)
    public void save_success() {
        Assertions.assertDoesNotThrow(() -> cashCommandRepository.save(cash));
    }

    @Test
    @DisplayName("[Cash 조회] cash 엔티티를 데이터베이스에서 조회할 수 있다.")
    public void findbyCash_success() {

        Optional<Cash> optionalCash = cashQueryRepository.findByCash(cash);
        assertTrue(optionalCash.isPresent());
    }
  
}