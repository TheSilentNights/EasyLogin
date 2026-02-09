package com.thesilentnights.easylogin.commands.common;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.thesilentnights.easylogin.service.PasswordRecoveryService;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class Recover implements CommonCommands {

    private final PasswordRecoveryService passwordRecoveryService;

    public Recover(PasswordRecoveryService passwordRecoveryService) {
        this.passwordRecoveryService = passwordRecoveryService;
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return Commands.literal("recover")
                .then(Commands.literal("send")
                        .then(Commands.argument("emailConfirm", StringArgumentType.greedyString())
                                .executes((CommandContext<CommandSourceStack> context) ->
                                        passwordRecoveryService.recoveryPassword(context) ? 1 : 0
                                )
                        )
                )
                .then(Commands.literal("check")
                        .then(Commands.argument("confirmCode", StringArgumentType.greedyString())
                                .executes((CommandContext<CommandSourceStack> context) ->
                                        passwordRecoveryService.confirmRecover(context) ? 1 : 0
                                )
                        )
                );
    }
}