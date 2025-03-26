package org.bank.pay.core.payment;

import lombok.Data;
import org.bank.core.common.Base64Converter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.ws.transport.http.HttpTransportConstants;

import java.util.UUID;

@Data
@ConfigurationProperties(prefix = "payment.callback")
public class PaymentCallbackProperties {

    private String url;
    private String api;
    private int port;

    public String callback() {
        return UriComponentsBuilder.newInstance()
                .scheme(HttpTransportConstants.HTTP_URI_SCHEME)
                .host(getUrl()).port(getPort()).path(getApi())
                .build()
                .toString();
    }

    public String callback(String user, UUID cardId) {

        String encoding = Base64Converter.encode(user) + '/';

        return UriComponentsBuilder.newInstance()
                .scheme(HttpTransportConstants.HTTP_URI_SCHEME)
                .host(getUrl()).port(getPort()).path(getApi() + encoding + cardId.toString())
                .build()
                .toString();
    }

}
