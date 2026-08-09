package cn.thesilentnights.easylogin.commands;

import cn.thesilentnights.easylogin.services.ChangePasswordService;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;

public class ChangePassword extends PermissionRequired implements ICommands {

        @Override
        public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
                var changepassword = Commands.literal("changepassword").requires(this::requireLoginAuth);
                var newPassword = Commands.argument("newPassword", StringArgumentType.string());
                var confirm = Commands.argument("confirm", StringArgumentType.string());

                confirm.executes(
                        (CommandContext<CommandSourceStack> context) -> ChangePasswordService.changePassword(context)
                                ? 1
                                : 0
                );

                dispatcher.register(changepassword.then(newPassword.then(confirm)));
        }
}
