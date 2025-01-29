package org.bank.pay.core.cash;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.dto.response.ResponseDto;
import org.bank.pay.dto.service.request.CashLimitRequest;
import org.bank.pay.dto.service.request.ChargeRequest;
import org.bank.pay.global.http.AuthenticationClaims;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pay/cash")
@RequiredArgsConstructor
public class BalanceController {

    private final CashFacade cashFacade;
    private final CashOptionFacade cashOptionFacade;

    @GetMapping
    public ResponseEntity<? super ResponseDto> cash(@AuthenticationClaims AuthClaims authClaims) {
        ResponseDto response = cashOptionFacade.checkBalance(authClaims);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/charge/immediate")
    public ResponseEntity<? super ResponseDto> immediate(@AuthenticationClaims AuthClaims authClaims,
                                                 ChargeRequest request) {
        ResponseDto response = cashFacade.chargeCash(authClaims, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/charge/limit")
    public ResponseEntity<? super ResponseDto> limit(@AuthenticationClaims AuthClaims authClaims,
                                             CashLimitRequest request) {
        ResponseDto response = cashOptionFacade.applyCashLimit(authClaims, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
