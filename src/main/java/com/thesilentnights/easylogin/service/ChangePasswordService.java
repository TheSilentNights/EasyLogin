package com.thesilentnights.easylogin.service;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.thesilentnights.easylogin.pojo.SqlColumnDefinition;
import com.thesilentnights.easylogin.utils.TextUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.Collection;

public class ChangePasswordService {


    public static boolean changePassword(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        if (!LoginService.isLoggedIn(context.getSource().getPlayerOrException().getUUID())) {
            context.getSource().sendFailure(TextUtil.serialize(TextUtil.FormatType.FAILURE, new TranslatableComponent("commands.password.change.failure.unlogged")));
            return false;
        }

        String newPassword = StringArgumentType.getString(context, "newPassword");
        String newPasswordConfirm = StringArgumentType.getString(context, "newPasswordConfirm");

        if (newPassword.equals(newPasswordConfirm)) {
            AccountService.updateSingleColumn(
                    SqlColumnDefinition.PASSWORD,
                    newPassword,
                    context.getSource().getPlayerOrException().getUUID()
            );
            context.getSource().sendSuccess(
                    TextUtil.serialize(
                            TextUtil.FormatType.SUCCESS,
                            new TranslatableComponent("commands.password.change.success")
                    ), true
            );

            return true;
        } else {
            MutableComponent failureMessage = new TranslatableComponent("commands.password.confirm.failure")
                    .withStyle(ChatFormatting.BOLD)
                    .withStyle(ChatFormatting.RED);
            context.getSource().sendFailure(failureMessage);
            return false;
        }
    }

    public static boolean changePasswordAdmin(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<GameProfile> player = GameProfileArgument.getGameProfiles(context, "player");
        GameProfile next = player.iterator().next();

        String newPassword = StringArgumentType.getString(context, "password");
        String newPasswordConfirm = StringArgumentType.getString(context, "confirm");
        if (newPassword.equals(newPasswordConfirm)) {
            AccountService.updateSingleColumn(
                    SqlColumnDefinition.PASSWORD,
                    newPassword,
                    next.getId()
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