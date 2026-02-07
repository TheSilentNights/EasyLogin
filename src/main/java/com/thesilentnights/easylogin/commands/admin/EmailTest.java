package com.thesilentnights.easylogin.commands.admin;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.thesilentnights.easylogin.service.LoginService;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class EmailTest extends AdminCommands {

    public EmailTest(LoginService loginService) {
        super(loginService);
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return super.MAIN_NODE.then(
                Commands.literal("email")
                        .then(
                                Commands.literal("test")
                                        .then(
                                                Commands.argument("targetEmail", StringArgumentType.greedyString())
                                                        .executes((CommandContext<CommandSourceStack> context) -> 1)
                                        )
                        )
        );
    }
}