package com.thesilentnights.easylogin.commands.admin;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.thesilentnights.easylogin.service.LoginService;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class ByPass extends AdminCommands {

    public ByPass(LoginService loginService) {
        super(loginService);
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return super.MAIN_NODE.then(
                Commands.literal("bypass").then(
                        Commands.literal("add").then(
                                Commands.argument("target", StringArgumentType.greedyString())
                                        .executes((CommandContext<CommandSourceStack> context) -> 1)
                        )
                )
        );
    }
}