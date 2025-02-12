package org.bank.store.route.domain;

import org.bank.core.domain.DomainNames;
import org.bank.store.route.domain.pay.PayDataSourceProperties;
import org.bank.store.route.domain.stub.Repository;
import org.bank.store.route.domain.user.UserDataSourceProperties;
import org.bank.store.source.DataSourceType;
import org.bank.store.source.NamedHikariDataSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Connection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RoutingDataSourceTest.UnitTest.class)
class RoutingDataSourceTest {

    @Configuration
    @ComponentScan(basePackages = "org.bank.store.route.domain")
    static class UnitTest { }

    @Autowired
    private UserDataSourceProperties userDataSourceProperties;
    @Autowired
    private PayDataSourceProperties payDataSourceProperties;

    @Qualifier("userDomainRepository") @Autowired
    private Repository userDomainRepository;

    @Qualifier("payDomainRepository") @Autowired
    private Repository payDomainRepository;


    @Test
    void 유저_도메인의_레포지토리가_다중_데이터_소스를_식별했을_때_유저_데이터베이스_커넥션을_가지는_데이터_소스를_가져옵니다() {
        NamedHikariDataSource namedHikariDataSource = userDomainRepository.routing();
        assertAll(
                () -> assertThat(namedHikariDataSource).isNotNull(),
                () -> assertThat(namedHikariDataSource.getDomain()).isEqualTo(DomainNames.USER),
                () -> assertThat(namedHikariDataSource.getType()).isEqualTo(DataSourceType.READWRITE)
        );
    }

    @Test
    void 페이_도메인의_레포지토리가_다중_데이터_소스를_식별했을_때_페이_데이터베이스_커넥션을_가지는_데이터_소스를_가져옵니다() {
        NamedHikariDataSource namedHikariDataSource = payDomainRepository.routing();
        assertAll(
                () -> assertThat(namedHikariDataSource).isNotNull(),
                () -> assertThat(namedHikariDataSource.getDomain()).isEqualTo(DomainNames.PAY),
                () -> assertThat(namedHikariDataSource.getType()).isEqualTo(DataSourceType.READWRITE)
        );
    }

    @Test
    void 라우팅을_통해_유저_데이터_소스를_가져왔을_때_정상적인_유저_데이터베이스의_커넥션인지_검증합니다() throws Exception{
        Connection connection = userDomainRepository.connection();
        String username = connection.getMetaData().getUserName().split("@")[0];
        assertThat(username).isEqualTo(userDataSourceProperties.getHikari().getUsername());
    }

    @Test
    void 라우팅을_통해_페이_데이터_소스를_가져왔을_때_정상적인_페이_데이터베이스의_커넥션인지_검증합니다() throws Exception {
        Connection connection = payDomainRepository.connection();
        String username = connection.getMetaData().getUserName().split("@")[0];
        assertThat(username).isEqualTo(payDataSourceProperties.getHikari().getUsername());
    }
}