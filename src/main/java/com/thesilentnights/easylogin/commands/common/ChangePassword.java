package com.thesilentnights.easylogin.commands.common;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.thesilentnights.easylogin.service.ChangePasswordService;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.springframework.stereotype.Component;

@Component
public class ChangePassword implements CommonCommands {

    private final ChangePasswordService changePasswordService;

    public ChangePassword(ChangePasswordService changePasswordService) {
        this.changePasswordService = changePasswordService;
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return Commands.literal("changepassword")
                .then(Commands.argument("newPassword", StringArgumentType.string())
                        .then(Commands.argument("newPasswordConfirm", StringArgumentType.string())
                                .executes(context -> {
                                    boolean success = changePasswordService.changePassword(context);
                                    return success ? 1 : 0;
                                })));
    }
}