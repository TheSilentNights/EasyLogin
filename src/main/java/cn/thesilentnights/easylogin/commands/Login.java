package cn.thesilentnights.easylogin.commands;

import cn.thesilentnights.easylogin.service.LoginService;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;

public class Login implements ICommands {

        @Override
        public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
                var login = Commands.literal("login");
                var password = Commands.argument("password", StringArgumentType.string());

                password.executes(
                        (CommandContext<CommandSourceStack> context) -> LoginService.login(context)
                                ? 1
                                : 0
                );

                dispatcher.register(login.then(password));
        }
}
