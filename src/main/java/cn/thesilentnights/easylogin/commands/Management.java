package cn.thesilentnights.easylogin.commands;

import cn.thesilentnights.easylogin.services.commands.PlayerInfoService;
import cn.thesilentnights.easylogin.services.passwords.ChangePasswordService;
import cn.thesilentnights.easylogin.utils.Dependencies;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import java.util.Locale;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;

public class Management extends PermissionRequired implements ICommands {

        @Override
        public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
                LiteralArgumentBuilder<CommandSourceStack> easylogin = Commands.literal("easylogin")
                        .requires(
                                (source) ->
                                        requireAdminPermission(source) && requireLoginAuth(source)
                        );

                var playerInfo = Commands.literal("playerInfo");
                var playerArgInfo = Commands.argument("player", GameProfileArgument.gameProfile());


                playerArgInfo.executes(
                        (CommandContext<CommandSourceStack> context) -> Dependencies.getDependency(PlayerInfoService.class).handle(context)
                                ? 1
                                : 0
                );

                dispatcher.register(easylogin.then(playerInfo.then(playerArgInfo)));


                var playerArgChangePassword = Commands.argument("player", GameProfileArgument.gameProfile());
                var changePassword = Commands.literal("changePassword".toLowerCase(Locale.ROOT));
                var passwordArg = Commands.argument("password", StringArgumentType.string());
                var confirmArg = Commands.argument("confirm", StringArgumentType.string());

                confirmArg.executes(
                        (CommandContext<CommandSourceStack> context) ->
                                Dependencies.getDependency(ChangePasswordService.class).changePasswordAdmin(context) ? 1 : 0
                );

                dispatcher.register(
                        easylogin.then(
                                changePassword.then(
                                        playerArgChangePassword.then(
                                                passwordArg.then(confirmArg)
                                        )
                                )
                        )
                );
        }
}
