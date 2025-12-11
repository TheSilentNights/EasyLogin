package com.thesilentnights.commands.common;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.thesilentnights.service.ChangePasswordService;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class ChangePassword implements CommonCommands {
    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return Commands.literal("changepassword")
                .then(Commands.argument("newPassword", StringArgumentType.greedyString())
                        .then(Commands.argument("newPasswordConfirm", StringArgumentType.greedyString())
                                .executes(context -> ChangePasswordService.changePassword(context) ? 1 : 0)));
    }
}
