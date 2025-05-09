package org.bank.store.mysql.core.pay.unit;

import org.bank.store.mysql.core.pay.unit.init.TestInitializer;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan("org.bank.store.mysql.core.pay.family")
@EnableJpaRepositories("org.bank.store.mysql.core.pay.family")
@EntityScan("org.bank.pay.core")
@Import(value = {TestInitializer.class, CashRepositoryUnitTest.class})
@DataJpaTest
public class FamilyRepositoryUnitTest extends H2UnitTest {
}
