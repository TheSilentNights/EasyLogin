package com.thesilentnights.commands.admin;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.thesilentnights.service.PlayerLoginAuth;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class PlayerInfoCommands implements AdminCommands{
    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCommand(LiteralArgumentBuilder<CommandSourceStack> mainNode) {
        return mainNode
                .then(Commands.literal("playerinfo")
                        .then(Commands.argument("playerName", StringArgumentType.string())
                                .executes(context -> {
                                    if (PlayerLoginAuth.hasAccount(StringArgumentType.getString(context, "playerName"))){
                                        PlayerLoginAuth.getAccount(StringArgumentType.getString(context, "playerName")).ifPresent(account -> {
                                            context.getSource().sendSuccess(Component.nullToEmpty(account.toString()), true);
                                        });
                                        return 1;
                                    }
                                    return 1;
                                }))
                );
    }
}
