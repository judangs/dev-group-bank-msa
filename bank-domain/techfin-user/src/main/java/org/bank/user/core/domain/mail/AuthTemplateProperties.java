package org.bank.user.core.domain.mail;

import lombok.Data;
import lombok.Getter;
import org.bank.user.global.properties.TemplateProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("email.verification")
public class AuthTemplateProperties extends TemplateProperties {

    @Getter
    private String host;
    private String port;
    private String api;
    private String template;


    @Override
    public String generateTemplate(String parameter) {
        return template.replace("{host}", host)
                .replace("{port}", port)
                .replace("{api}", api)
                .replace("{param}", parameter);
    }
}