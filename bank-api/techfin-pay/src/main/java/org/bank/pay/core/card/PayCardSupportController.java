package org.bank.pay.core.card;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.auth.AuthenticatedUser;
import org.bank.core.dto.response.ResponseDtoV2;
import org.bank.pay.dto.service.request.PaymentCardRegisterRequest;
import org.bank.pay.dto.service.request.UpdateCardAliasRequest;
import org.bank.pay.global.http.HttpResponseEntityStatusConverter;
import org.bank.pay.global.swagger.spec.PayCardSupportSwaggerSpec;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/pay/card")
@RequiredArgsConstructor
public class PayCardSupportController implements PayCardSupportSwaggerSpec {

    private final HttpResponseEntityStatusConverter httpResponseEntityStatusConverter;

    private final CardFacade cardFacade;

    @GetMapping
    public ResponseEntity<? extends ResponseDtoV2> card(@AuthenticatedUser AuthClaims user) {
        ResponseDtoV2 response = cardFacade.getRegisteredCards(user);
        return httpResponseEntityStatusConverter.convert(response);
    }

    @PostMapping
    public ResponseEntity<? extends ResponseDtoV2> register(@AuthenticatedUser AuthClaims user,
                                                        @RequestBody PaymentCardRegisterRequest request) {
        ResponseDtoV2 response = cardFacade.registerCard(user, request);
        return httpResponseEntityStatusConverter.convert(response);
    }

    @PutMapping("{cardId}/alias")
    public ResponseEntity<? extends ResponseDtoV2> alias(@AuthenticatedUser AuthClaims user,
                                                     @PathVariable UUID cardId,
                                                     @RequestBody UpdateCardAliasRequest request) {
        ResponseDtoV2 response = cardFacade.updateCardAlias(user, cardId, request.getCardName());
        return httpResponseEntityStatusConverter.convert(response);
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<? extends ResponseDtoV2> delete(@AuthenticatedUser AuthClaims user,
                                                      @PathVariable UUID cardId) {
        cardFacade.deleteRegisteredCard(user, cardId);
        return ResponseEntity.noContent().build();
    }


}
