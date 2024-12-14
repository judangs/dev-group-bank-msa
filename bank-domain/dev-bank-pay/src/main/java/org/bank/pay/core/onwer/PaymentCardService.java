package org.bank.pay.core.onwer;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.bank.pay.core.onwer.repository.PayOwnerReader;
import org.bank.pay.core.onwer.repository.PayOwnerStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentCardService {

    private final PaymentCardClient paymentCardClient;

    private final PayOwnerReader payOwnerReader;
    private final PayOwnerStore payOwnerStore;

    @Transactional
    public void registerCard(OwnerClaims claims, PaymentCard paymentCard) throws IllegalArgumentException {

        Optional<PayOwner> owner = payOwnerReader.findByUserClaims(claims);
        if(owner.isEmpty()) {
            owner = createPayOwnerFromClaims(claims);
        }

        PayOwner payOwner = owner.get();
        Optional<PaymentCard> ownerCard =  payOwner.getPaymentCards().stream()
                .filter(card -> card.getCardNumber().equals(paymentCard.getCardName()))
                .findFirst();

        if(ownerCard.isPresent()) {
            throw new IllegalArgumentException("이미 등록된 카드입니다.");
        }

        boolean isValidCard = paymentCardClient.validateCard(paymentCard);
        if (!isValidCard) {
            throw new IllegalArgumentException("부정확한 카드 정보입니다.");
        }

        payOwner.addPaymentCard(paymentCard);
    }



    @Transactional(readOnly = true)
    public List<PaymentCard> getRegisteredCards(OwnerClaims claims) {

        PayOwner payOwner = payOwnerReader.findByUserClaims(claims).orElseThrow(() -> new EntityNotFoundException("사용자가 존재하지 않습니다."));

        return payOwnerReader.findAllPaymentCardsByOwner(payOwner);
    }

    @Transactional
    public void updateCardAlias(OwnerClaims claims, UUID cardId, String newCardName) throws IllegalArgumentException, EntityNotFoundException {

        try {
            PayOwner payOwner = payOwnerReader.findByUserClaims(claims).orElseThrow(() -> new EntityNotFoundException("사용자가 존재하지 않습니다."));
            payOwner.updateCardAlias(cardId, newCardName);
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException("등록되지 않은 카드 정보입니다.");
        }
    }

    @Transactional
    public void deleteCard(OwnerClaims claims, UUID cardId) {
        PayOwner payOwner = payOwnerReader.findByUserClaims(claims).orElseThrow(() -> new EntityNotFoundException("사용자가 존재하지 않습니다."));
        payOwner.removeRegisteredCard(cardId);
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected Optional<PayOwner> createPayOwnerFromClaims(OwnerClaims claims) {
        PayOwner payOwner = new PayOwner(claims);
        payOwnerStore.save(payOwner);

        return Optional.of(payOwner);
    }
}