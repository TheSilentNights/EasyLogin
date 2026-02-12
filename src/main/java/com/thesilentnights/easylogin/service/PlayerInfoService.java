package com.thesilentnights.easylogin.service;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.network.chat.TextComponent;

import java.util.Collection;

public class PlayerInfoService {
    public static boolean handle(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<GameProfile> player = GameProfileArgument.getGameProfiles(context, "player");

        context.getSource().sendSuccess(new TextComponent("Player info: " + AccountService.getAccount(player.iterator().next().getId()).toString()), true);

        return true;
    }
}
