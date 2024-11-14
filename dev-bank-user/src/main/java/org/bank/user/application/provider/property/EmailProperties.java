package org.bank.user.application.provider.property;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:template.properties")
@ConfigurationProperties("email.verification")
@Setter
public class EmailProperties {



    private String host;
    private String port;
    private String api;
    private String template;

    public String getTemplate(String param) {
        return template.replace("{host}", host)
                .replace("{port}", port)
                .replace("{api}", api)
                .replace("{param}", param);
    }

}
