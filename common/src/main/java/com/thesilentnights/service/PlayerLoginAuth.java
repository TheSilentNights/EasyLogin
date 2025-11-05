package com.thesilentnights.service;

import com.thesilentnights.pojo.PlayerAccount;
import com.thesilentnights.repo.PlayerCache;
import com.thesilentnights.sql.DatabaseProvider;
import dev.architectury.event.EventResult;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Optional;


public class PlayerLoginAuth {
    static DatabaseProvider provider;

    public static void init(DatabaseProvider provider) {
        PlayerLoginAuth.provider = provider;
    }

    public static boolean authPlayerWithPwd(String username, String password) {
        return false;
    }

    public static EventResult shouldCancelEvent(PlayerEntity entity){
        if (entity instanceof ServerPlayerEntity entity1){
            return isLoggedIn(entity1) ? EventResult.pass() : EventResult.interruptFalse();
        }
        return EventResult.pass();
    }

    public static boolean isLoggedIn(String username, String ip) {
        if (PlayerCache.hasAccount( username)){
            return true;
        }
        Optional<PlayerAccount> account = PlayerCache.getAccount(username);
        return account.filter(playerAccount -> (account.get().getLastlogin_ip().equals(ip))&&(playerAccount.getLogin_timestamp() + 1000 * 60 * 60 * 24 > System.currentTimeMillis())).isPresent();
    }

    public static boolean isLoggedIn(ServerPlayerEntity entity){
        return isLoggedIn(entity.getName().getString(), entity.getIp());
    }


    public static void logoutPlayer(String username, String password) {

    }
}
