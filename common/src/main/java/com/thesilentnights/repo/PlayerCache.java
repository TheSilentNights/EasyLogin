package com.thesilentnights.repo;

import com.thesilentnights.pojo.PlayerAccount;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


public class PlayerCache {
    private static final Map<String, PlayerAccount> cacheMap = new ConcurrentHashMap<>();

     public static void addAccount(PlayerAccount account) {
        cacheMap.put(account.getUsername(), account);
    }

    public static Optional<PlayerAccount> getAccount(String username) {
        return Optional.ofNullable(cacheMap.get(username));
    }

    public static boolean hasAccount(String username) {
        return cacheMap.containsKey(username);
    }

    public static void dropAccount(String username) {
        cacheMap.remove(username);
    }
}
