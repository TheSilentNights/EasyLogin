package com.thesilentnights.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.thesilentnights.service.PlayerLoginAuth;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class LoginCommands implements ICommands{
    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return Commands.literal("login").then(Commands.argument("password", StringArgumentType.string()).executes(context->{
            return PlayerLoginAuth.authPlayerWithPwd(context.getSource().getTextName(), StringArgumentType.getString(context, "password")) ? 1 : 0;
        }));
    }
}
