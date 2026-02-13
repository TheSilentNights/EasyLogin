package com.thesilentnights.easylogin.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.thesilentnights.easylogin.service.LoginService;
import com.thesilentnights.easylogin.utils.TextUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class Logout extends PermissionRequired implements ICommands {

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("logout")
                        .requires(this::requireLoginAuth)
                        .executes(context -> {
                            LoginService.forceLogoutPlayer(context.getSource().getPlayerOrException());
                            String name = context.getSource().getPlayerOrException().getGameProfile().getName();
                            context.getSource().sendSuccess(
                                    () -> TextUtil.serialize(
                                            TextUtil.FormatType.SUCCESS, Component.translatable(
                                                    "commands.logout.success",
                                                    name
                                            )
                                    ), false
                            );
                            return 1;
                        })
        );
    }
}
