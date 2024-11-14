package org.bank.user.dto.profile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.bank.user.domain.profile.Address;

@Getter
@Builder
public class ProfileSaveRequest {


    @NotNull
    private String name;
    @NotNull
    private String residentNumber;
    private Integer age;


    @NotNull
    private Address address;

    @NotNull
    @Email
    private String email;

    @NotNull
    private String phone;
}
