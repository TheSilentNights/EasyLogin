package com.thesilentnights.service;

import com.google.inject.Singleton;
import com.thesilentnights.repo.PlayerCache;
import com.thesilentnights.sql.SqlLite;
import jakarta.inject.Inject;

@Singleton
public class PlayerLoginAuthImpl implements PlayerLoginAuth {
    @Inject
    SqlLite sqlLite;

    @Override
    public boolean authPlayerWithToken(String username, String token) {
        return false;
    }

    @Override
    public boolean authPlayerWithPwd(String username, String password) {
        return false;
    }

    @Override
    public void logoutPlayer(String username, String password) {
        if (PlayerCache.isLoggedIn(username)) {
            PlayerCache.dropAccount(username);
        }
    }
}
