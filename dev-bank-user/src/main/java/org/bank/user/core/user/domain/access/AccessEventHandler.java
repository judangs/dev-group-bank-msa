package org.bank.user.core.user.domain.access;


import lombok.RequiredArgsConstructor;
import org.bank.user.core.user.domain.access.repository.UserCredentialAccessJpaRepository;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class AccessEventHandler {

    private final UserCredentialAccessJpaRepository userCredentialAccessJpaRepository;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public void handle(UserAuthEvent event) {
        // 여기서 유저 로그인 / 로그아웃 이벤트를 받는다.
        userCredentialAccessJpaRepository.save(event.toDomain());









    }


}
