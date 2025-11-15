package com.thesilentnights.events;

import com.thesilentnights.events.ievents.EasyLoginEvents;
import com.thesilentnights.repo.BlockPosRepo;
import com.thesilentnights.repo.PlayerCache;
import com.thesilentnights.service.PlayerLoginAuth;
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
    PlayerLoginAuth playerLoginAuth;
    
    public void register(){
        PlayerEvent.PLAYER_JOIN.register(entity -> {
            if (!playerLoginAuth.isLoggedIn(entity)){
                if (playerLoginAuth.hasAccount(entity.getGameProfile().getName())){
                    TickTimerManager.addTickTimer(new Message(entity,new TextComponent("please login your account by /login"), 80, true));
                }else{
                    TickTimerManager.addTickTimer(new Message(entity,new TextComponent("please register your account by /register"), 80, true));
                }
            }
        });

        PlayerEvent.PLAYER_QUIT.register(entity -> {
            playerLoginAuth.logoutPlayer(entity);
            TickTimerManager.cancel(entity.getUUID());
        });

        TickEvent.SERVER_POST.register(server -> {
            TickTimerManager.tick();
        });

        // custom event
        EasyLoginEvents.ON_LOGIN.register(((account, serverPlayer) -> {
            BlockPosRepo.removeBlockPos(account.getUsername());
            PlayerCache.addAccount(account);
        }));
        EasyLoginEvents.ON_LOGOUT.register(((account, serverPlayer)  -> {
            PlayerCache.dropAccount(serverPlayer.getGameProfile().getName());
            BlockPosRepo.removeBlockPos(account.getUsername());
        }));
    }
}
