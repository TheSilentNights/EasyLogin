package com.thesilentnights.easylogin.service;

import com.thesilentnights.easylogin.configs.EasyLoginConfig;
import com.thesilentnights.easylogin.service.task.KickPlayer;
import com.thesilentnights.easylogin.service.task.Message;
import com.thesilentnights.easylogin.utils.TextUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class PreLoginService {

    public static void preLogin(ServerPlayer serverPlayer) {
        if (NPCService.isNPC(serverPlayer)) {
            ByPassService.addBypass(serverPlayer.getUUID());
        }

        // Try to re-login from cache
        if (LoginService.reLogFromCache(serverPlayer)) {
            serverPlayer.sendMessage(TextUtil.serialize(TextUtil.FormatType.INFO, "relogged from cache"), serverPlayer.getUUID());
            return;
        }

        // Apply blindness effect
        addBlindEffectToPlayer(serverPlayer);


        if (AccountService.hasAccount(serverPlayer.getUUID())) {
            TaskService.addTask(new Message(serverPlayer, TextUtil.serialize(TextUtil.FormatType.INFO, "use /login to login"), 80));
        } else {
            TaskService.addTask(
                    new Message(serverPlayer, TextUtil.serialize(TextUtil.FormatType.INFO, "use /register to register"), 80)
            );
        }

        // Schedule kick timeout
        TaskService.addTask(
                new KickPlayer(serverPlayer, EasyLoginConfig.INSTANCE.loginTimeoutTick)
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