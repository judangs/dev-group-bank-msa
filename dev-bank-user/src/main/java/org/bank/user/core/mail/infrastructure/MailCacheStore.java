package org.bank.user.core.mail.infrastructure;

import lombok.Getter;
import org.bank.user.core.mail.domain.MailCache;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Component
public class MailCacheStore {

    // 사용자 이메일 인증을 기다리는 대기 큐
    private final Map<String, MailCache> mailCache = new ConcurrentHashMap<>();


}
