package org.bank.user.core.domain.account.service;

import org.bank.core.auth.AuthClaims;
import org.bank.core.dto.response.ResponseCodeV2;
import org.bank.user.core.domain.account.Credential;
import org.bank.user.core.domain.account.Profile;

public interface AccountManagerService {

    boolean existsAccount(String userid);
    ResponseCodeV2 createAccount(Profile profile, Credential credential);
    ResponseCodeV2 editProfile(Profile profile, AuthClaims user);
    ResponseCodeV2 withdrawAccount(AuthClaims user);

}
