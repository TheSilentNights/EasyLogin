package com.thesilentnights.easylogin.events.listener;

import com.thesilentnights.easylogin.service.AccountService;
import com.thesilentnights.easylogin.service.LoginService;
import com.thesilentnights.easylogin.service.PreLoginService;
import com.thesilentnights.easylogin.service.TaskService;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class Listener {

    private final AccountService accountService;
    private final LoginService loginService;
    private final PreLoginService preLoginService;

    public Listener(AccountService accountService, LoginService loginService, PreLoginService preLoginService) {
        this.accountService = accountService;
        this.loginService = loginService;
        this.preLoginService = preLoginService;

        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getPlayer() instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer) event.getPlayer();
            preLoginService.preLogin(serverPlayer);
        }
    }

    @SubscribeEvent
    public void onPlayerQuit(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getPlayer() instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer) event.getPlayer();
            logoutPlayer(serverPlayer);
        }
    }

    private void logoutPlayer(ServerPlayer serverPlayer) {
        loginService.logoutPlayer(serverPlayer);
        TaskService.cancelPlayer(serverPlayer.getUUID());
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent tickEvent) {
        if (tickEvent.phase == TickEvent.Phase.END) {
            TaskService.tick();
        }
    }
}