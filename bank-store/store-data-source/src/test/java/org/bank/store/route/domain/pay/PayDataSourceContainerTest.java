package org.bank.store.route.domain.pay;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.sql.SQLException;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = PayDataSourceContainerTest.UnitTest.class)
class PayDataSourceContainerTest {

    @Configuration
    @ComponentScan(basePackages = "org.bank.store.route.domain.pay")
    static class UnitTest { }

    @Autowired
    private DataSource dataSource;

    @Test
    void 유저_도메인에서_사용할_데이터_소스의_커넥션을_검증합니다() throws SQLException {
        Assertions.assertThat(dataSource).isNotNull();
        Assertions.assertThat(dataSource.getConnection().getMetaData()).isNotNull();
    }

}