package com.thesilentnights.easylogin.service;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.thesilentnights.easylogin.pojo.SqlColumnDefinition;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;

public class ChangePasswordService {

    private final LoginService loginService;
    private final AccountService accountService;

    public ChangePasswordService(LoginService loginService, AccountService accountService) {
        this.loginService = loginService;
        this.accountService = accountService;
    }

    public boolean changePassword(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        if (!loginService.isLoggedIn(context.getSource().getPlayerOrException().getUUID())) {
            MutableComponent message = new TranslatableComponent("commands.password.change.failure.unlogged")
                    .withStyle(ChatFormatting.BOLD)
                    .withStyle(ChatFormatting.RED);
            context.getSource().sendFailure(message);
            return false;
        }

        String newPassword = StringArgumentType.getString(context, "newPassword");
        String newPasswordConfirm = StringArgumentType.getString(context, "newPasswordConfirm");

        if (newPassword.equals(newPasswordConfirm)) {
            accountService.updateSingleColumn(
                    SqlColumnDefinition.PASSWORD,
                    newPassword,
                    context.getSource().getPlayerOrException().getUUID()
            );
            MutableComponent successMessage = new TranslatableComponent("commands.password.change.success")
                    .withStyle(ChatFormatting.BOLD)
                    .withStyle(ChatFormatting.GREEN);
            context.getSource().sendSuccess(successMessage, true);
            return true;
        } else {
            MutableComponent failureMessage = new TranslatableComponent("commands.password.confirm.failure")
                    .withStyle(ChatFormatting.BOLD)
                    .withStyle(ChatFormatting.RED);
            context.getSource().sendFailure(failureMessage);
            return false;
        }
    }
}