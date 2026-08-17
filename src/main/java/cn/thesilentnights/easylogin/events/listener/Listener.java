package cn.thesilentnights.easylogin.events.listener;

import cn.thesilentnights.easylogin.services.auth.LoginService;
import cn.thesilentnights.easylogin.services.auth.PreLoginService;
import cn.thesilentnights.easylogin.services.task.TaskService;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

public class Listener {
        private final PreLoginService preLoginService;
        private final LoginService loginService;
        private final TaskService taskService;

        public Listener(
                PreLoginService preLoginService,
                LoginService loginService,
                TaskService taskService,
                IEventBus eventBus
        ) {
                this.preLoginService = preLoginService;
                this.loginService = loginService;
                this.taskService = taskService;
                eventBus.register(this);
        }

        @SubscribeEvent
        public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
                if (event.getEntity() instanceof ServerPlayer serverPlayer) {
                        preLoginService.preLogin(serverPlayer);
                }
        }

        @SubscribeEvent
        public void onPlayerQuit(PlayerEvent.PlayerLoggedOutEvent event) {
                if (event.getEntity() instanceof ServerPlayer serverPlayer) {
                        loginService.logout(serverPlayer);
                }
        }


        @SubscribeEvent
        public void onServerTick(ServerTickEvent.Pre tickEvent) {
                taskService.tick();
        }
}
