package com.thesilentnights.easylogin.registrys

import com.thesilentnights.easylogin.commands.EasyLoginCommands
import net.minecraftforge.event.RegisterCommandsEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
class CommandRegistrar {
    @SubscribeEvent
    fun onRegister(event: RegisterCommandsEvent) {
        EasyLoginCommands.register(event.dispatcher)

    }
}
