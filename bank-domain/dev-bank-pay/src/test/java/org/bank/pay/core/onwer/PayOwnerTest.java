package org.bank.pay.core.onwer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PayOwnerTest {

    PayOwner payOwner = new PayOwner();

    @Test
    @DisplayName("[PayOwner] 카드를 추가 합니다.")
    void addPaymentCard() {
        for(Integer cardCount=1; cardCount<=5; cardCount++) {
            payOwner.addPaymentCard(new PaymentCard());
            assertEquals(payOwner.getPaymentCards().size(), cardCount);
        }
    }


    @Test
    @DisplayName("[PayOwner] 카드의 별칭을 업데이트 합니다.")
    void updateCardAlias() {

        UUID cardId = UUID.randomUUID();
        payOwner.addPaymentCard(PaymentCard
                .builder().cardId(cardId).build());

        payOwner.updateCardAlias(cardId, "변경된 카드 별칭");
        PaymentCard card = payOwner.getPaymentCards().get(0);
        assertEquals(card.getCardName(), "변경된 카드 별칭");
    }

    @Test
    void removeRegisteredCard() {

        UUID cardId = UUID.randomUUID();
        payOwner.addPaymentCard(PaymentCard
                .builder().cardId(cardId).build());

        payOwner.removeRegisteredCard(cardId);
        assertTrue(payOwner.getPaymentCards().isEmpty());

    }
}