package org.bank.store.mysql.core.pay.config.docker;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(SpringExtension.class)
@TestPropertySource("classpath:data-source.properties")
@EnableConfigurationProperties(PayDataSourceProperties.class)
class PayDataSourcePropertiesTest {

    @Autowired
    private PayDataSourceProperties payDataSourceProperties;

    @Test
    public void property_load_success() {

        assertNotNull(payDataSourceProperties);
        assertFalse(payDataSourceProperties.getSources().isEmpty());
    }
}