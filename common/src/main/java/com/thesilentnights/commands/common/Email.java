package com.thesilentnights.commands.common;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.thesilentnights.service.EmailService;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class Email implements CommonCommands {
    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return Commands.literal("email")
                .then(Commands.literal("bind")
                        .then(Commands.argument("email", StringArgumentType.string())
                                .then(Commands.argument("emailConfirm", StringArgumentType.string())
                                        .executes(commandContext -> EmailService.bindEmail(commandContext)?1:0))));
    }
}
