package org.bank.user.core.user.infrastructure.persistence;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Component
public class MailCacheStore {

    // 사용자 이메일 인증을 기다리는 대기 큐
    private final Map<String, MailCacheMeta> mailCache = new ConcurrentHashMap<>();


}
