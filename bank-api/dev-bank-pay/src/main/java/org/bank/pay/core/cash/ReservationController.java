package org.bank.pay.core.cash;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.dto.response.ResponseDto;
import org.bank.pay.dto.request.ReservedChargeRequest;
import org.bank.pay.global.http.AuthenticationClaims;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/pay/cash")
@RequiredArgsConstructor
public class ReservationController {

    private final CashFacade cashFacade;


    @PostMapping("/scheduled")
    public ResponseEntity<? super ResponseDto> scheduled(@AuthenticationClaims AuthClaims authClaims,
                                                 ReservedChargeRequest request) {
        ResponseDto response = cashFacade.reserveReCharge(authClaims, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/scheduled/{scheduledId}")
    public ResponseEntity<Void> cancelled(@PathVariable UUID scheduledId) {
        cashFacade.cancelReCharge(scheduledId);
        return ResponseEntity.noContent().build();
    }
}
