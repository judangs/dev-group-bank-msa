package org.bank.store.mysql.core.pay.config.docker;

import org.bank.store.mysql.global.name.DataSourceBeanNames;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.MySQLContainer;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = PayDataSourceTest.TestConfig.class)
class PayDataSourceTest {

    @Configuration
    @ComponentScan(basePackages ={
            "org.bank.store.mysql.core.pay.config"
    })
    static class TestConfig {

    }

    @Autowired
    private PayDataSource payDataSource;

    @Autowired @Qualifier(DataSourceBeanNames.pay)
    private DataSource dataSource;


    @Test
    @DisplayName("[Bean 등록 시] DataSource가 생성됩니다.")
    public void dataSource_bean_success() {
        assertNotNull(dataSource);
    }

    @Test
    @DisplayName("[Bean 등록 시] docker container가 실행되는지 테스트합니다.")
    public void container_start_success() {
        MySQLContainer<?> container = payDataSource.MySQLContainerConfigurer().getContainer();
        assertTrue(container.isRunning());
    }
}