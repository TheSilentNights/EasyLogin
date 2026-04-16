package cn.thesilentnights.easylogin.events.listener;

import cn.thesilentnights.easylogin.configs.EasyLoginConfig;
import cn.thesilentnights.easylogin.service.LoginService;
import cn.thesilentnights.easylogin.service.PreLoginService;
import cn.thesilentnights.easylogin.service.TaskService;
import cn.thesilentnights.easylogin.utils.LogUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class Listener {
    public Listener() {
        MinecraftForge.EVENT_BUS.register(this);
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
    public void onServerTick(TickEvent.ServerTickEvent tickEvent) {
        TaskService.tick();
    }
}