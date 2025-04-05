package org.bank.store.unit;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
        "org.bank.store.source",
        "org.bank.store.mail"
})
public class RedisMailStorageUnitTest {
}
