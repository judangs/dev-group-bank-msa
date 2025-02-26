package org.bank.pay.core.payment;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:/payment-callback.properties")
@EnableConfigurationProperties(PaymentCallbackProperties.class)
public class PaymentConfiguration {

}
