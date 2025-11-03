package com.thesilentnights.service;

public interface PlayerLoginAuth {
    boolean authPlayerWithToken(String username, String token);

    boolean authPlayerWithPwd(String username, String password);

    void logoutPlayer(String username, String password);
}
