package com.thesilentnights.repo;

import com.thesilentnights.pojo.PlayerAccount;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class PlayerCache {
    private final Map<String, PlayerAccount> cacheMap = new HashMap<>();

    synchronized public void addAccount(PlayerAccount account) {
        cacheMap.put(account.getUsername(), account);
    }

    synchronized public Optional<PlayerAccount> getAccount(String username) {
        return Optional.ofNullable(cacheMap.get(username));
    }

    synchronized public boolean hasAccount(String username) {
        return cacheMap.containsKey(username);
    }

    synchronized public void dropAccount(String username) {
        cacheMap.remove(username);
    }
}
