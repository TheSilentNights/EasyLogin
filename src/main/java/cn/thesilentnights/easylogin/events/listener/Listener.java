package cn.thesilentnights.easylogin.events.listener;

import cn.thesilentnights.easylogin.service.LoginService;
import cn.thesilentnights.easylogin.service.PreLoginService;
import cn.thesilentnights.easylogin.service.TaskService;
import net.neoforged.bus.api.SubscribeEvent;


public class Listener {
    public Listener() {
        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            PreLoginService.preLogin(serverPlayer);
        }
    }

    @SubscribeEvent
    public void onPlayerQuit(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            LoginService.logoutPlayer(serverPlayer);
        }
    }


    @SubscribeEvent
    public void onServerTick(ServerTickEvent.Pre tickEvent) {
        TaskService.tick();
    }
}