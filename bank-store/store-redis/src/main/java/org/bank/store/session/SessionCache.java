package org.bank.store.session;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Getter
@RedisHash(value = "session", timeToLive = 60 * 60 * 24 * 14)
public class SessionCache implements Serializable {

    @Id
    private String access;

    private String refresh;

    public SessionCache(String access, String refresh) {
        this.access = access;
        this.refresh = refresh;
    }


}
