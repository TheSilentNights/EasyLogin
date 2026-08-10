package cn.thesilentnights.easylogin.services.auth;

import cn.thesilentnights.easylogin.configs.EasyLoginConfig;
import cn.thesilentnights.easylogin.repo.PlayerCache;
import cn.thesilentnights.easylogin.services.data.DataService;
import cn.thesilentnights.easylogin.services.task.KickPlayer;
import cn.thesilentnights.easylogin.services.task.Message;
import cn.thesilentnights.easylogin.services.task.TaskService;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class PreLoginServiceImpl implements PreLoginService {
        private final DataService dataService;
        private final TaskService taskService;

        public PreLoginServiceImpl(DataService dataService, TaskService taskService) {
                this.dataService = dataService;
                this.taskService = taskService;
        }

        private final static MobEffectInstance blindness = new MobEffectInstance(
                MobEffects.BLINDNESS,
                25565,
                10,
                false,
                false
        );

        @Override
        public void preLogin(ServerPlayer serverPlayer) {
                if (PlayerCache.isPlayerLogged(serverPlayer.getUUID())) {
                        return;
                }
                // Apply blindness effect
                addBlindEffectToPlayer(serverPlayer);


                if (dataService.hasAccount(serverPlayer.getUUID())) {
                        taskService.addTask(
                                new Message(
                                        serverPlayer,
                                        Component.literal("use /login to login"),
                                        5
                                )
                        );
                } else {
                        taskService.addTask(
                                new Message(
                                        serverPlayer,
                                        Component.literal("use /register to register"),
                                        5
                                )
                        );
                }

                // Schedule kick timeout
                taskService.addTask(
                        new KickPlayer(serverPlayer, Long.valueOf(EasyLoginConfig.loginTimeoutSeconds.get()))
                );
        }

        private void addBlindEffectToPlayer(ServerPlayer serverPlayer) {
                serverPlayer.addEffect(blindness);
        }
}
