package org.bank.user.core.mail.presentation.http;


import lombok.RequiredArgsConstructor;
import org.bank.user.core.mail.application.usecase.AccountMailUseCase;
import org.bank.user.dto.AccountResponse;
import org.bank.user.global.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/mail")
@RequiredArgsConstructor
public class MailController {

    private final AccountMailUseCase mailUseCase;

    @GetMapping("/confirm-mail")
    public ResponseEntity<? super AccountResponse> confirmFinderMail(@RequestParam String confirm) {
        ResponseDto responseBody = mailUseCase.confirmAccountEmail(confirm);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
