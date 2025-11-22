package com.thesilentnights.repo;

import com.thesilentnights.pojo.PlayerAccount;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
public class PlayerCache {
    private static final Map<UUID, PlayerAccount> cacheMap = new ConcurrentHashMap<>();

    public static void addAccount(PlayerAccount account) {
        cacheMap.put(UUID.fromString(account.getUuid()), account);
    }

    public static Optional<PlayerAccount> getAccount(UUID uuid) {
        return Optional.ofNullable(cacheMap.get(uuid));
    }

    public static boolean hasAccount(UUID uuid) {
        return cacheMap.containsKey(uuid);
    }

    public static void dropAccount(UUID uuid, boolean tempDrop) {
        if (tempDrop){
            PlayerAccount uuid1 = cacheMap.get(uuid);
            PlayerSessionCache.scheduleDrop(uuid1);
        }
        cacheMap.remove(uuid);
    }
}
