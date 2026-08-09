package cn.thesilentnights.easylogin.services;

import cn.thesilentnights.easylogin.configs.EasyLoginConfig;
import cn.thesilentnights.easylogin.repo.PlayerCache;
import cn.thesilentnights.easylogin.services.task.KickPlayer;
import cn.thesilentnights.easylogin.services.task.Message;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class PreLoginService {
        static MobEffectInstance blindness = new MobEffectInstance(
                MobEffects.BLINDNESS,
                25565,
                10,
                false,
                false
        );

        public static void preLogin(ServerPlayer serverPlayer) {
                if (PlayerCache.isPlayerLogged(serverPlayer.getUUID())) {
                        return;
                }
                // Apply blindness effect
                addBlindEffectToPlayer(serverPlayer);


                if (DataService.hasAccount(serverPlayer.getUUID())) {
                        TaskService.addTask(
                                new Message(
                                        serverPlayer,
                                        Component.literal("use /login to login"),
                                        5
                                )
                        );
                } else {
                        TaskService.addTask(
                                new Message(
                                        serverPlayer,
                                        Component.literal("use /register to register"),
                                        5
                                )
                        );
                }

                // Schedule kick timeout
                TaskService.addTask(
                        new KickPlayer(serverPlayer, Long.valueOf(EasyLoginConfig.loginTimeoutSeconds.get()))
                );
        }

        private static void addBlindEffectToPlayer(ServerPlayer serverPlayer) {
                serverPlayer.addEffect(blindness);
        }
}
