package com.thesilentnights.easylogin.registrys;

import com.thesilentnights.easylogin.commands.EasyLoginCommands;
import com.thesilentnights.easylogin.utils.LogUtil;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CommandRegistrar {
    private final EasyLoginCommands commands;

    public CommandRegistrar(EasyLoginCommands commands, IEventBus eventBus) {
        this.commands = commands;
        eventBus.register(this);
    }

    @SubscribeEvent
    public void onRegister(RegisterCommandsEvent event) {
        LogUtil.logInfo(CommandRegistrar.class, "Registering commands...");
        commands.register(event.getDispatcher());
    }
}