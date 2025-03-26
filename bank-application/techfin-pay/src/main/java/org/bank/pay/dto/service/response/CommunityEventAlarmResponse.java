package org.bank.pay.dto.service.response;

import lombok.Data;
import lombok.Setter;
import org.bank.core.dto.response.ResponseDtoV2;
import org.bank.pay.core.event.family.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class CommunityEventAlarmResponse extends ResponseDtoV2 {


    private List<CommunityEvent> alarms = new ArrayList<>();

    @Data
    @Setter
    public static class CommunityEvent {
        private UUID familyId;
        private FamilyEventStatus status;
        private LocalDateTime expiryDate;
        private List<PaymentProduct> products;

        public CommunityEvent(UUID familyId, FamilyEventStatus status, LocalDateTime expiryDate) {
            this.familyId = familyId;
            this.status = status;
            this.expiryDate = expiryDate;
        }

        public CommunityEvent(UUID familyId, FamilyEventStatus status, List<PaymentProduct> products) {
            this.familyId = familyId;
            this.status = status;
            this.products = products;
        }

        public static CommunityEvent of(FamilyEventEntity event) {
            if(event instanceof FamilyInvitation invitation) {
                return new CommunityEvent(invitation.getFamilyId(), invitation.getStatus(), invitation.getExpiryDate());
            }
            if(event instanceof FamilyPayment payment) {
                return new CommunityEvent(payment.getFamilyId(), payment.getStatus(), payment.getProducts());
            }

            return null;
        }
    }

    public CommunityEventAlarmResponse(List<FamilyEventEntity> events) {
        events.forEach(event -> this.alarms.add(CommunityEvent.of(event)));
    }


}
