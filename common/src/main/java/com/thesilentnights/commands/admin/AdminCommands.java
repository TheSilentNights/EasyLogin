package com.thesilentnights.commands.admin;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public interface AdminCommands {
    static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        LiteralArgumentBuilder<CommandSourceStack> mainNode = Commands.literal("easylogin").requires(commandSourceStack -> commandSourceStack.hasPermission(4));
        dispatcher.register(new PlayerInfoCommands().getCommand(mainNode));
        dispatcher.register(new TeleportToOfflinePlayer().getCommand(mainNode));
    }

    LiteralArgumentBuilder<CommandSourceStack> getCommand(LiteralArgumentBuilder<CommandSourceStack> mainNode);
}
