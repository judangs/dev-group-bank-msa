package org.bank.pay.core.cash;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.auth.AuthenticatedUser;
import org.bank.core.dto.response.ResponseDtoV2;
import org.bank.pay.dto.service.request.CashLimitRequest;
import org.bank.pay.dto.service.request.ChargeRequest;
import org.bank.pay.global.http.HttpResponseEntityStatusConverter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/pay/cash")
@RequiredArgsConstructor
public class ChargeController {

    private final HttpResponseEntityStatusConverter httpResponseEntityStatusConverter;

    private final CashFacade cashFacade;
    private final CashOptionFacade cashOptionFacade;

    @GetMapping("/{cardId}")
    public ResponseEntity<? extends ResponseDtoV2> cash(@AuthenticatedUser AuthClaims user,
                                                    @PathVariable UUID cardId) {

        ResponseDtoV2 response = cashOptionFacade.checkBalance(user, cardId);
        return httpResponseEntityStatusConverter.convert(response);
    }

    @PostMapping("/charge/immediate")
    public ResponseEntity<? extends ResponseDtoV2> immediate(@AuthenticatedUser AuthClaims authClaims,
                                                 ChargeRequest request
    ) {

        ResponseDtoV2 response = cashFacade.purchase(authClaims, request);
        return httpResponseEntityStatusConverter.convert(response);
    }

    @PutMapping("{cardId}/charge/limit")
    public ResponseEntity<? extends ResponseDtoV2> limit(@AuthenticatedUser AuthClaims authClaims,
                                             @PathVariable UUID cardId,
                                             CashLimitRequest request) {
        ResponseDtoV2 response = cashOptionFacade.applyLimit(authClaims, cardId, request);
        return httpResponseEntityStatusConverter.convert(response);
    }
}
