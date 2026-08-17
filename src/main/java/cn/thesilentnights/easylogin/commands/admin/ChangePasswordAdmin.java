package cn.thesilentnights.easylogin.commands.admin;

import cn.thesilentnights.easylogin.commands.AdminCommands;
import cn.thesilentnights.easylogin.services.passwords.ChangePasswordService;
import cn.thesilentnights.easylogin.utils.Dependencies;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.GameProfileArgument;

import java.util.Locale;

public class ChangePasswordAdmin extends AdminCommands {
        @Override
        protected LiteralArgumentBuilder<CommandSourceStack> registerAsAdmin(LiteralArgumentBuilder<CommandSourceStack> mainNode) {
                var playerArgChangePassword = Commands.argument("player", GameProfileArgument.gameProfile());
                //changePassword已经被普通命令注册了，这里改为setPassword
                var changePassword = Commands.literal("setPassword".toLowerCase(Locale.ROOT));
                var passwordArg = Commands.argument("password", StringArgumentType.string());
                var confirmArg = Commands.argument("confirm", StringArgumentType.string());

                confirmArg.executes(
                        (CommandContext<CommandSourceStack> context) ->
                                Dependencies.getDependency(ChangePasswordService.class).changePasswordAdmin(context) ? 1 : 0
                );


                return mainNode.then(changePassword.then(playerArgChangePassword.then(passwordArg.then(confirmArg))));
        }
}
