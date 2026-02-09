package com.thesilentnights.easylogin.commands.admin;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.thesilentnights.easylogin.service.LoginService;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.entity.Entity;


public abstract class AdminCommands {
    LiteralArgumentBuilder<CommandSourceStack> MAIN_NODE = Commands.literal("easylogin")
            .requires((CommandSourceStack commandSourceStack) -> {
                Entity entity = commandSourceStack.getEntity();
                if (entity == null) {
                    return commandSourceStack.hasPermission(4);
                } else {
                    return commandSourceStack.hasPermission(4) && LoginService.isLoggedIn(entity.getUUID());
                }
            });


    abstract LiteralArgumentBuilder<CommandSourceStack> getCommand();
}