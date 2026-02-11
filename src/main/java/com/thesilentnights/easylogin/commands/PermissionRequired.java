package com.thesilentnights.easylogin.commands;

import com.thesilentnights.easylogin.service.LoginService;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

public abstract class PermissionRequired {
    public boolean requireAdminPermission(CommandSourceStack sourceStack) {
        return sourceStack.hasPermission(4);
    }

    public boolean requireLoginAuth(CommandSourceStack sourceStack) {
        if (sourceStack.getEntity() != null && sourceStack.getEntity() instanceof ServerPlayer player) {
            return LoginService.isLoggedIn(player.getUUID());
        } else {
            return false;
        }
    }


}
