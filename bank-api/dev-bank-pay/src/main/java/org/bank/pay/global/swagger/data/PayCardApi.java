package org.bank.pay.global.swagger.data;

import org.bank.pay.dto.service.response.PaymentCardsListResponse;

import java.util.Optional;

public class PayCardApi {

    public static final String REGISTER_REQUEST = """
            {
                    "cardNumber": "cardNumber_feb8aa55de71",
                    "cVC": "cVC_b26967838b87",
                    "passstartWith": "passstartWith_0dbeee28283f",
                    "dateOfExpiry": "2025-02-25",
                    "cardName": "cardName_a959e0d8813e"
            }
            """;


    public static final String ALIAS_REQUEST = """
            {
              "cardName": "cardName_0c24e0d404da"
            }            
            """;

    public static final String CARDLIST_RESPONSE = """
            {
              "cards": [
                {
                  "cardId": "7b2ffe69-c662-43bc-9f71-144d7408952d"
                }
              ],
              "code": "SUCCESS",
              "message": "message_0bff35322733",
              "completed_at": "2025-02-25 18:57:46"
            }
            """;


    public static Optional<String> response(Class<?> classType) {
        if(classType.equals(PaymentCardsListResponse.class)) {
            return Optional.of(CARDLIST_RESPONSE);
        }

        return Optional.empty();
    }
}
