package com.thesilentnights.commands.common;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.thesilentnights.EasyLogin;
import net.minecraft.commands.CommandSourceStack;


public interface CommonCommands {
    static void registerCommands(CommandDispatcher<CommandSourceStack> commandDispatcher){
        commandDispatcher.register(EasyLogin.context.getBean(LoginCommands.class).getCommand());
        commandDispatcher.register(EasyLogin.context.getBean(RegistrarCommands.class).getCommand());
        commandDispatcher.register(EasyLogin.context.getBean(LogoutCommand.class).getCommand());
    }

    LiteralArgumentBuilder<CommandSourceStack> getCommand();
}
