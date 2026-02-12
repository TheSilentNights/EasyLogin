package com.thesilentnights.easylogin.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.thesilentnights.easylogin.service.ChangePasswordService;
import com.thesilentnights.easylogin.service.PlayerInfoService;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.GameProfileArgument;

import java.util.Locale;

public class Management extends PermissionRequired implements ICommands {

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> node = Commands.literal("easylogin").requires(sourceStack -> requireAdminPermission(sourceStack) && requireAdminPermission(sourceStack));

        node.then(Commands.literal("playerInfo")
                .then(Commands.argument("player", GameProfileArgument.gameProfile())
                        .executes(commandContext -> {
                            return PlayerInfoService.handle(commandContext) ? 1 : 0;
                        })));

        node.then(Commands.literal("changePassword".toLowerCase(Locale.CHINA))
                .then(Commands.argument("player", GameProfileArgument.gameProfile())
                        .then(Commands.argument("password", StringArgumentType.string())
                                .then(Commands.argument("confirm", StringArgumentType.string())
                                        .executes(commandContext -> {
                                            return ChangePasswordService.changePasswordAdmin(commandContext) ? 1 : 0;
                                        })))));

        dispatcher.register(node);
    }
}
