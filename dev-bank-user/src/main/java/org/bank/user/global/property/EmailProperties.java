package org.bank.user.global.property;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Setter
@Component
@PropertySource("classpath:mail-dev.properties")
@ConfigurationProperties("email.verification")
public class EmailProperties {

    private String host;
    private String port;
    private String api;
    private String template;

    @Value("${email.temporal.password.template}")
    private String temporalPasswordTemplate;

    @Value("${email.temporal.password.template.param}")
    private String temporalPasswordTemplateParam;

    public String getVerticationMailTemplate(String param) {
        return template.replace("{host}", host)
                .replace("{port}", port)
                .replace("{api}", api)
                .replace("{param}", param);
    }

    public String getTemporalPasswordTemplate(String param) {
        return temporalPasswordTemplate
                .replace(temporalPasswordTemplateParam, param);

    }

}
