package org.bank.user.core.user.application.usecase;

import org.bank.user.global.usecase.UseCase;
import org.bank.user.dto.ResponseDto;
import org.bank.user.dto.AccountRequest;

// 사용자 요구사항
public interface UserUseCase extends UseCase {

    boolean existsAccount(String userid);
    ResponseDto createAccount(AccountRequest request);
    ResponseDto withdrawAccount(String userid);
    ResponseDto editProfile(AccountRequest request, String userid);



    void confirmAccountEmail(String confirmParam);

}
