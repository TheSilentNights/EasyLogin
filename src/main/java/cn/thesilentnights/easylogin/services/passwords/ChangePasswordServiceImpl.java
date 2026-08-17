package cn.thesilentnights.easylogin.services.passwords;

import cn.thesilentnights.easylogin.pojo.PlayerPasswordData;
import cn.thesilentnights.easylogin.repo.PlayerCache;
import cn.thesilentnights.easylogin.services.auth.LoginService;
import cn.thesilentnights.easylogin.services.data.DataService;
import cn.thesilentnights.easylogin.utils.MessageSender;
import cn.thesilentnights.easylogin.utils.MessageSender.MessageType;
import cn.thesilentnights.easylogin.utils.PasswordHasher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.logging.LogUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.server.players.NameAndId;
import org.slf4j.Logger;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public class ChangePasswordServiceImpl implements ChangePasswordService {

        private final DataService dataService;
        private final LoginService loginService;

        public ChangePasswordServiceImpl(DataService dataService, LoginService loginService) {
                this.dataService = dataService;
                this.loginService = loginService;
        }

        @Override
        public boolean changePassword(
                CommandContext<CommandSourceStack> context
        ) throws CommandSyntaxException {
                UUID uuid = context.getSource().getPlayerOrException().getUUID();
                if (!loginService.isPlayerLogged(uuid)){
                        return false;
                }

                if (!loginService.isPlayerLogged(uuid)) {
                        MessageSender.sendMessage(
                                context,
                                "commands.password.change.failure.unlogged",
                                MessageType.ERROR
                        );
                        return false;
                }

                String newPassword = StringArgumentType.getString(context, "newPassword");
                String newPasswordConfirm = StringArgumentType.getString(context, "newPasswordConfirm");

                if (newPassword.equals(newPasswordConfirm)) {
                        dataService.updatePassword(
                                context.getSource().getPlayerOrException().getUUID(),
                                newPassword
                        );
                        MessageSender.sendMessage(
                                context,
                                "commands.password.change.success",
                                MessageType.SUCCESS
                        );

                        return true;
                } else {

                        MessageSender.sendMessage(
                                context,
                                "commands.password.confirm.failure",
                                MessageType.ERROR
                        );
                        return false;

                }
        }


        @Override
        public boolean changePasswordAdmin(
                CommandContext<CommandSourceStack> context
        ) throws CommandSyntaxException {

                Collection<NameAndId> player = GameProfileArgument.getGameProfiles(context, "player");

                if (player.isEmpty()) {
                        MessageSender.sendMessage(context, "player not found", MessageType.ERROR);
                        return false;
                }

                NameAndId next = player.iterator().next();

                String newPassword = StringArgumentType.getString(context, "password");
                String newPasswordConfirm = StringArgumentType.getString(context, "confirm");
                if (newPassword.equals(newPasswordConfirm)) {
                        dataService.updatePassword(next.id(), newPassword);

                        MessageSender.sendMessage(
                                context,
                                "commands.password.change.success",
                                MessageType.SUCCESS
                        );
                        return true;
                } else {
                        MessageSender.sendMessage(
                                context,
                                "commands.password.confirm.failure",
                                MessageType.ERROR
                        );
                        return false;
                }
        }




}
