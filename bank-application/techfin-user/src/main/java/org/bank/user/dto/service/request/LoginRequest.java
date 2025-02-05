package org.bank.user.dto.service.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequest {
    private String userid;
    private String password;
}
