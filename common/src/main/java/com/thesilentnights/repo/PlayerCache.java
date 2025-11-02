package com.thesilentnights.repo;

import com.thesilentnights.pojo.PlayerAccount;

import java.util.HashMap;
import java.util.Map;


public class PlayerCache {
    static Map<String, PlayerAccount> cacheMap = new HashMap<>();

    synchronized public static void addAccount(PlayerAccount account) {
        cacheMap.put(account.name(), account);
    }

    synchronized public static boolean isLoggedIn(String username) {
        return cacheMap.containsKey(username);
    }

    synchronized public static void dropAccount(String username) {
        cacheMap.remove(username);
    }
}
