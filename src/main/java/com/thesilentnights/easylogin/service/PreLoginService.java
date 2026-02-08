package com.thesilentnights.easylogin.service;

import com.thesilentnights.easylogin.configs.EasyLoginConfig;
import com.thesilentnights.easylogin.service.task.KickPlayer;
import com.thesilentnights.easylogin.service.task.Message;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import org.springframework.stereotype.Service;

@Service
public class PreLoginService {

    private final AccountService accountService;
    private final LoginService loginService;

    public PreLoginService(AccountService accountService, LoginService loginService) {
        this.accountService = accountService;
        this.loginService = loginService;
    }

    public void preLogin(ServerPlayer serverPlayer) {
        if (NPCService.isNPC(serverPlayer)) {
            ByPassService.addBypass(serverPlayer.getUUID());
        }

        // Try to re-login from cache
        if (loginService.reLogFromCache(serverPlayer)) {
            serverPlayer.sendMessage(new TextComponent("relogged from cache"), serverPlayer.getUUID());
            return;
        }

        // Apply blindness effect
        addBlindEffectToPlayer(serverPlayer);

        String taskIdBase = TaskService.generateTaskIdentifier(
                serverPlayer.getUUID(),
                TaskService.Suffix.MESSAGE.name()
        );

        if (accountService.hasAccount(serverPlayer.getUUID())) {
            TaskService.addTask(
                    taskIdBase,
                    new Message(serverPlayer, new TextComponent("please login your account by /login"), 80, true)
            );
        } else {
            TaskService.addTask(
                    taskIdBase,
                    new Message(serverPlayer, new TextComponent("please register your account by /register"), 80, true)
            );
        }

        // Schedule kick timeout
        TaskService.addTask(
                TaskService.generateTaskIdentifier(
                        serverPlayer.getUUID(),
                        TaskService.Suffix.TIMEOUT.name()
                ),
                new KickPlayer(serverPlayer, EasyLoginConfig.loginTimeoutTick.get())
        );
    }

    private void addBlindEffectToPlayer(ServerPlayer serverPlayer) {
        MobEffectInstance blindness = new MobEffectInstance(
                MobEffects.BLINDNESS,
                25565,
                10,
                false,
                false
        );
        serverPlayer.addEffect(blindness);
    }
}