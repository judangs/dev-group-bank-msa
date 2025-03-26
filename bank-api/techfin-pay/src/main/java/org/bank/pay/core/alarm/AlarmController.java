package org.bank.pay.core.alarm;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.auth.AuthenticatedUser;
import org.bank.core.dto.response.ResponseDtoV2;
import org.bank.pay.global.http.HttpResponseEntityStatusConverter;
import org.bank.pay.global.swagger.spec.CommunityAlarmSwaggerSpec;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pay/alarm")
@RequiredArgsConstructor
public class AlarmController implements CommunityAlarmSwaggerSpec {

    private final HttpResponseEntityStatusConverter httpResponseEntityStatusConverter;

    private final CommunityEventAlarmReader communityEventAlarmReader;

    @GetMapping
    public ResponseEntity<? extends ResponseDtoV2> list(@AuthenticatedUser AuthClaims user) {
        ResponseDtoV2 response = communityEventAlarmReader.read(user);
        return httpResponseEntityStatusConverter.convert(response);
    }
}
