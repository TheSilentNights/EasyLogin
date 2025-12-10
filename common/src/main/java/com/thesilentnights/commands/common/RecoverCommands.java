package com.thesilentnights.commands.common;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.thesilentnights.service.PasswordRecoveryService;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class RecoverCommands implements CommonCommands {
    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return Commands.literal("recover")
                .then(Commands.argument("emailConfirm", StringArgumentType.greedyString())
                        .executes(context -> PasswordRecoveryService.recoveryPassword(context) ? 1 : 0));
    }
}
