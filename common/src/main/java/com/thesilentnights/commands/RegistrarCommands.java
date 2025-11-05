package com.thesilentnights.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.thesilentnights.service.PlayerLoginAuth;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;

public class RegistrarCommands implements ICommands{

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return Commands.literal("register")
                .then(Commands.argument("password", StringArgumentType.string())
                        .then(Commands.argument("repeatPassword", StringArgumentType.string())
                                .executes(context->{
                                    String password = StringArgumentType.getString(context, "password");
                                    String repeatPassword = StringArgumentType.getString(context, "repeatPassword");
                                    if (password.equals(repeatPassword)){
                                        PlayerLoginAuth.registerPlayer(context.getSource().getPlayerOrException(), password);
                                        return 1;
                                    }
                                    context.getSource().getPlayerOrException().sendMessage(new TextComponent("password not match"),context.getSource().getPlayerOrException().getUUID());
                                    return 0;
                                })));
    }
}
