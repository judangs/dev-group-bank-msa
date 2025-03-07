package org.bank.pay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(
        scanBasePackages = {
                "org.bank.pay",
                "org.bank.store.domain.pay",
                "org.bank.store.mysql.core.pay"
        },
        exclude = {
                DataSourceAutoConfiguration.class
        }
)
public class PayApplication {

    public static void main(String[] args) {
        SpringApplication.run(PayApplication.class, args);
    }
}
