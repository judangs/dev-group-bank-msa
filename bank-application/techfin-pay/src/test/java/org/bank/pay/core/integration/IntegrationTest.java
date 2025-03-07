package org.bank.pay.core.integration;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = IntegrationTest.Integration.class)
@ActiveProfiles({"test"})
public class IntegrationTest {

    @Configuration
    @ComponentScan(basePackages = {
            "org.bank.pay.core",
            "org.bank.store.domain.pay",
            "org.bank.store.mysql.core.pay"
    })
    @Import(IntegrationTestInitializer.class)
    static class Integration { }

    @Autowired
    protected IntegrationTestInitializer integrationTestInitializer;
}
