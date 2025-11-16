package com.thesilentnights.commands.admin;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.thesilentnights.service.PlayerLoginAuth;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public class PlayerInfoCommands implements AdminCommands{
    @Autowired
    private PlayerLoginAuth loginAuth;

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCommand(LiteralArgumentBuilder<CommandSourceStack> mainNode) {
        return mainNode
                .then(Commands.literal("playerinfo")
                        .then(Commands.argument("playerName", StringArgumentType.string())
                                .executes(context -> {
                                    if (loginAuth.hasAccount(UUID.fromString(StringArgumentType.getString(context, "playerName")))){
                                        loginAuth.getAccount(UUID.fromString(StringArgumentType.getString(context, "playerName"))).ifPresent(account -> {
                                            context.getSource().sendSuccess(Component.nullToEmpty(account.toString()), true);
                                        });
                                        return 1;
                                    }
                                    return 1;
                                }))
                );
    }
}
