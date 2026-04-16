package cn.thesilentnights.easylogin.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;

import cn.thesilentnights.easylogin.service.ChangePasswordService;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class ChangePassword implements ICommands {

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        var changepassword = Commands.literal("changepassword");
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