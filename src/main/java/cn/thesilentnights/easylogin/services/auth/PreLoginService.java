package cn.thesilentnights.easylogin.services.auth;

import net.minecraft.server.level.ServerPlayer;

public interface PreLoginService {
        void preLogin(ServerPlayer serverPlayer);
}
