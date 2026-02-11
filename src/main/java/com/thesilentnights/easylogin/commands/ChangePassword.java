package com.thesilentnights.easylogin.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.thesilentnights.easylogin.service.ChangePasswordService;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class ChangePassword implements ICommands {


    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("changepassword")
                        .then(Commands.argument("newPassword", StringArgumentType.string())
                                .then(Commands.argument("newPasswordConfirm", StringArgumentType.string())
                                        .executes(context -> {
                                            boolean success = ChangePasswordService.changePassword(context);
                                            return success ? 1 : 0;
                                        }))));
    }
}