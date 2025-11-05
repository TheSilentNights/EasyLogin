package com.thesilentnights.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;


public interface ICommands {
    public static void registerCommands(CommandDispatcher<CommandSourceStack> commandDispatcher){
        commandDispatcher.register(new LoginCommands().getCommand());
        commandDispatcher.register(new RegistrarCommands().getCommand());
    }

    LiteralArgumentBuilder<CommandSourceStack> getCommand();
}
