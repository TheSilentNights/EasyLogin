package com.thesilentnights.easylogin.repo;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.thesilentnights.easylogin.pojo.PlayerAccount;
import com.thesilentnights.easylogin.pojo.PlayerSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.UUID;

public class PlayerSessionCache {

    private static final Logger log = LogManager.getLogger(PlayerSessionCache.class);
    private static final Cache<String, PlayerSession> sessions = Caffeine.newBuilder()
            .maximumSize(60)
            .expireAfterAccess(Duration.ofSeconds(5 * 60L))
            .build();

    public static void scheduleDrop(PlayerAccount account) {
        sessions.put(account.getUuid().toString(), new PlayerSession(account));
    }

    public static boolean hasSession(UUID uuid) {
        return sessions.getIfPresent(uuid.toString()) != null;
    }

    public static PlayerSession getSession(UUID key) {
        return sessions.getIfPresent(key.toString());
    }
}