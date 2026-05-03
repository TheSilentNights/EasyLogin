package cn.thesilentnights.easylogin.registrys;

import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

import cn.thesilentnights.easylogin.commands.*;

public class CommandRegistrar {

    List<ICommands> commands = List.of(
            new Login(),
            new Registrar(),
            new ChangePassword(),
            new Management()
    );

    public CommandRegistrar(IEventBus eventBus) {
        eventBus.register(this);
    }

    @SubscribeEvent
    public void onRegister(RegisterCommandsEvent event) {
        commands.forEach(command -> command.register(event.getDispatcher()));
    }
}