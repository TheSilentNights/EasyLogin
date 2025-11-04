package com.thesilentnights.service;

import com.thesilentnights.repo.PlayerCache;
import com.thesilentnights.sql.DatabaseProvider;


public class PlayerLoginAuth {
    static DatabaseProvider provider;

    public static void init(DatabaseProvider provider) {
        PlayerLoginAuth.provider = provider;
    }

    public static boolean authPlayerWithToken(String username, String token) {
        return false;
    }


    public static boolean authPlayerWithPwd(String username, String password) {
        return false;
    }


    public static void logoutPlayer(String username, String password) {
        if (PlayerCache.isLoggedIn(username)) {
            PlayerCache.dropAccount(username);
        }
    }
}
