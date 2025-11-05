package com.thesilentnights.service;

import com.thesilentnights.pojo.PlayerAccount;
import com.thesilentnights.repo.PlayerCache;
import com.thesilentnights.sql.DatabaseProvider;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;


public class PlayerLoginAuth {
    static DatabaseProvider provider;

    public static void init(DatabaseProvider provider) {
        PlayerLoginAuth.provider = provider;
    }

    public static boolean authPlayerWithPwd(String username, String password) {
        return false;
    }

    public static boolean hasAccount(String username) {
        return provider.getAuth(username).isPresent();
    }

    public static boolean shouldCancelEvent(Player entity){
        if (entity instanceof ServerPlayer entity1){
            return shouldCancelEvent(entity1);
        }
        return false;
    }

    public static boolean shouldCancelEvent(ServerPlayer entity){
        return !isLoggedIn(entity);
    }

    public static boolean isLoggedIn(String username, String ip) {
        if (PlayerCache.hasAccount(username)){
            return true;
        }
        Optional<PlayerAccount> account = PlayerCache.getAccount(username);
        return account.filter(playerAccount -> (account.get().getLastlogin_ip().equals(ip))&&(playerAccount.getLogin_timestamp() + 1000 * 60 * 60 * 24 > System.currentTimeMillis())).isPresent();
    }

    public static boolean isLoggedIn(ServerPlayer entity){
        return isLoggedIn(entity.getName().getString(), entity.getIpAddress());
    }


    public static void logoutPlayer(String username, String password) {

    }
}
