package com.thesilentnights.easylogin.commands;

import com.thesilentnights.easylogin.service.LoginService;
import net.minecraft.commands.CommandSourceStack;

public abstract class PermissionRequired {
    public boolean requireAdminPermission(CommandSourceStack sourceStack) {
        return sourceStack.hasPermission(4);
    }

    public boolean requireLoginAuth(CommandSourceStack sourceStack) {
        if (sourceStack.getEntity() != null) {
            return LoginService.isLoggedIn(sourceStack.getEntity().getUUID());
        } else {
            return false;
        }
    }


}
