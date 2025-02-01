package org.bank.user.core.domain.mail;

import org.bank.user.global.properties.TemplateProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class JavaAuthEmailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        return new JavaMailSenderImpl();
    }

    @Configuration
    @PropertySource("classpath:auth-mail-template.properties")
    @EnableConfigurationProperties(AuthTemplateProperties.class)
    static class AuthEmailTemplate {
        @Bean
        public TemplateProperties authEmailTemplateProperties() {
            return new AuthTemplateProperties();
        }
    }

    @Configuration
    @PropertySource("classpath:temporal-password-mail-template.properties")
    @EnableConfigurationProperties(TemporalPasswordTemplateProperties.class)
    static class TemporalEmailTemplate {
        @Bean
        public TemplateProperties temporalPasswordEmailTemplateProperties() {
            return new TemporalPasswordTemplateProperties();
        }
    }
}
