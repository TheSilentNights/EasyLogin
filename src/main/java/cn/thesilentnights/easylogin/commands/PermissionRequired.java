package cn.thesilentnights.easylogin.commands;

import cn.thesilentnights.easylogin.services.LoginService;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.permissions.Permissions;

public abstract class PermissionRequired {
        public boolean requireAdminPermission(CommandSourceStack sourceStack) {
                return sourceStack.permissions().hasPermission(Permissions.COMMANDS_ADMIN);
        }

        public boolean requireLoginAuth(CommandSourceStack sourceStack) {
                if (sourceStack.getEntity() != null) {
                        return LoginService.isLoggedIn(sourceStack.getEntity().getUUID());
                } else {
                        return false;
                }
        }


}
