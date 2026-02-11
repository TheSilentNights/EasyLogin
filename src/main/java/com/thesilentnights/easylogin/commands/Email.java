package com.thesilentnights.easylogin.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.thesilentnights.easylogin.service.EmailService;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class Email extends PermissionRequired implements ICommands {

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("email").requires(this::requireLoginAuth)
                        .then(Commands.literal("bind")
                                .then(Commands.argument("email", StringArgumentType.greedyString())
                                        .executes(commandContext -> {
                                            if (EmailService.bindEmail(commandContext)) {
                                                return 1;
                                            } else {
                                                return 0;
                                            }
                                        }))));
    }
}