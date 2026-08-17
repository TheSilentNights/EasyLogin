package cn.thesilentnights.easylogin.registrys;

import cn.thesilentnights.easylogin.commands.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.List;

public class CommandRegistrar {

        List<ICommands> commands = List.of(
                new Login(),
                new Registrar(),
                new ChangePassword(),
                new Management(),
                new ForceLogin()
        );

        public CommandRegistrar(IEventBus eventBus) {
                eventBus.register(this);
        }

        @SubscribeEvent
        public void onRegister(RegisterCommandsEvent event) {
                commands.forEach(command -> command.register(event.getDispatcher()));
        }
}
