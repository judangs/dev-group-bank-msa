package org.bank.pay.core.history;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.dto.response.ResponseDto;
import org.bank.pay.dto.service.request.HistoryRecordRequest;
import org.bank.pay.global.http.AuthenticationClaims;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pay")
@RequiredArgsConstructor
public class PaymentHistoryController {

    private final HistoryReader historyReader;

    @GetMapping("/history")
    public ResponseEntity<? super ResponseDto> record(@AuthenticationClaims AuthClaims authClaims,
                                                      HistoryRecordRequest request) {
        ResponseDto response = historyReader.getRecords(authClaims, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/hisotry/detail")
    public ResponseEntity<? super ResponseDto> detail(@AuthenticationClaims AuthClaims authClaims,
                                                      HistoryRecordRequest request) {
        ResponseDto response = historyReader.getRecordDetails(authClaims, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
