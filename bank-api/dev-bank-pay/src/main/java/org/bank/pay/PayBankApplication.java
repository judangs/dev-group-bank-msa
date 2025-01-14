package org.bank.pay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.bank.pay")
public class PayBankApplication {

    public static void main(String[] args) {
        SpringApplication.run(PayBankApplication.class, args);
    }
}
