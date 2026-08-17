package cn.thesilentnights.easylogin.services.auth;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public interface LoginService {
        boolean login(CommandContext<CommandSourceStack> context) throws CommandSyntaxException;

        void forceLogin(ServerPlayer serverPlayer);

        void logout(ServerPlayer serverPlayer);

        boolean register(CommandContext<CommandSourceStack> context) throws CommandSyntaxException;

        boolean isPlayerLogged(UUID uuid);
}
