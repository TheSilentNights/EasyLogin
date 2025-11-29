package com.thesilentnights.commands.common;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.thesilentnights.repo.PlayerCache;
import com.thesilentnights.utils.TextUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public class LogoutCommand implements CommonCommands {
    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return Commands.literal("logout").executes(commandContext -> {
            ServerPlayer player = commandContext.getSource().getPlayerOrException();
            if (PlayerCache.hasAccount(player.getUUID())) {
                PlayerCache.dropAccount(player.getUUID(), false);
                commandContext.getSource().sendFailure(TextUtil.createBold(ChatFormatting.GREEN, "logged out"));
            } else {
                commandContext.getSource().sendFailure(TextUtil.createBold(ChatFormatting.RED, "you are not logged in"));
            }
            return 1;
        });
    }
}
