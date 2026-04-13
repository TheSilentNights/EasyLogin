package cn.thesilentnights.easylogin.service;

import cn.thesilentnights.easylogin.configs.EasyLoginConfig;
import cn.thesilentnights.easylogin.service.task.KickPlayer;
import cn.thesilentnights.easylogin.service.task.Message;
import cn.thesilentnights.easylogin.utils.MessageSender;
import cn.thesilentnights.easylogin.utils.MessageSender.MessageType;
import cn.thesilentnights.easylogin.utils.TextUtil;
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
            MessageSender.sendMessage(
                    serverPlayer,
                    "relogged from cache",
                    MessageType.INFO
            );
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
                new KickPlayer(serverPlayer, EasyLoginConfig.loginTimeoutTick.get())
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