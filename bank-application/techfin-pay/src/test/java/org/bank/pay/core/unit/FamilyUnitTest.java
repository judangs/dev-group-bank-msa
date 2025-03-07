package org.bank.pay.core.unit;

import org.junit.jupiter.api.extension.ExtendWith;
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
}
