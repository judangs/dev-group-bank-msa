package org.bank.pay.core.unit;

import org.bank.pay.core.domain.owner.repository.PayOwnerReader;
import org.bank.pay.core.domain.owner.repository.PayOwnerStore;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CardUnitTest.Unit.class)
public class CardUnitTest {

    @Configuration
    @ComponentScan(basePackages = "org.bank.pay.core.domain.owner")
    static class Unit {}

    @MockBean
    protected PayOwnerReader payOwnerReader;
    @MockBean
    protected PayOwnerStore payOwnerStore;

}
