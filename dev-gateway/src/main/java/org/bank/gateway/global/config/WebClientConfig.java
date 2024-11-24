package org.bank.gateway.global.config;

import org.bank.common.constants.auth.ProtocolConstants;
import org.bank.gateway.global.properties.AuthWebClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class WebClientConfig {


    @Bean
    public AuthWebClientProperties authWebClientProperties() {
        return new AuthWebClientProperties();
    }

    @Bean
    public WebClient authServiceWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl(ProtocolConstants.HTTP + authWebClientProperties().getAuthBaseUri())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}