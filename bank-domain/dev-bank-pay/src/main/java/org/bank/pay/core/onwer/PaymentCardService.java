package org.bank.pay.core.onwer;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.pay.core.onwer.repository.PayOwnerReader;
import org.bank.pay.core.onwer.repository.PayOwnerStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentCardService {

    private final PayOwnerReader payOwnerReader;
    private final PayOwnerStore payOwnerStore;

    @Transactional
    public PaymentCard registerCard(AuthClaims claims, PaymentCard paymentCard) throws IllegalArgumentException {

        OwnerClaims ownerClaims = (OwnerClaims) claims;
        Optional<PayOwner> owner = payOwnerReader.findByUserClaims(ownerClaims);
        if(owner.isEmpty()) {
            owner = createPayOwnerFromClaims(ownerClaims);
        }

        PayOwner payOwner = owner.get();
        Optional<PaymentCard> ownerCard =  payOwner.getPaymentCards().stream()
                .filter(card -> card.getCardNumber().equals(paymentCard.getCardName()))
                .findFirst();

        if(ownerCard.isPresent()) {
            throw new IllegalArgumentException("이미 등록된 카드입니다.");
        }

        payOwner.addPaymentCard(paymentCard);
        return paymentCard;
    }

    @Transactional(readOnly = true)
    public List<PaymentCard> getRegisteredCards(AuthClaims claims) {

        OwnerClaims ownerClaims = (OwnerClaims) claims;
        PayOwner payOwner = payOwnerReader.findByUserClaims(ownerClaims).orElseThrow(() -> new EntityNotFoundException("사용자가 존재하지 않습니다."));

        return payOwnerReader.findAllPaymentCardsByOwner(payOwner);
    }

    @Transactional(readOnly = true)
    public PaymentCard getRegisteredCard(AuthClaims claims, UUID paymentCardId) {

        OwnerClaims ownerClaims = (OwnerClaims) claims;
        PayOwner payOwner = payOwnerReader.findByUserClaims(ownerClaims).orElseThrow(() -> new EntityNotFoundException("사용자가 존재하지 않습니다."));

        List<PaymentCard> paymentCards =  payOwnerReader.findAllPaymentCardsByOwner(payOwner);
        PaymentCard paymentCard = paymentCards.stream().filter(card -> card.getCardId().equals(paymentCardId)).findFirst().orElseThrow();
        return paymentCard;
    }

    @Transactional
    public void updateCardAlias(AuthClaims claims, UUID cardId, String newCardName) throws IllegalArgumentException {
        OwnerClaims ownerClaims = (OwnerClaims) claims;
        try {
            PayOwner payOwner = payOwnerReader.findByUserClaims(ownerClaims).orElseThrow(() -> new EntityNotFoundException("사용자가 존재하지 않습니다."));
            payOwner.updateCardAlias(cardId, newCardName);
        } catch (EntityNotFoundException e) {
            throw new IllegalArgumentException("등록되지 않은 카드 정보입니다.");
        }

    }

    @Transactional
    public void deleteCard(AuthClaims claims, UUID cardId) {
        OwnerClaims ownerClaims = (OwnerClaims) claims;
        PayOwner payOwner = payOwnerReader.findByUserClaims(ownerClaims).orElseThrow(() -> new EntityNotFoundException("사용자가 존재하지 않습니다."));
        payOwner.removeRegisteredCard(cardId);
    }


//    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Transactional
    protected Optional<PayOwner> createPayOwnerFromClaims(OwnerClaims claims) {
        PayOwner payOwner = new PayOwner(claims);
        payOwnerStore.save(payOwner);

        return Optional.of(payOwner);
    }
}