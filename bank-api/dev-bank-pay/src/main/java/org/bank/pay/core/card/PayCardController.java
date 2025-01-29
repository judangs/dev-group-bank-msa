package org.bank.pay.core.card;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.dto.response.ResponseDto;
import org.bank.pay.dto.service.request.PaymentRequest;
import org.bank.pay.dto.service.request.UpdateCardAliasRequest;
import org.bank.pay.global.http.AuthenticationClaims;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/pay/card")
@RequiredArgsConstructor
public class PayCardController {

    private final PaymentFacade paymentFacade;

    @GetMapping
    public ResponseEntity<? super ResponseDto> card(@AuthenticationClaims AuthClaims authClaims) {
        ResponseDto response = paymentFacade.getRegisteredCards(authClaims);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping
    public ResponseEntity<? super ResponseDto> register(@AuthenticationClaims AuthClaims authClaims,
                                                        PaymentRequest request) {
        ResponseDto response = paymentFacade.registerCard(authClaims, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/alias")
    public ResponseEntity<? super ResponseDto> alias(@AuthenticationClaims AuthClaims authClaims,
                                                     UpdateCardAliasRequest request) {
        ResponseDto response = paymentFacade.updateCardAlias(authClaims, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<? super ResponseDto> delete(@AuthenticationClaims AuthClaims authClaims,
                                                      @PathVariable UUID cardId) {
        paymentFacade.deleteRegisteredCard(authClaims, cardId);
        return ResponseEntity.noContent().build();
    }


}
