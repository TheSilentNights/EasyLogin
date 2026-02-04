package com.thesilentnights.easylogin.registrys

import com.thesilentnights.easylogin.commands.EasyLoginCommands
import com.thesilentnights.easylogin.utils.logInfo
import net.minecraftforge.event.RegisterCommandsEvent
import net.minecraftforge.eventbus.api.SubscribeEvent


class CommandRegistrar(private val easyLoginCommands: EasyLoginCommands) {
    @SubscribeEvent
    fun onRegister(event: RegisterCommandsEvent) {
        logInfo(CommandRegistrar::class, "Registering commands...")
        easyLoginCommands.register(event.dispatcher)
    }
}
