package com.thesilentnights.easylogin.registrys;

import com.thesilentnights.easylogin.commands.EasyLoginCommands;
import com.thesilentnights.easylogin.utils.LogUtil;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.springframework.stereotype.Component;

@Component
public class CommandRegistrar {
    private EasyLoginCommands commands;

    public CommandRegistrar(EasyLoginCommands commands) {
        this.commands = commands;
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRegister(RegisterCommandsEvent event) {
        LogUtil.logInfo(CommandRegistrar.class, "Registering commands...");
        commands.register(event.getDispatcher());
    }
}