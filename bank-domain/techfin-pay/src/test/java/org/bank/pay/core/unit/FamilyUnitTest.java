package org.bank.pay.core.unit;

import org.bank.pay.core.domain.familly.repository.FamilyReader;
import org.bank.pay.core.domain.familly.repository.FamilyStore;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = FamilyUnitTest.Unit.class)
public class FamilyUnitTest {

    @Configuration
    @ComponentScan(basePackages = "org.bank.pay.core.domain.familly")
    static class Unit { }


    @MockBean
    protected FamilyReader familyReader;
    @MockBean
    protected FamilyStore familyStore;
}
