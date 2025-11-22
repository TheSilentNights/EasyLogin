package com.thesilentnights.service;

import com.thesilentnights.events.ievents.EasyLoginEvents;
import com.thesilentnights.exception.AlreadyLoggedInException;
import com.thesilentnights.exception.PasswordDoesNotMatchException;
import com.thesilentnights.pojo.PlayerAccount;
import com.thesilentnights.repo.PlayerCache;
import com.thesilentnights.sql.DatabaseProvider;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;


@Slf4j
@Service
public class PlayerLoginService {
    @Autowired
    private DatabaseProvider provider;

    public boolean authPlayerWithPwd(ServerPlayer serverPlayer, String password) throws AlreadyLoggedInException {
        if (PlayerCache.hasAccount(serverPlayer.getUUID())) {
            throw new AlreadyLoggedInException(serverPlayer.getGameProfile().getName());
        }
        Optional<PlayerAccount> playerAccount1 = provider.getAuthByUUID(serverPlayer.getUUID().toString()).filter(playerAccount -> playerAccount.getPassword().equals(password));
        if (playerAccount1.isPresent()) {
            //push events
            EasyLoginEvents.ON_LOGIN.invoker().onLogin(playerAccount1.get(), serverPlayer);
            return true;
        }
        return false;
    }

    public boolean hasAccount(UUID uuid) {
        return provider.getAuthByUUID(uuid.toString()).isPresent();
    }

    public boolean shouldCancelEvent(Player entity) {
        if (entity instanceof ServerPlayer entity1) {
            return !isLoggedIn(entity1);
        }
        return false;
    }

    public boolean isLoggedIn(ServerPlayer serverPlayer){
        return PlayerCache.hasAccount(serverPlayer.getUUID());
    }

    public void registerPlayer(ServerPlayer serverPlayer, String password ,String repeat) throws PasswordDoesNotMatchException {
        log.info("registerPlayer {}", serverPlayer.getGameProfile().getName());
        if (!password.equals(repeat)){
            throw new PasswordDoesNotMatchException(password,repeat);
        }

        provider.saveAuth(new PlayerAccount(
                serverPlayer.getGameProfile().getName(),
                password, serverPlayer.getIpAddress(),
                serverPlayer.getX(),
                serverPlayer.getY(),
                serverPlayer.getZ(),
                serverPlayer.getLevel().dimension().location().getNamespace(),
                serverPlayer.getUUID().toString(),
                null,
                System.currentTimeMillis()
        ));
        Optional<PlayerAccount> auth = provider.getAuthByUUID(serverPlayer.getUUID().toString());
        if (auth.isEmpty()){
            log.atError().log("sql error found in registering player");
        }else{
            EasyLoginEvents.ON_LOGIN.invoker().onLogin(auth.get(), serverPlayer);
        }
    }



    public Optional<PlayerAccount> getAccount(UUID uuid) {
        return provider.getAuthByUUID(uuid.toString());
    }

    /**
     *
     * @param serverPlayer targetPlayer
     * @param proactive whether the action is made by a player or is automatically triggered by logout event
     */
    public void logoutPlayer(ServerPlayer serverPlayer,boolean proactive) {
        Optional<PlayerAccount> account = PlayerCache.getAccount(serverPlayer.getUUID());
        if (account.isPresent()) {
            PlayerAccount playerAccount = account.get();
            playerAccount.setLastlogin_ip(serverPlayer.getIpAddress());
            playerAccount.setLogin_timestamp(System.currentTimeMillis());
            playerAccount.setLastlogin_x(serverPlayer.getX());
            playerAccount.setLastlogin_y(serverPlayer.getY());
            playerAccount.setLastlogin_z(serverPlayer.getZ());
            playerAccount.setLastlogin_world(serverPlayer.getLevel().dimension().location().getNamespace());
            provider.saveAuth(playerAccount);
            //push events
            EasyLoginEvents.ON_LOGOUT.invoker().onLogout(playerAccount, serverPlayer);
        }
    }
}
