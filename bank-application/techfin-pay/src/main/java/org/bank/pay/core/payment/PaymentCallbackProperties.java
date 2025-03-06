package org.bank.pay.core.payment;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.ws.transport.http.HttpTransportConstants;

@Data
@ConfigurationProperties(prefix = "payment.callback")
public class PaymentCallbackProperties {

    private String url;
    private int port;

    public String callback() {
        return UriComponentsBuilder.newInstance()
                .scheme(HttpTransportConstants.HTTPS_URI_SCHEME)
                .host(getUrl()).port(getPort()).path("/")
                .build()
                .toString();
    }
}
