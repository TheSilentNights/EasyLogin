package com.thesilentnights.repo;

import com.thesilentnights.pojo.PlayerAccount;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class PlayerCache {
    private static final Map<String, PlayerAccount> cacheMap = new HashMap<>();

    synchronized public static void addAccount(PlayerAccount account) {
        cacheMap.put(account.getUsername(), account);
    }

    synchronized public static Optional<PlayerAccount> getAccount(String username) {
        return Optional.ofNullable(cacheMap.get(username));
    }

    synchronized public static boolean hasAccount(String username) {
        return cacheMap.containsKey(username);
    }

    synchronized public static void dropAccount(String username) {
        cacheMap.remove(username);
    }
}
