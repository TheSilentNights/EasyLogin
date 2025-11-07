package com.thesilentnights.commands.common;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.thesilentnights.service.PlayerLoginAuth;
import com.thesilentnights.task.TickTimerManager;
import com.thesilentnights.utils.TextUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class RegistrarCommands implements CommonCommands {

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return Commands.literal("register")
                .then(Commands.argument("password", StringArgumentType.string())
                        .then(Commands.argument("repeatPassword", StringArgumentType.string())
                                .executes(context -> {
                                    String password = StringArgumentType.getString(context, "password");
                                    String repeatPassword = StringArgumentType.getString(context, "repeatPassword");
                                    if (password.equals(repeatPassword)) {
                                        if (PlayerLoginAuth.hasAccount(context.getSource().getPlayerOrException().getGameProfile().getName())){
                                            context.getSource().getPlayerOrException().sendMessage(TextUtil.createText(ChatFormatting.RED, "the account has already been registered"), context.getSource().getPlayerOrException().getUUID());
                                            return 0;
                                        }
                                        PlayerLoginAuth.registerPlayer(context.getSource().getPlayerOrException(), password);
                                        context.getSource().getPlayerOrException().sendMessage(TextUtil.createText(ChatFormatting.GREEN, "register success"), context.getSource().getPlayerOrException().getUUID());
                                        TickTimerManager.cancel(context.getSource().getPlayerOrException().getUUID());
                                        return 1;
                                    }
                                    context.getSource().getPlayerOrException().sendMessage(TextUtil.createText(ChatFormatting.RED, "password dose not match"), context.getSource().getPlayerOrException().getUUID());
                                    return 0;
                                })));
    }
}
