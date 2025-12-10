package com.thesilentnights.events;

import com.thesilentnights.events.ievents.EasyLoginEvents;
import com.thesilentnights.repo.BlockPosRepo;
import com.thesilentnights.repo.PlayerCache;
import com.thesilentnights.repo.PlayerSessionCache;
import com.thesilentnights.service.AccountService;
import com.thesilentnights.service.TaskService;
import com.thesilentnights.service.task.KickPlayer;
import com.thesilentnights.service.task.Message;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.network.chat.TextComponent;


public class ServerSideEvents {

    public static void register() {
        PlayerEvent.PLAYER_JOIN.register(entity -> {
            if (PlayerSessionCache.hasSession(entity) && PlayerSessionCache.getSession(entity.getUUID()).getAccount().getLastlogin_ip().equals(entity.getIpAddress())) {
                entity.sendMessage(new TextComponent("already logged in!"), entity.getUUID());
                EasyLoginEvents.ON_LOGIN.invoker().onLogin(PlayerSessionCache.getSession(entity.getUUID()).getAccount(), entity);
            } else {
                if (AccountService.hasAccount(entity.getUUID())) {
                    TaskService.addTask(TaskService.generateTaskIdentifier(entity.getUUID(), TaskService.TaskType.MESSAGE), new Message(entity, new TextComponent("please login your account by /login"), 80, true));
                } else {
                    TaskService.addTask(TaskService.generateTaskIdentifier(entity.getUUID(), TaskService.TaskType.MESSAGE), new Message(entity, new TextComponent("please register your account by /register"), 80, true));
                }
                TaskService.addTask(TaskService.generateTaskIdentifier(entity.getUUID(), TaskService.TaskType.KICK_PLAYER), new KickPlayer(entity, 60 * 20));
            }
        });

        PlayerEvent.PLAYER_QUIT.register(entity -> {
            AccountService.logoutPlayer(entity, false);
            TaskService.cancelPlayer(entity.getUUID());

        });

        TickEvent.SERVER_POST.register(server -> {
            TaskService.tick();
            PlayerSessionCache.tick();
        });

        // custom event
        EasyLoginEvents.ON_LOGIN.register(((account, serverPlayer) -> {
            BlockPosRepo.removeBlockPos(account.getUsername());
            PlayerCache.addAccount(account);
            TaskService.cancelPlayer(serverPlayer.getUUID());
        }));
        EasyLoginEvents.ON_LOGOUT.register(((account, serverPlayer) -> {
            PlayerCache.dropAccount(serverPlayer.getUUID(), true);
            BlockPosRepo.removeBlockPos(account.getUsername());
        }));

    }
}
