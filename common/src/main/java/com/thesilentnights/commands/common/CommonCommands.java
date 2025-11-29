package com.thesilentnights.commands.common;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;


public interface CommonCommands {
    static void registerCommands(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register(new LoginCommands().getCommand());
        commandDispatcher.register(new RegistrarCommands().getCommand());
        commandDispatcher.register(new LogoutCommand().getCommand());
    }

    LiteralArgumentBuilder<CommandSourceStack> getCommand();
}
