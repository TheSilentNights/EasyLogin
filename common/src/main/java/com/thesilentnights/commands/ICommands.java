package com.thesilentnights.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.ServerCommandSource;

public interface ICommands {
    public static void registerCommands(CommandDispatcher<ServerCommandSource> commandDispatcher){
        commandDispatcher.register(new LoginCommands().getCommand());
    }

    LiteralArgumentBuilder<ServerCommandSource> getCommand();
}
