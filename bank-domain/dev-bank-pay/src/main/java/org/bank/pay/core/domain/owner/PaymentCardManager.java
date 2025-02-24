package org.bank.pay.core.domain.owner;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.auth.AuthenticationException;
import org.bank.pay.core.domain.owner.repository.PayOwnerReader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentCardManager implements PayCardService{

    private final PayOwnerReader payOwnerReader;

    @Transactional
    public PaymentCard register(AuthClaims user, PaymentCard card) throws AuthenticationException{
        Optional<PayOwner> payOwner = payOwnerReader.findByUserClaims(user);

        payOwner.ifPresentOrElse(
                owner -> {
                    if(owner.match(card).isEmpty()) {
                        owner.addPaymentCard(card);
                    }
                },
                () -> new AuthenticationException("존재하지 않는 사용자입니다."));

        return card;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentCard> gets(AuthClaims user) throws AuthenticationException{

        try {
            return payOwnerReader.findPaymentCardsByUser(user);
        } catch (EntityNotFoundException e) {
            throw new AuthenticationException("사용자가 존재하지 않습니다.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentCard get(AuthClaims user, UUID cardId) throws AuthenticationException {

        return this.gets(user).stream()
                .filter(card -> card.match(cardId)).findFirst()
                .orElseThrow();
    }

    @Override
    @Transactional
    public void updateAlias(AuthClaims user, UUID cardId, String cardAlias) throws AuthenticationException {
            PayOwner payOwner = payOwnerReader.findByUserClaims(user)
                    .orElseThrow(() -> new AuthenticationException("사용자가 존재하지 않습니다."));

            payOwner.match(cardId).ifPresent(card -> card.alias(cardAlias));
    }

    @Override
    @Transactional
    public void remove(AuthClaims user, UUID cardId) throws AuthenticationException {
        PayOwner payOwner = payOwnerReader.findByUserClaims(user)
                .orElseThrow(() -> new AuthenticationException("사용자가 존재하지 않습니다."));

        payOwner.close(cardId);
    }
}