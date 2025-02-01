package org.bank.user.dto.service.response;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.bank.core.dto.response.ResponseDtoV2;

import java.util.List;

@Getter
@SuperBuilder
public final class AccountIdListResponse extends ResponseDtoV2 {
    private List<String> userid;
}
