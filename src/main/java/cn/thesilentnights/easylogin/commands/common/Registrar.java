package cn.thesilentnights.easylogin.commands.common;

import cn.thesilentnights.easylogin.commands.ICommands;
import cn.thesilentnights.easylogin.services.auth.LoginService;
import cn.thesilentnights.easylogin.utils.Dependencies;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class Registrar implements ICommands {

        @Override
        public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
                var register = Commands.literal("register");
                var password = Commands.argument("password", StringArgumentType.string());
                var repeat = Commands.argument("repeat", StringArgumentType.string());

                repeat.executes(
                        (CommandContext<CommandSourceStack> context) -> Dependencies.getDependency(LoginService.class).register(context) ? 1 : 0
                );

                //serialize
                dispatcher.register(register.then(password.then(repeat)));
        }
}
