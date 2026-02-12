package com.thesilentnights.easylogin.service;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.thesilentnights.easylogin.pojo.PlayerAccount;
import com.thesilentnights.easylogin.repo.PlayerCache;
import com.thesilentnights.easylogin.utils.LogUtil;
import com.thesilentnights.easylogin.utils.TextUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.network.chat.TranslatableComponent;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public class ChangePasswordService {


    public static boolean changePassword(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        if (!LoginService.isLoggedIn(context.getSource().getPlayerOrException().getUUID())) {
            context.getSource().sendFailure(TextUtil.serialize(TextUtil.FormatType.FAILURE, new TranslatableComponent("commands.password.change.failure.unlogged")));
            return false;
        }

        String newPassword = StringArgumentType.getString(context, "newPassword");
        String newPasswordConfirm = StringArgumentType.getString(context, "newPasswordConfirm");

        if (newPassword.equals(newPasswordConfirm)) {
            AccountService.updatePassword(
                    newPassword,
                    context.getSource().getPlayerOrException().getUUID()
            );
            updateCache(context.getSource().getPlayerOrException().getUUID());
            context.getSource().sendSuccess(
                    TextUtil.serialize(
                            TextUtil.FormatType.SUCCESS,
                            new TranslatableComponent("commands.password.change.success")
                    ), true
            );

            return true;
        } else {

            context.getSource().sendFailure(TextUtil.serialize(TextUtil.FormatType.FAILURE, new TranslatableComponent("commands.password.confirm.failure")));
            return false;
        }
    }

    public static boolean changePasswordAdmin(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<GameProfile> player = GameProfileArgument.getGameProfiles(context, "player");
        GameProfile next = player.iterator().next();

        String newPassword = StringArgumentType.getString(context, "password");
        String newPasswordConfirm = StringArgumentType.getString(context, "confirm");
        if (newPassword.equals(newPasswordConfirm)) {
            AccountService.updatePassword(
                    newPassword,
                    next.getId()
            );
            updateCache(next.getId());

            context.getSource().sendSuccess(
                    TextUtil.serialize(
                            TextUtil.FormatType.SUCCESS,
                            new TranslatableComponent("commands.password.change.success")
                    )
                    , true
            );
            return true;
        } else {
            context.getSource().sendFailure(TextUtil.serialize(TextUtil.FormatType.FAILURE, new TranslatableComponent("commands.password.confirm.failure")));
            return false;
        }
    }

    private static void updateCache(UUID uuid) {
        Optional<PlayerAccount> account = AccountService.getAccount(uuid);
        if (account.isPresent()) {
            PlayerCache.addAccount(account.get());
        } else {
            LogUtil.logError(ChangePasswordService.class, "Error updating cache", new SQLException("Error updating cache"));
        }
    }

}