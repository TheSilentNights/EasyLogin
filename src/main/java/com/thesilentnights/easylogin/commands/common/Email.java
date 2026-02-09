package com.thesilentnights.easylogin.commands.common;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.thesilentnights.easylogin.service.EmailService;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class Email implements CommonCommands {

    private final EmailService emailService;

    public Email(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return Commands.literal("email")
                .then(Commands.literal("bind")
                        .then(Commands.argument("email", StringArgumentType.greedyString())
                                .executes(commandContext -> {
                                    if (emailService.bindEmail(commandContext)) {
                                        return 1;
                                    } else {
                                        return 0;
                                    }
                                })));
    }
}