package org.bank.pay.core.card;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.dto.response.ResponseDtoV2;
import org.bank.pay.core.domain.card.PaymentCard;
import org.bank.pay.core.domain.owner.service.PayCardService;
import org.bank.pay.dto.service.request.CardPaymentRequest;
import org.bank.pay.dto.service.response.PaymentCardsListResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CardFacade {

    private final PayCardService payCardService;


    public ResponseDtoV2 registerCard(AuthClaims user, CardPaymentRequest request) {
        payCardService.register(user, card(request));
        return ResponseDtoV2.success("결제 카드 등록에 성공했습니다.");
    }

    public ResponseDtoV2 getRegisteredCards(AuthClaims user) {
        List<PaymentCard> paymentCards = payCardService.gets(user);
        return new PaymentCardsListResponse(paymentCards);
    }

    public ResponseDtoV2 updateCardAlias(AuthClaims user, UUID cardId, String cardName) {
        payCardService.updateAlias(user, cardId, cardName);
        return ResponseDtoV2.success("카드 이름 변경에 성공했습니다.");
    }

    public ResponseDtoV2 deleteRegisteredCard(AuthClaims user, UUID cardId) {
        payCardService.remove(user, cardId);
        return ResponseDtoV2.success("카드 삭제에 성공했습니다.");
    }


    private PaymentCard card(CardPaymentRequest request) {
        return PaymentCard.of(request.getCardName(), request.getCardNumber(), request.getCVC(), request.getPassstartWith(), request.getDateOfExpiry());
    }
}
