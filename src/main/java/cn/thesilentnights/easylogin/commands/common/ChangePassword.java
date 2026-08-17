package cn.thesilentnights.easylogin.commands.common;

import cn.thesilentnights.easylogin.commands.ICommands;
import cn.thesilentnights.easylogin.commands.PermissionRequired;
import cn.thesilentnights.easylogin.services.passwords.ChangePasswordService;
import cn.thesilentnights.easylogin.utils.Dependencies;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class ChangePassword extends PermissionRequired implements ICommands {

        @Override
        public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
                var changepassword = Commands.literal("changepassword").requires(this::requireLoginAuth);
                var newPassword = Commands.argument("newPassword", StringArgumentType.string());
                var confirm = Commands.argument("newPasswordConfirm", StringArgumentType.string());



                dispatcher.register(changepassword.then(newPassword.then(confirm.executes(
                        (var context) -> Dependencies.getDependency(ChangePasswordService.class).changePassword(context)
                                ? 1
                                : 0
                ))));
        }
}
