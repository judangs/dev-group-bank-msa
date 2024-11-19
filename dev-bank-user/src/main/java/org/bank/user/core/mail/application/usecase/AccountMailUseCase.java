package org.bank.user.core.mail.application.usecase;

import org.bank.user.global.dto.ResponseDto;

public interface AccountMailUseCase {

    ResponseDto confirmAccountEmail(String token);
}
