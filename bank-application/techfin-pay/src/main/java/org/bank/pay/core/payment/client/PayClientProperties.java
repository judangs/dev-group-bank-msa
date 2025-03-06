package org.bank.pay.core.payment.client;

import org.springframework.util.MultiValueMap;

public abstract class PayClientProperties {


    public abstract MultiValueMap<String, String> headers();

}
