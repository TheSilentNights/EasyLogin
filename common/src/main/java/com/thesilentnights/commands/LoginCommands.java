package com.thesilentnights.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.thesilentnights.service.PlayerLoginAuth;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class LoginCommands implements ICommands{
    @Override
    public LiteralArgumentBuilder<ServerCommandSource> getCommand() {
        return CommandManager.literal("login").then(CommandManager.argument("password", StringArgumentType.string()).executes( context->{
            return PlayerLoginAuth.authPlayerWithPwd(context.getSource().getName(), StringArgumentType.getString(context, "password")) ? 1 : 0;
        }));
    }
}
