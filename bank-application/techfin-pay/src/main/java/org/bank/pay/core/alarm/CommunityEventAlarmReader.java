package org.bank.pay.core.alarm;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.dto.response.ResponseDtoV2;
import org.bank.pay.core.domain.familly.repository.FamilyEventReader;
import org.bank.pay.core.event.family.FamilyEventEntity;
import org.bank.pay.dto.service.response.CommunityEventAlarmResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommunityEventAlarmReader {

    private final FamilyEventReader familyEventReader;

    public ResponseDtoV2 read(AuthClaims user) {
        List<FamilyEventEntity> events = familyEventReader.findAllEventsByUser(user);
        return new CommunityEventAlarmResponse(events);
    }
}
