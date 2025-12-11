package com.thesilentnights.service;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.TranslatableComponent;

public class ChangePasswordService {
    public static boolean changePassword(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        if (!LoginService.isLoggedIn(context.getSource().getPlayerOrException().getUUID())){
            context.getSource().sendFailure(new TranslatableComponent("commands.password.change.failure.unlogged").withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.RED));
            return false;
        }

        String newPassword = StringArgumentType.getString(context, "newPassword");
        String newPasswordConfirm = StringArgumentType.getString(context, "newPasswordConfirm");
        if (newPassword.equals(newPasswordConfirm)){
            AccountService.getAccount(context.getSource().getPlayerOrException().getUUID()).ifPresent(playerAccount -> {
                playerAccount.setPassword(newPassword);
                AccountService.updateAccount(playerAccount);
            });
            return true;
        }else{
            context.getSource().sendFailure(new TranslatableComponent("commands.password.confirm.failure").withStyle(ChatFormatting.RED).withStyle(ChatFormatting.BOLD));
        }
        return false;
    }
}
