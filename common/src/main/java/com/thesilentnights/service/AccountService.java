package com.thesilentnights.service;

import com.thesilentnights.events.ievents.EasyLoginEvents;
import com.thesilentnights.exception.AlreadyLoggedInException;
import com.thesilentnights.exception.PasswordDoesNotMatchException;
import com.thesilentnights.pojo.PlayerAccount;
import com.thesilentnights.repo.PlayerCache;
import com.thesilentnights.sql.DatabaseProvider;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;
import java.util.UUID;


@Slf4j
public class AccountService {
    private static DatabaseProvider provider;

    public static void init(DatabaseProvider databaseProvider) {
        provider = databaseProvider;
    }

    public static boolean loginWithPassword(ServerPlayer serverPlayer, String password) throws AlreadyLoggedInException {
        if (PlayerCache.hasAccount(serverPlayer.getUUID())) {
            throw new AlreadyLoggedInException(serverPlayer.getGameProfile().getName());
        }
        Optional<PlayerAccount> playerAccount1 = provider.getAuthByUUID(serverPlayer.getUUID()).filter(playerAccount -> playerAccount.getPassword().equals(password));
        if (playerAccount1.isPresent()) {
            //push events
            EasyLoginEvents.ON_LOGIN.invoker().onLogin(playerAccount1.get(), serverPlayer);
            return true;
        }
        return false;
    }

    public static boolean hasAccount(UUID uuid) {
        return provider.getAuthByUUID(uuid).isPresent();
    }

    public static boolean hasAccount(String username) {
        return provider.getAuthByName(username).isPresent();
    }


    public static void registerPlayer(ServerPlayer serverPlayer, String password, String repeat) throws PasswordDoesNotMatchException {
        log.info("registerPlayer {}", serverPlayer.getGameProfile().getName());
        if (!password.equals(repeat)) {
            throw new PasswordDoesNotMatchException(password, repeat);
        }

        boolean flag = provider.saveAccount(new PlayerAccount(
                serverPlayer.getGameProfile().getName(),
                password, serverPlayer.getIpAddress(),
                serverPlayer.getX(),
                serverPlayer.getY(),
                serverPlayer.getZ(),
                serverPlayer.getLevel().dimension().location().getNamespace(),
                serverPlayer.getUUID(),
                null,
                System.currentTimeMillis()
        ));

        if (!flag) {
            log.error("error occurred while saving auth");
            return;
        }

        Optional<PlayerAccount> auth = provider.getAuthByUUID(serverPlayer.getUUID());
        if (auth.isEmpty()) {
            log.atError().log("sql error found in registering player");
        } else {
            EasyLoginEvents.ON_LOGIN.invoker().onLogin(auth.get(), serverPlayer);
        }
    }


    public static Optional<PlayerAccount> getAccount(UUID uuid) {
        return provider.getAuthByUUID(uuid);
    }

    public static Optional<PlayerAccount> getAccount(String username) {
        return provider.getAuthByName(username);
    }

    public static void updateAccount(PlayerAccount playerAccount){
        provider.saveAccount(playerAccount);
    }

    /**
     *
     * @param serverPlayer targetPlayer
     * @param proactive    whether the action is made by a player or is automatically triggered by logout event
     */
    public static void logoutPlayer(ServerPlayer serverPlayer, boolean proactive) {
        Optional<PlayerAccount> account = PlayerCache.getAccount(serverPlayer.getUUID());
        if (account.isPresent()) {
            PlayerAccount playerAccount = account.get();
            playerAccount.setLastlogin_ip(serverPlayer.getIpAddress());
            playerAccount.setLogin_timestamp(System.currentTimeMillis());
            playerAccount.setLastlogin_x(serverPlayer.getX());
            playerAccount.setLastlogin_y(serverPlayer.getY());
            playerAccount.setLastlogin_z(serverPlayer.getZ());
            playerAccount.setLastlogin_world(serverPlayer.getLevel().dimension().location().getNamespace());
            provider.saveAccount(playerAccount);
            //push events
            EasyLoginEvents.ON_LOGOUT.invoker().onLogout(playerAccount, serverPlayer);
        }
    }
}
