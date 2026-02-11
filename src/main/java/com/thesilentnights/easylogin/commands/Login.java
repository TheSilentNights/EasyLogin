package com.thesilentnights.easylogin.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.thesilentnights.easylogin.service.LoginService;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class Login implements ICommands {

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("login")
                        .then(Commands.argument("password", StringArgumentType.string())
                                .executes(context -> {
                                            boolean success = LoginService.login(context);
                                            return success ? 1 : 0;
                                        }
                                )
                        )
        );
    }
}