package org.bank.pay.global.swagger.data;

import org.bank.pay.dto.service.response.PaymentCardsListResponse;

import java.util.Optional;

public class PayCardApi {

    public static final String REGISTER_REQUEST = """
            {
                    "cardNumber": "4214 1101 2932 4444",
                    "cVC": "012",
                    "passstartWith": "12",
                    "dateOfExpiry": "2030-12-31",
                    "cardName": "자주 사용하는 결제 카드"
            }
            """;


    public static final String ALIAS_REQUEST = """
            {
              "cardName": "카드 별칭 변경"
            }            
            """;

    public static final String CHARGE_CASH_REQUEST = """
            {
              "paymentCardId": "6458cdb8-ede3-44fd-be82-2962c6ddf21b",
              "chargeAmount": 0.00
            }
            """;

    public static final String CONVERSION_CASH_REQUEST = """
            {
              "cardId": "f22df7a0-35fa-4947-8562-3d49c4c99eaf",
              "amount": {
                "balance": 0.00
              }
            }
            """;

    public static final String RESERVATION_CHARGE_CASH_REQUEST = """
            {
              "paymentCardId": "b99b7603-1e95-4458-a08b-eee8bb19f3bd",
              "chargeAmount": 0.00,
              "date": "2025-03-25 12:58:46",
              "balance": 0.00
            }
            """;

    public static final String LIMIT_CASH_REQUEST = """
            {
              "daily": 100000.00,
              "each": 50000.00
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

    public static final String ACCEPT_FAMILY_INVITATION = """
            {
              "familyId": "f0eaf802-0c23-4189-9e39-9074811226b6",
              "decision": "ACCEPT"
            }
            """;


    public static Optional<String> response(Class<?> classType) {
        if(classType.equals(PaymentCardsListResponse.class)) {
            return Optional.of(CARDLIST_RESPONSE);
        }

        return Optional.empty();
    }
}
