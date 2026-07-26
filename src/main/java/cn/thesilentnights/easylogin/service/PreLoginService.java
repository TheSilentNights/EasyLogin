package cn.thesilentnights.easylogin.service;

import cn.thesilentnights.easylogin.configs.EasyLoginConfig;
import cn.thesilentnights.easylogin.repo.PlayerCache;
import cn.thesilentnights.easylogin.service.task.KickPlayer;
import cn.thesilentnights.easylogin.service.task.Message;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.neoforged.neoforge.client.event.ClientChatReceivedEvent.Player;


public class PreLoginService {

    public static void preLogin(ServerPlayer serverPlayer) {
        if (PlayerCache.isPlayerLogged(serverPlayer.getUUID())) {
            return;
        }
        // Apply blindness effect
        addBlindEffectToPlayer(serverPlayer);


        if (AccountService.hasAccount(serverPlayer.getUUID())) {
            TaskService.addTask(new Message(serverPlayer, Component.literal("use /login to login"), 5));
        } else {
            TaskService.addTask(
                    new Message(serverPlayer, Component.literal("use /register to register"), 5)
            );
        }

        // Schedule kick timeout
        TaskService.addTask(
                new KickPlayer(serverPlayer, Long.valueOf(EasyLoginConfig.loginTimeoutSeconds.get()))
        );
    }

    private static void addBlindEffectToPlayer(ServerPlayer serverPlayer) {
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