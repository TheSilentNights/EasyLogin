package com.thesilentnights.easylogin.commands.common;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.thesilentnights.easylogin.repo.PlayerCache;
import com.thesilentnights.easylogin.service.LoginService;
import com.thesilentnights.easylogin.utils.TextUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public class Logout implements CommonCommands {
    private final LoginService loginService;

    public Logout(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return Commands.literal("logout")
                .executes((CommandContext<CommandSourceStack> commandContext) -> {
                    ServerPlayer player = commandContext.getSource().getPlayerOrException();
                    if (PlayerCache.hasAccount(player.getUUID())) {
                        //TODO rewrite
                        PlayerCache.dropAccount(player.getUUID(), false);
                        loginService.logoutPlayer(player);
                        commandContext.getSource().sendFailure(TextUtil.createBold(ChatFormatting.GREEN, "logged out"));
                    } else {
                        commandContext.getSource().sendFailure(TextUtil.createBold(ChatFormatting.RED, "you are not logged in"));
                    }
                    return 1;
                });
    }
}