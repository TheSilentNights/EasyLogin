package com.thesilentnights.easylogin.registrys

import com.thesilentnights.easylogin.commands.EasyLoginCommands
import net.minecraftforge.event.RegisterCommandsEvent
import org.koin.core.component.KoinComponent


class CommandRegistrar(val easyLoginCommands: EasyLoginCommands): KoinComponent {
    fun onRegister(event: RegisterCommandsEvent) {
        easyLoginCommands.register(event.dispatcher)
    }
}
