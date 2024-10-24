package org.bank.user.dto.credential;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import org.bank.user.domain.credential.RoleClassification;

@Getter
public class CredentialSaveRequest {

    @NotNull
    private String userid;
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}$",
            message = "Password must be at least 8 characters long, contain at least one digit, one lowercase letter, one uppercase letter, and one special character."
    )
    @NotNull
    private String password;
    @NotNull
    private String username;

    private RoleClassification.UserRole role = RoleClassification.UserRole.INDIVIDUAL;
}
