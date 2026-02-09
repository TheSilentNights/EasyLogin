package com.thesilentnights.easylogin.service

import com.thesilentnights.easylogin.configs.EasyLoginConfig
import com.thesilentnights.easylogin.service.task.KickPlayer
import com.thesilentnights.easylogin.service.task.Message
import net.minecraft.network.chat.TextComponent
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects

class PreLoginService(val accountService: AccountService, val loginService: LoginService) {
    fun preLogin(serverPlayer: ServerPlayer) {
        if (NPCService.isNPC(serverPlayer)) {
            ByPassService.addBypass(serverPlayer.uuid)
        }

        //if can reload from cache
        if (loginService.reLogFromCache(serverPlayer)) {
            serverPlayer.sendMessage(TextComponent("relogged from cache"), serverPlayer.getUUID())
            return
        }

        //put effects on player
        addBlindEffectToPlayer(serverPlayer)

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

    private fun addBlindEffectToPlayer(serverPlayer: ServerPlayer) {
        serverPlayer.addEffect(
            MobEffectInstance(
                MobEffects.BLINDNESS,
                25565,
                10,
                false,
                false
            )
        )
    }
}