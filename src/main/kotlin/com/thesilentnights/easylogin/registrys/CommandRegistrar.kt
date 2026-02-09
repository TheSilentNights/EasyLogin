package com.thesilentnights.easylogin.registrys

import com.thesilentnights.easylogin.commands.EasyLoginCommands
import com.thesilentnights.easylogin.utils.logInfo
import net.minecraftforge.event.RegisterCommandsEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
object CommandRegistrar {
    @SubscribeEvent
    @JvmStatic
    fun onRegister(event: RegisterCommandsEvent) {
        logInfo(CommandRegistrar::class, "Registering commands...")
        EasyLoginCommands.register(event.dispatcher)
    }
}
