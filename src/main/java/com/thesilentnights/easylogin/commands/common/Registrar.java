package com.thesilentnights.easylogin.commands.common;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.thesilentnights.easylogin.service.LoginService;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class Registrar implements CommonCommands {

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return Commands.literal("register")
                .then(
                        Commands.argument("password", StringArgumentType.string())
                                .then(
                                        Commands.argument("repeat", StringArgumentType.string())
                                                .executes((CommandContext<CommandSourceStack> context) ->
                                                        LoginService.register(context) ? 1 : 0
                                                )
                                )
                );
    }
}