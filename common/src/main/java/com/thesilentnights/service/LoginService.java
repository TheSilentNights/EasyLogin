package com.thesilentnights.service;

import com.thesilentnights.repo.PlayerCache;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class LoginService {
    public static boolean isLoggedIn(UUID key){
        return PlayerCache.hasAccount(key);
    }
}
