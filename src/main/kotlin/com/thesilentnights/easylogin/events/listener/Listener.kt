package com.thesilentnights.easylogin.events.listener

import com.thesilentnights.easylogin.service.AccountService
import com.thesilentnights.easylogin.service.LoginService
import com.thesilentnights.easylogin.service.PreLoginService
import com.thesilentnights.easylogin.service.TaskService
import net.minecraft.server.level.ServerPlayer
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.TickEvent.ServerTickEvent
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent
import net.minecraftforge.eventbus.api.SubscribeEvent


class Listener {
    val accountService: AccountService;
    val loginService: LoginService;
    val preLoginService: PreLoginService;

    constructor(accountService: AccountService, loginService: LoginService, preLoginService: PreLoginService) {
        this.accountService = accountService
        this.loginService = loginService
        this.preLoginService = preLoginService

        with(MinecraftForge.EVENT_BUS) {
            addListener(this@Listener::onPlayerJoin)
            addListener(this@Listener::onPlayerQuit)
            addListener(this@Listener::onServerTick)
        }
    }

    @SubscribeEvent
    fun onPlayerJoin(event: PlayerLoggedInEvent) {
        if (event.player is ServerPlayer) {
            val serverPlayer = event.player as ServerPlayer
            preLoginService.preLogin(serverPlayer)
        }
    }


    @SubscribeEvent
    fun onPlayerQuit(event: PlayerLoggedOutEvent) {
        if (event.player is ServerPlayer) {
            val serverPlayer = event.player as ServerPlayer
            loginService.logoutPlayer(serverPlayer)
            TaskService.cancelPlayer(serverPlayer.getUUID())
        }
    }

    @SubscribeEvent
    fun onServerTick(tickEvent: ServerTickEvent) {
        TaskService.tick()
    }
}