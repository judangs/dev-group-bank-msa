package org.bank.gateway.global.auth;

import org.bank.core.domain.DomainNames;
import org.bank.gateway.global.route.ServiceServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
@PropertySource("classpath:/app-services.properties")
@EnableConfigurationProperties(ServiceServerProperties.class)
public class RefreshWebClientConfiguration {


    @Bean
    public WebClient refreshWebClient(WebClient.Builder builder, ServiceServerProperties serviceServerProperties) {
        return builder
                .baseUrl(serviceServerProperties.url(DomainNames.USER))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}