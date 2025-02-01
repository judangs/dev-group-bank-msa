package org.bank.user.dto.service.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String userid;
    private String password;
}
