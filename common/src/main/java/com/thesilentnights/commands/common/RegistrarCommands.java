package com.thesilentnights.commands.common;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.thesilentnights.exception.PasswordDoesNotMatchException;
import com.thesilentnights.service.PlayerLoginService;
import com.thesilentnights.utils.TextUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RegistrarCommands implements CommonCommands {
    @Autowired
    PlayerLoginService loginAuth;

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return Commands.literal("register")
                .then(Commands.argument("password", StringArgumentType.string())
                        .then(Commands.argument("repeatPassword", StringArgumentType.string())
                                .executes(context -> {
                                    String password = StringArgumentType.getString(context, "password");
                                    String repeatPassword = StringArgumentType.getString(context, "repeatPassword");


                                    if (loginAuth.hasAccount(context.getSource().getPlayerOrException().getUUID())) {
                                        context.getSource().getPlayerOrException().sendMessage(TextUtil.createText(ChatFormatting.RED, "the account has already been registered"), context.getSource().getPlayerOrException().getUUID());
                                        return 0;
                                    }


                                    try {
                                        loginAuth.registerPlayer(context.getSource().getPlayerOrException(), password,repeatPassword);
                                        context.getSource().getPlayerOrException().sendMessage(TextUtil.createText(ChatFormatting.GREEN, "register success"), context.getSource().getPlayerOrException().getUUID());
                                        return 1;
                                    } catch (PasswordDoesNotMatchException e) {
                                        context.getSource().sendFailure(TextUtil.createText(ChatFormatting.RED,"password does not match"));
                                        return 0;
                                    }
                                })));
    }
}
