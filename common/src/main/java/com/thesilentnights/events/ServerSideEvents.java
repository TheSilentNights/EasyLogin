package com.thesilentnights.events;

import com.thesilentnights.events.ievents.EasyLoginEvents;
import com.thesilentnights.repo.BlockPosRepo;
import com.thesilentnights.repo.PlayerCache;
import com.thesilentnights.repo.PlayerSessionCache;
import com.thesilentnights.service.PlayerLoginService;
import com.thesilentnights.task.KickPlayer;
import com.thesilentnights.task.Message;
import com.thesilentnights.task.TickTimerManager;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.network.chat.TextComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServerSideEvents {
    @Autowired
    PlayerLoginService playerLoginService;
    
    public void register(){
        PlayerEvent.PLAYER_JOIN.register(entity -> {
            if (PlayerSessionCache.hasSession(entity)){
                EasyLoginEvents.ON_LOGIN.invoker().onLogin(PlayerSessionCache.getSession(entity.getUUID()).getAccount(),entity);
            }else{
                if (playerLoginService.hasAccount(entity.getUUID())){
                    TickTimerManager.addTickTimer(new Message(entity,new TextComponent("please login your account by /login"), 80, true));
                }else{
                    TickTimerManager.addTickTimer(new Message(entity,new TextComponent("please register your account by /register"), 80, true));
                }
                TickTimerManager.addTickTimer(new KickPlayer(entity,60*20));
            }
        });

        PlayerEvent.PLAYER_QUIT.register(entity -> {
            playerLoginService.logoutPlayer(entity,false);
            TickTimerManager.cancelPlayer(entity.getUUID());
        });

        TickEvent.SERVER_POST.register(server -> {
            TickTimerManager.tick();
            PlayerSessionCache.tick();
        });

        // custom event
        EasyLoginEvents.ON_LOGIN.register(((account, serverPlayer) -> {
            BlockPosRepo.removeBlockPos(account.getUsername());
            PlayerCache.addAccount(account);
            TickTimerManager.cancelPlayer(serverPlayer.getUUID());
        }));
        EasyLoginEvents.ON_LOGOUT.register(((account, serverPlayer)  -> {
            PlayerCache.dropAccount(serverPlayer.getUUID(),true);
            BlockPosRepo.removeBlockPos(account.getUsername());
        }));

    }
}
