package cn.thesilentnights.easylogin.service;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

import java.util.UUID;

import cn.thesilentnights.easylogin.repo.PlayerCache;

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