package org.bank.user.dto.credential;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginActionRequest {
    private String userid;
    private String password;
}
