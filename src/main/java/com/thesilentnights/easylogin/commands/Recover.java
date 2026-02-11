package com.thesilentnights.easylogin.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.thesilentnights.easylogin.service.PasswordRecoveryService;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class Recover implements ICommands {


    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("recover")
                        .then(Commands.literal("send")
                                .then(Commands.argument("emailConfirm", StringArgumentType.greedyString())
                                        .executes((CommandContext<CommandSourceStack> context) ->
                                                PasswordRecoveryService.recoveryPassword(context) ? 1 : 0
                                        )
                                )
                        )
                        .then(Commands.literal("check")
                                .then(Commands.argument("confirmCode", StringArgumentType.greedyString())
                                        .executes((CommandContext<CommandSourceStack> context) ->
                                                PasswordRecoveryService.confirmRecover(context) ? 1 : 0
                                        )
                                )
                        ));
    }
}