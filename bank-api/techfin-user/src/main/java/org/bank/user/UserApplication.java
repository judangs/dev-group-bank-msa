package org.bank.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication(
        scanBasePackages = {
                "org.bank.user",
                "org.bank.store.domain.user",
                "org.bank.store.mysql.core.user"
        },
        exclude = {DataSourceAutoConfiguration.class}
)
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
