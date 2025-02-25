package org.bank.pay.dto.service.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bank.core.dto.response.ResponseDtoV2;
import org.bank.pay.core.domain.owner.PaymentCard;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class PaymentCardsListResponse extends ResponseDtoV2 {

    List<CardInfo> cards;

    public PaymentCardsListResponse(List<PaymentCard> cards) {
        this.cards = cards.stream().map(CardInfo::new).toList();
    }

    public class CardInfo {
        private UUID cardId;

        CardInfo(PaymentCard card) {
            this.cardId = card.getCardId();
        }
    }
}
