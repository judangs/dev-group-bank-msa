package org.bank.user.dto;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
public final class AccountResponse extends ResponseDto {

    private List<String> userid;

}
