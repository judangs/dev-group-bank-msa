package org.bank.user.presentation.http;

import lombok.RequiredArgsConstructor;
import org.bank.user.application.usecase.UserUseCase;
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

        ResponseDto actions = userUseCase.createAccount(request);
        return ResponseEntity.status(HttpStatus.OK).body(actions);
    }

    @GetMapping("/confirm-mail")
    public void confirm(@RequestParam String confirm) {
        userUseCase.confirmAccountEmail(confirm);
    }

}
