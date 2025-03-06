package org.bank.pay.core.payment.naver;

import org.bank.pay.core.payment.client.PaymentClientHeaderResolver;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestClient;

@Configuration
@PropertySource("classpath:/naver-pay.properties")
@EnableConfigurationProperties(NaverPayClientProperties.class)
public class NaverPayConfiguration {

    @Bean
    public RestClient naverPayRestClient(NaverPayClientProperties naverPayClientProperties) {

        PaymentClientHeaderResolver paymentClientHeaderResolver = new PaymentClientHeaderResolver(naverPayClientProperties);

        return RestClient.builder()
                .baseUrl(naverPayClientProperties.payment())
                .defaultHeaders(httpHeaders -> httpHeaders.addAll(paymentClientHeaderResolver.headers()))
                .build();
    }
}
