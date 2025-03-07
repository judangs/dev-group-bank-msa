package org.bank.pay.core.callback;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.auth.AuthenticatedUser;
import org.bank.core.dto.response.ResponseDtoV2;
import org.bank.pay.core.payment.PaymentService;
import org.bank.pay.global.http.HttpResponseEntityStatusConverter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
@RequestMapping("/pay/payments/callback")
@RequiredArgsConstructor
public class PaymentController {

    private final HttpResponseEntityStatusConverter httpResponseEntityStatusConverter;
    private final PaymentService paymentService;

    @GetMapping("/{reserveId}")
    public ResponseEntity<? extends ResponseDtoV2> callback(@AuthenticatedUser AuthClaims user,
                                   @PathVariable UUID reserveId,
                                   @RequestParam String resultCode,
                                   @RequestParam String paymentId
    ) {

        if(resultCode.equals("Success")) {
            ResponseDtoV2 response = paymentService.process(user, paymentId);
            return httpResponseEntityStatusConverter.convert(response);
        }

        return httpResponseEntityStatusConverter.convert(ResponseDtoV2.fail("결제에 실패했습니다. 결제를 다시 시도해주세요."));
    }
}
