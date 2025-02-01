package org.bank.user.core.domain.mail;

import lombok.Data;
import org.bank.user.global.properties.TemplateProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "email.temporal.password")
public class TemporalPasswordTemplateProperties extends TemplateProperties {
    private String template;
    private String param;

    @Override
    public String generateTemplate(String parameter) {
        return template.replace(param, parameter);
    }
}
