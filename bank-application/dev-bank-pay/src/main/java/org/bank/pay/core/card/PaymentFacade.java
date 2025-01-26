package org.bank.pay.core.card;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.dto.response.ResponseDto;
import org.bank.pay.client.PaymentCardClient;
import org.bank.pay.core.onwer.PaymentCard;
import org.bank.pay.core.onwer.PaymentCardService;
import org.bank.pay.dto.service.request.PaymentRequest;
import org.bank.pay.dto.service.request.UpdateCardAliasRequest;
import org.bank.pay.dto.service.response.PaymentCardsListResponse;
import org.bank.pay.global.exception.PaymentException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentFacade {

    private final PaymentCardService paymentCardService;
    private final PaymentCardClient paymentCardClient;


    public ResponseDto registerCard(AuthClaims authClaims, PaymentRequest request) {

        PaymentCard paymentCard = card(request);
        try {
            paymentCard = paymentCardService.registerCard(authClaims, paymentCard);
            boolean isValidCard = paymentCardClient.validateCard(paymentCard);
            if(!isValidCard) {
                throw new PaymentException("부정확한 카드 정보입니다.");
            }


        } catch (PaymentException e) {
            paymentCardService.deleteCard(authClaims, paymentCard.getCardId());
            return ResponseDto.fail(e.getMessage());

        } catch (IllegalArgumentException e) {

            return ResponseDto.fail(e.getMessage());
        }

        return ResponseDto.success("카드 등록에 성공했습니다.");
    }

    public ResponseDto getRegisteredCards(AuthClaims authClaims) {
        List<PaymentCard> paymentCards = paymentCardService.getRegisteredCards(authClaims);
        return new PaymentCardsListResponse(paymentCards);
    }

    public ResponseDto updateCardAlias(AuthClaims authClaims, UpdateCardAliasRequest request) {

        try {
            paymentCardService.updateCardAlias(authClaims, request.getCardId(), request.getCardName());

        } catch (IllegalArgumentException e) {
            return ResponseDto.fail(e.getMessage());
        }

        return ResponseDto.success("카드 이름 변경에 성공했습니다.");
    }

    public ResponseDto deleteRegisteredCard(AuthClaims authClaims, UUID cardId) {
        paymentCardService.deleteCard(authClaims, cardId);
        return ResponseDto.success("카드 삭제에 성공했습니다.");
    }


    private PaymentCard card(PaymentRequest request) {
        return PaymentCard.of(request.getCardName(), request.getCardNumber(), request.getCVC(), request.getPassstartWith(), request.getDateOfExpiry());
    }
}
