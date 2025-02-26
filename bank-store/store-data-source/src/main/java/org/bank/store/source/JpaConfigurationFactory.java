package org.bank.store.source;

import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

public abstract class JpaConfigurationFactory {

    @Configuration
    @PropertySource("classpath:jpa-hibernate.properties")
    @EnableConfigurationProperties(JpaProperties.class)
    protected static class JpaManagerConfiguration {
        @Bean
        public JpaProperties jpaProperties() {
            return new JpaProperties();
        }


        @Bean
        public EntityManagerFactoryBuilder entityManagerFactoryBuilder() {
            return new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(),
                    jpaProperties().getProperties(), null);
        }
    }
}
