package cn.thesilentnights.easylogin.service;

import cn.thesilentnights.easylogin.repo.PlayerCache;
import java.util.UUID;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

public class ActionCheckService {

        public static boolean shouldCancelEvent(LivingEntity entity) {
                if (entity instanceof ServerPlayer) {
                        return !isLoggedIn(entity.getUUID());
                }
                return false;
        }

        public static boolean isLoggedIn(UUID uuid) {
                return PlayerCache.isPlayerLogged(uuid);
        }
}
