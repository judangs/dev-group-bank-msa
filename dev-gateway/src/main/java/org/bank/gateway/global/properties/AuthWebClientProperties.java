package org.bank.gateway.global.properties;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

@Getter
public class AuthWebClientProperties {

    @Value("${service.user.ip}")
    private String ip;

    @Value("${service.user.port}")
    private String port;


    public String getAuthBaseUri() {
        return new StringBuilder()
                .append(ip)
                .append(":")
                .append(port)
                .toString();
    }
}
