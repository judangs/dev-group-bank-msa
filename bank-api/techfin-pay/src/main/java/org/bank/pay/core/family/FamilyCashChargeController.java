package org.bank.pay.core.family;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.auth.AuthenticatedUser;
import org.bank.core.dto.response.ResponseDtoV2;
import org.bank.pay.dto.service.request.CashConversionRequest;
import org.bank.pay.global.http.HttpResponseEntityStatusConverter;
import org.bank.pay.global.swagger.spec.CashConversionSwaggerSpec;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/pay/family")
@RequiredArgsConstructor
public class FamilyCashChargeController implements CashConversionSwaggerSpec {

    private final HttpResponseEntityStatusConverter httpResponseEntityStatusConverter;

    private final FamilyEventFacade familyEventFacade;

    @PostMapping("{familyId}/cash")
    public ResponseEntity<? extends ResponseDtoV2> conversion(@AuthenticatedUser AuthClaims user,
                                                              @PathVariable UUID familyId,
                                                              @RequestBody CashConversionRequest cashConversionRequest) {

        ResponseDtoV2 response = familyEventFacade.convertPersonalCashToFamilyCash(user,familyId, cashConversionRequest.cardId(), cashConversionRequest.amount());
        return httpResponseEntityStatusConverter.convert(response);
    }
}
