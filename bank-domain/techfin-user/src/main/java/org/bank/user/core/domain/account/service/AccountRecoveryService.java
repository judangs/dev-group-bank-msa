package org.bank.user.core.domain.account.service;

import org.bank.core.dto.response.ResponseCodeV2;

public interface AccountRecoveryService {

    ResponseCodeV2 findAccountIDs(String username, String email);
    ResponseCodeV2 findAccountPassword(String userid, String email);
}
