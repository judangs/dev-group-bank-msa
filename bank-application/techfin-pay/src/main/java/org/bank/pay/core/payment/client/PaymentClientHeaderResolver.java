package org.bank.pay.core.payment.client;

import lombok.RequiredArgsConstructor;
import org.springframework.util.MultiValueMap;

@RequiredArgsConstructor
public class PaymentClientHeaderResolver {

    private final PayClientProperties properties;

    public MultiValueMap<String, String> headers() {
        return properties.headers();
    }
}
