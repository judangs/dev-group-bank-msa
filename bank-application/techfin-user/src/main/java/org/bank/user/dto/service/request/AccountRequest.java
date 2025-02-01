package org.bank.user.dto.service.request;

import lombok.Data;
import org.bank.core.common.Address;
import org.bank.core.common.Role;

@Data
public class AccountRequest {

    private ProfileRequest profile;
    private CredentialRequest credential;

    @Data
    public class ProfileRequest {
        private String name;
        private String residentNumber;
        private Integer age;
        private Address address;
        private String email;
        private String phone;
    }

    @Data
    public class CredentialRequest {
        private String userid;
        private String password;
        private String username;

        private Role.UserRole role = Role.UserRole.INDIVIDUAL;
    }
}
