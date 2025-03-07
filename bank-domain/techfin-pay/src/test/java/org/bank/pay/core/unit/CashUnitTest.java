package org.bank.pay.core.unit;

import org.bank.pay.core.domain.cash.repository.CashReader;
import org.bank.pay.core.domain.cash.repository.CashStore;
import org.bank.pay.core.domain.cash.repository.ReservedCashReader;
import org.bank.pay.core.domain.cash.repository.ReservedCashStore;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
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

    @MockBean
    public CashReader cashReader;
    @MockBean
    public CashStore cashStore;
    @MockBean
    public ReservedCashReader reservedCashReader;
    @MockBean
    public ReservedCashStore reservedCashStore;
}
