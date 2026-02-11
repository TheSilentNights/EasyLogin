package com.thesilentnights.easylogin.registrys;

import com.thesilentnights.easylogin.commands.*;
import com.thesilentnights.easylogin.commands.Email;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class CommandRegistrar {

    List<ICommands> commands = List.of(
            new Login(),
            new Registrar(),
            new ChangePassword(),
            new Recover(),
            new Email()
    );

    public CommandRegistrar(IEventBus eventBus) {
        eventBus.register(this);
    }

    @SubscribeEvent
    public void onRegister(RegisterCommandsEvent event) {
        commands.forEach(command -> command.register(event.getDispatcher()));
    }
}