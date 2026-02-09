package com.thesilentnights.easylogin.commands.common;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.thesilentnights.easylogin.service.ChangePasswordService;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class ChangePassword implements CommonCommands {


    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return Commands.literal("changepassword")
                .then(Commands.argument("newPassword", StringArgumentType.string())
                        .then(Commands.argument("newPasswordConfirm", StringArgumentType.string())
                                .executes(context -> {
                                    boolean success = ChangePasswordService.changePassword(context);
                                    return success ? 1 : 0;
                                })));
    }
}