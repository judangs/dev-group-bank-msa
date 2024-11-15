package org.bank.user.core.user.presentation.http;

import lombok.RequiredArgsConstructor;
import org.bank.user.core.user.application.usecase.UserUseCase;
import org.bank.user.dto.AccountResponse;
import org.bank.user.dto.ResponseDto;
import org.bank.user.dto.AccountRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserController {

    private final UserUseCase userUseCase;

    @PostMapping("/account-create")
    public ResponseEntity<? super AccountResponse> create(@RequestBody AccountRequest request) {

        ResponseDto responseBody = userUseCase.createAccount(request);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    @GetMapping
    public ResponseEntity<? super AccountResponse> findAccountUserid(
            @RequestParam String username,
            @RequestParam String email
    ) {

        ResponseDto responseBody = userUseCase.findAccountIDs(username, email);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    @GetMapping("/confirm-mail")
    public ResponseEntity<? super AccountResponse> confirmFinderMail(@RequestParam String confirm) {
        ResponseDto responseBody = userUseCase.confirmAccountEmail(confirm);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

}
