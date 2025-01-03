package org.bank.pay.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bank.core.dto.response.ResponseDto;
import org.bank.pay.core.onwer.PaymentCard;

import java.util.List;

@Data
@AllArgsConstructor
public class PaymentCardsListResponse extends ResponseDto {

    List<PaymentCard> cards;
}
