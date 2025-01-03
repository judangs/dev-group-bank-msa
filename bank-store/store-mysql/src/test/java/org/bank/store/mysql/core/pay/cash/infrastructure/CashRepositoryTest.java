package org.bank.store.mysql.core.pay.cash.infrastructure;

import org.bank.core.cash.Money;
import org.bank.pay.core.cash.Cash;
import org.bank.pay.core.onwer.OwnerClaims;
import org.bank.pay.core.onwer.PayOwner;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CashRepositoryTest.TestConfig.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CashRepositoryTest {

    @Configuration
    @ComponentScan(basePackages ={
            "org.bank.store.mysql.core.pay.config",
            "org.bank.store.mysql.core.pay.cash"
    })
    static class TestConfig {

    }

    private static OwnerClaims claims = new OwnerClaims("fixture", "name", "fixture@bank.com");
    private static PayOwner owner = new PayOwner(claims);
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
    public void findByCash_success() {

        Optional<Cash> optionalCash = cashQueryRepository.findByCash(cash);
        assertTrue(optionalCash.isPresent());
    }

    @Test
    @DisplayName("[Cash 조회] 사용자 정보에 해당하는 cash 엔티티를 데이터베이스에서 조회할 수 있다.")
    public void findByClaims_success() {

        Cash cash = cashQueryRepository.findByOwnerClaims(claims);
        assertEquals(claims, cash.getPayOwner().getClaims());

    }

    @Test
    @DisplayName("[Cash 조회] 사용자 정보에 해당 하는 Cash 엔티티의 잔고를 조회할 수 있다.")
    public void findCreditByClaims_success() {

        Money credit = cashQueryRepository.findBalanceByOwnerClaims(claims);
        assertEquals(0, credit.getBalance().compareTo(BigDecimal.ZERO));

    }
  
}