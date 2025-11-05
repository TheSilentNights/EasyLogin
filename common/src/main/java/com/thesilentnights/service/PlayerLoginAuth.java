package com.thesilentnights.service;

import com.thesilentnights.pojo.PlayerAccount;
import com.thesilentnights.repo.PlayerCache;
import com.thesilentnights.sql.DatabaseProvider;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;


@Slf4j
public class PlayerLoginAuth {
    static DatabaseProvider provider;
    private static final PlayerCache cache = new PlayerCache();

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

    public static void registerPlayer(ServerPlayer serverPlayer, String password) {
        log.info("registerPlayer");
        provider.saveAuth(new PlayerAccount(
                serverPlayer.getGameProfile().getName(),
                password,serverPlayer.getIpAddress(),
                serverPlayer.getX(),
                serverPlayer.getY(),
                serverPlayer.getZ(),
                serverPlayer.getLevel().dimension().location().getNamespace(),
                serverPlayer.getUUID().toString(),
                null,
                System.currentTimeMillis()
        ));
    }

    public static boolean shouldCancelEvent(ServerPlayer entity){
        return !isLoggedIn(entity);
    }

    public static boolean isLoggedIn(String username, String ip) {
        if (cache.hasAccount(username)){
            return true;
        }
        Optional<PlayerAccount> account = cache.getAccount(username);
        return account.filter(playerAccount -> (account.get().getLastlogin_ip().equals(ip))&&(playerAccount.getLogin_timestamp() + 1000 * 60 * 60 * 24 > System.currentTimeMillis())).isPresent();
    }

    public static boolean isLoggedIn(ServerPlayer entity){
        return isLoggedIn(entity.getName().getString(), entity.getIpAddress());
    }


    public static void logoutPlayer(ServerPlayer serverPlayer) {
        Optional<PlayerAccount> account = cache.getAccount(serverPlayer.getGameProfile().getName());
        if (account.isPresent()){
            PlayerAccount playerAccount = account.get();
            playerAccount.setLastlogin_ip(serverPlayer.getIpAddress());
            playerAccount.setLogin_timestamp(System.currentTimeMillis());
            playerAccount.setLastlogin_x(serverPlayer.getX());
            playerAccount.setLastlogin_y(serverPlayer.getY());
            playerAccount.setLastlogin_z(serverPlayer.getZ());
            playerAccount.setLastlogin_world(serverPlayer.getLevel().dimension().location().getNamespace());
            provider.saveAuth(playerAccount);
        }
    }
}
