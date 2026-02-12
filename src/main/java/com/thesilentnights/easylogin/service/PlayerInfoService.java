package com.thesilentnights.easylogin.service;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.thesilentnights.easylogin.utils.TextUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.GameProfileArgument;

import java.util.Collection;

public class PlayerInfoService {
    public static boolean handle(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<GameProfile> player = GameProfileArgument.getGameProfiles(context, "player");

        context.getSource().sendSuccess(TextUtil.serialize(TextUtil.FormatType.INFO, "Player info: " + AccountService.getAccount(player.iterator().next().getId()).toString()), true);

        return true;
    }
}
