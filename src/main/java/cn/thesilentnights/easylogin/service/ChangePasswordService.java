package cn.thesilentnights.easylogin.service;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import cn.thesilentnights.easylogin.pojo.PlayerPasswordData;
import cn.thesilentnights.easylogin.repo.PlayerCache;
import cn.thesilentnights.easylogin.utils.LogUtil;
import cn.thesilentnights.easylogin.utils.MessageSender;
import cn.thesilentnights.easylogin.utils.MessageSender.MessageType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.server.players.NameAndId;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public class ChangePasswordService {

    public static boolean changePassword(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        if (!LoginService.isLoggedIn(context.getSource().getPlayerOrException().getUUID())) {
            MessageSender.sendMessage(context, "commands.password.change.failure.unlogged", MessageType.ERROR);
            return false;
        }

        String newPassword = StringArgumentType.getString(context, "newPassword");
        String newPasswordConfirm = StringArgumentType.getString(context, "newPasswordConfirm");

        if (newPassword.equals(newPasswordConfirm)) {
            DataService.updatePassword(context.getSource().getPlayerOrException().getUUID(), newPassword);
            updateCache(context.getSource().getPlayerOrException().getUUID());
            MessageSender.sendMessage(context, "commands.password.change.success", MessageType.SUCCESS);

            return true;
        } else {

            MessageSender.sendMessage(context, "commands.password.confirm.failure", MessageType.ERROR);
            return false;

        }
    }

    public static boolean changePasswordAdmin(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<NameAndId> player = GameProfileArgument.getGameProfiles(context, "player");

        if (player.isEmpty()) {
            MessageSender.sendMessage(context, "player not found", MessageType.ERROR);
            return false;
        }

        NameAndId next = player.iterator().next();

        String newPassword = StringArgumentType.getString(context, "password");
        String newPasswordConfirm = StringArgumentType.getString(context, "confirm");
        if (newPassword.equals(newPasswordConfirm)) {
            DataService.updatePassword(next.id(), newPassword);
            updateCache(next.id());

            MessageSender.sendMessage(context, "commands.password.change.success", MessageType.SUCCESS);
            return true;
        } else {
            MessageSender.sendMessage(context, "commands.password.confirm.failure", MessageType.ERROR);
            return false;
        }
    }

    private static void updateCache(UUID id) {
        Optional<PlayerPasswordData> account = DataService.getPlayerPasswordData(id);
        if (account.isPresent()) {
            PlayerCache.addPlayer(id);
        } else {
            LogUtil.getLogger().error("Error updating cache", new SQLException("Error updating cache"));
        }
    }

}