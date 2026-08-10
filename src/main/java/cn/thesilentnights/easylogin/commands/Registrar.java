package cn.thesilentnights.easylogin.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;

import cn.thesilentnights.easylogin.services.auth.LoginService;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;

public class Registrar implements ICommands {

        @Override
        public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
                var register = Commands.literal("register");
                var password = Commands.argument("password", StringArgumentType.string());
                var repeat = Commands.argument("repeat", StringArgumentType.string());

                repeat.executes(
                        (CommandContext<CommandSourceStack> context) -> LoginService.register(context) ? 1 : 0
                );

                //serialize
                dispatcher.register(register.then(password.then(repeat)));
        }
}
