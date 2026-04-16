package cn.thesilentnights.easylogin.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import cn.thesilentnights.easylogin.service.ChangePasswordService;
import cn.thesilentnights.easylogin.service.PlayerInfoService;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.GameProfileArgument;

import java.util.Locale;

public class Management extends PermissionRequired implements ICommands {

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> easylogin = Commands.literal("easylogin")
                .requires(this::requireAdminPermission);

        var playerInfo = Commands.literal("playerInfo");
        var playerArg = Commands.argument("player", GameProfileArgument.gameProfile());
        var changePassword = Commands.literal("changePassword".toLowerCase(Locale.ROOT));
        var passwordArg = Commands.argument("password", StringArgumentType.string());
        var confirmArg = Commands.argument("confirm", StringArgumentType.string());

        playerArg.executes(
                (CommandContext<CommandSourceStack> context) -> PlayerInfoService.handle(context)
                        ? 1
                        : 0
        );

        confirmArg.executes(
                (CommandContext<CommandSourceStack> context) -> ChangePasswordService.changePasswordAdmin(context)
                        ? 1
                        : 0
        );

        dispatcher.register(easylogin.then(playerInfo.then(playerArg)));
        dispatcher.register(easylogin.then(changePassword.then(playerArg.then(passwordArg.then(confirmArg)))));
    }
}