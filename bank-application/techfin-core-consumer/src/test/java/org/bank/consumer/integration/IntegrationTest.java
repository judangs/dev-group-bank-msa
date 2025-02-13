package org.bank.consumer.integration;

import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {
        "org.bank.consumer",
        "org.bank.pay.core.domain",
        "org.bank.user.core.domain",
        "org.bank.store.route"
})
public class IntegrationTest {
}
