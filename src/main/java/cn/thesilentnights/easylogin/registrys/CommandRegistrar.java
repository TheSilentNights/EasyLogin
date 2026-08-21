package cn.thesilentnights.easylogin.registrys;

import cn.thesilentnights.easylogin.commands.ICommands;
import cn.thesilentnights.easylogin.commands.admin.ChangePasswordAdmin;
import cn.thesilentnights.easylogin.commands.admin.ForceLogin;
import cn.thesilentnights.easylogin.commands.admin.PlayerInfo;
import cn.thesilentnights.easylogin.commands.common.ChangePassword;
import cn.thesilentnights.easylogin.commands.common.Login;
import cn.thesilentnights.easylogin.commands.common.Registrar;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.List;

public class CommandRegistrar {

        List<ICommands> commands = List.of(
                new Login(),
                new Registrar(),
                new ChangePassword(),
                new ForceLogin(),
                new PlayerInfo(),
                new ChangePasswordAdmin()
        );

        public CommandRegistrar(IEventBus eventBus) {
                eventBus.register(this);
        }

        @SubscribeEvent
        public void onRegister(RegisterCommandsEvent event) {
                commands.forEach(command -> command.register(event.getDispatcher()));
        }
}
