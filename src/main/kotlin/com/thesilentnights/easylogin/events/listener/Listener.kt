package com.thesilentnights.easylogin.events.listener

import com.thesilentnights.easylogin.configs.EasyLoginConfig
import com.thesilentnights.easylogin.events.EasyLoginEvents
import com.thesilentnights.easylogin.repo.PlayerCache
import com.thesilentnights.easylogin.repo.PlayerSessionCache
import com.thesilentnights.easylogin.service.AccountService
import com.thesilentnights.easylogin.service.LoginService
import com.thesilentnights.easylogin.service.TaskService
import com.thesilentnights.easylogin.service.task.KickPlayer
import com.thesilentnights.easylogin.service.task.Message
import net.minecraft.network.chat.TextComponent
import net.minecraft.server.level.ServerPlayer
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.TickEvent.ServerTickEvent
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent
import org.koin.core.component.KoinComponent


class Listener(val accountService: AccountService,val loginService: LoginService): KoinComponent {

    init {
        with(MinecraftForge.EVENT_BUS){
            addListener(this@Listener::onPlayerJoin)
            addListener(this@Listener::onPlayerQuit)
            addListener(this@Listener::onEasyPlayerLogin)
            addListener(this@Listener::onEasyPlayerLogout)
            addListener(this@Listener::onServerTick)
        }
    }

    private fun onPlayerJoin(event: PlayerLoggedInEvent) {
        if (event.player is ServerPlayer) {
            val serverPlayer = event.player as ServerPlayer
            //if can reload from cache
            if (loginService.reLogFromCache(serverPlayer)) {
                serverPlayer.sendMessage(TextComponent("already logged in!"), serverPlayer.getUUID())

                MinecraftForge.EVENT_BUS.post(
                    EasyLoginEvents.PlayerLoginEvent(
                        serverPlayer,
                        PlayerSessionCache.getSession(serverPlayer.getUUID())!!.account
                    )
                )

                return
            }

            if (accountService.hasAccount(serverPlayer.getUUID())) {
                TaskService.addTask(
                    TaskService.generateTaskIdentifier(serverPlayer.getUUID(), TaskService.Suffix.MESSAGE.name),
                    Message(serverPlayer, TextComponent("please login your account by /login"), 80, true)
                )
            } else {
                TaskService.addTask(
                    TaskService.generateTaskIdentifier(serverPlayer.getUUID(), TaskService.Suffix.MESSAGE.name),
                    Message(
                        serverPlayer,
                        TextComponent("please register your account by /register"),
                        80,
                        true
                    )
                )
            }

            //prepare for kicking out
            TaskService.addTask(
                TaskService.generateTaskIdentifier(serverPlayer.getUUID(), TaskService.Suffix.TIMEOUT.name),
                KickPlayer(serverPlayer, EasyLoginConfig.loginTimeoutTick.get())
            )
        }
    }


    private fun onEasyPlayerLogin(event: EasyLoginEvents.PlayerLoginEvent) {
        PlayerCache.addAccount(event.account)
        TaskService.cancelPlayer(event.serverPlayer.uuid)
    }

    private fun onEasyPlayerLogout(event: EasyLoginEvents.PlayerLogoutEvent) {
        PlayerCache.dropAccount(event.serverPlayer.uuid, true)
    }

    private fun onPlayerQuit(event: PlayerLoggedOutEvent) {
        if (event.player is ServerPlayer) {
            val serverPlayer = event.player as ServerPlayer
            loginService.logoutPlayer(serverPlayer)
            TaskService.cancelPlayer(serverPlayer.getUUID())
        }
    }

    private fun onServerTick(tickEvent: ServerTickEvent) {
        TaskService.tick()
    }
}