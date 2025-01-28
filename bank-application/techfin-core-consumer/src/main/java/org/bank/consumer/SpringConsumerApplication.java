package org.bank.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.bank")
public class SpringConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringConsumerApplication.class, args);
    }
}
