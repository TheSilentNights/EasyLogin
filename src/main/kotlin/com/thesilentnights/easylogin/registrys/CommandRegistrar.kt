package com.thesilentnights.easylogin.registrys

import com.thesilentnights.easylogin.commands.EasyLoginCommands
import net.minecraftforge.event.RegisterCommandsEvent


object CommandRegistrar {

    fun onRegister(event: RegisterCommandsEvent) {
        EasyLoginCommands.register(event.dispatcher)
    }
}
