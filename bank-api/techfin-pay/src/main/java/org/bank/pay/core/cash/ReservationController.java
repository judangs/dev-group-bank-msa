package org.bank.pay.core.cash;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.auth.AuthenticatedUser;
import org.bank.core.dto.response.ResponseDtoV2;
import org.bank.pay.dto.service.request.ReservedChargeRequest;
import org.bank.pay.global.http.HttpResponseEntityStatusConverter;
import org.bank.pay.global.swagger.spec.ReservationChargeCashSwaggerSpec;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/pay/cash")
@RequiredArgsConstructor
public class ReservationController implements ReservationChargeCashSwaggerSpec {

    private final HttpResponseEntityStatusConverter httpResponseEntityStatusConverter;

    private final CashFacade cashFacade;


    @PostMapping("/scheduled")
    public ResponseEntity<? extends ResponseDtoV2> scheduled(@AuthenticatedUser AuthClaims authClaims,
                                                 ReservedChargeRequest request) {
        ResponseDtoV2 response = cashFacade.reserveReCharge(authClaims, request);
        return httpResponseEntityStatusConverter.convert(response);
    }

    @DeleteMapping("/scheduled/{scheduledId}")
    public ResponseEntity<Void> cancelled(@PathVariable UUID scheduledId) {
        cashFacade.cancelReCharge(scheduledId);
        return ResponseEntity.noContent().build();
    }
}
