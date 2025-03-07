package org.bank.pay.core.unit;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@TestConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CashUnitTest.Unit.class)
public class CashUnitTest {


    @Configuration
    @ComponentScan(basePackages = "org.bank.pay.core.domain.cash")
    public static class Unit { }
}
