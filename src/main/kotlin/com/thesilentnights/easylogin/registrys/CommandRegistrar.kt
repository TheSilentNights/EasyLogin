package com.thesilentnights.easylogin.registrys

import com.thesilentnights.easylogin.commands.EasyLoginCommands
import net.minecraftforge.event.RegisterCommandsEvent

fun registerCommands(event: RegisterCommandsEvent) {
    EasyLoginCommands.register(event.dispatcher)
}
