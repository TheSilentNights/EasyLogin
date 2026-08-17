package cn.thesilentnights.easylogin.commands;

import cn.thesilentnights.easylogin.services.auth.LoginService;
import cn.thesilentnights.easylogin.utils.Dependencies;
import com.mojang.logging.LogUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.permissions.Permissions;

public abstract class PermissionRequired {
        protected boolean requireAdminPermission(CommandSourceStack sourceStack) {
                return sourceStack.permissions().hasPermission(Permissions.COMMANDS_ADMIN);
        }

        protected boolean requireLoginAuth(CommandSourceStack sourceStack) {
                if (sourceStack.getEntity() != null) {
                        return Dependencies.getDependency(LoginService.class).isPlayerLogged(sourceStack.getEntity().getUUID());
                } else {
                        LogUtils.getLogger().info("false in login auth");
                        return false;
                }
        }


}
