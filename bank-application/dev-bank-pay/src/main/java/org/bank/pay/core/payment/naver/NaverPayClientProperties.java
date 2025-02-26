package org.bank.pay.core.payment.naver;

import lombok.Data;
import org.bank.pay.core.payment.client.PayClientProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.ws.transport.http.HttpTransportConstants;

@Data
@ConfigurationProperties(prefix = "naver.pay")
public class NaverPayClientProperties extends PayClientProperties {

    private PaymentUrl payment;
    private PaymentUrl window;
    private PaymentUrl apply;
    private PaymentUrl history;
    private PaymentSecret client;
    private PaymentSecret chain;

    public final String X_NAVER_CLIENT_ID = "X-Naver-Client-Id";
    public final String X_NAVER_CLIENT_SECRET = "X-Naver-Client-Secret";
    public final String X_NAVERPAY_CHAIN_ID = "X-NaverPay-Chain-Id";

    @Override
    public MultiValueMap<String, String> headers() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(X_NAVER_CLIENT_ID, getClient().getId());
        headers.add(X_NAVER_CLIENT_SECRET, getClient().getSecret());
        headers.add(X_NAVERPAY_CHAIN_ID, getChain().getId());
        return headers;
    }

    @Data
    public static class PaymentUrl {
        private String url;
        private String api;
    }

    @Data
    public static class PaymentSecret {
        private String id;
        private String secret;
    }

    public String payment() {
        return url(getPayment().getUrl(), getPayment().getApi());
    }

    public String window(String reserve) {
        return url(getWindow().getUrl(), getWindow().getApi() + reserve);
    }

    public String apply() {
        return url(getApply().getUrl(), getApply().getApi());
    }

    public String history(String id) {
        return url(getHistory().getUrl(), getHistory().getApi() + id);
    }

    private String url(String host, String api) {
        return UriComponentsBuilder.newInstance()
                .scheme(HttpTransportConstants.HTTPS_URI_SCHEME)
                .host(host).path(api)
                .build()
                .toString();
    }
}
