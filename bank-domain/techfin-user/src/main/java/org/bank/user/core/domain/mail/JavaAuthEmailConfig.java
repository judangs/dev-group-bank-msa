package org.bank.user.core.domain.mail;

import org.bank.user.global.properties.TemplateProperties;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@PropertySource("classpath:/java-mail-sender.properties")
@EnableConfigurationProperties(MailProperties.class)
public class JavaAuthEmailConfig {

    @Bean
    public JavaMailSender javaMailSender(MailProperties mailProperties) {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        Properties properties = javaMailSender.getJavaMailProperties();
        properties.putAll(mailProperties.getProperties());

        javaMailSender.setHost(mailProperties.getHost());
        javaMailSender.setPort(mailProperties.getPort());
        javaMailSender.setUsername(mailProperties.getUsername());
        javaMailSender.setPassword(mailProperties.getPassword());

        return javaMailSender;
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
