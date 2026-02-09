package com.thesilentnights.easylogin.service;

import com.thesilentnights.easylogin.repo.PlayerCache;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

import java.util.UUID;

public class ActionCheckService {

    public static boolean shouldCancelEvent(LivingEntity entity) {
        if (entity instanceof ServerPlayer) {
            return !isLoggedIn(entity.getUUID()) && !ByPassService.isBypassed(entity.getUUID());
        }
        return false;
    }

    public static boolean isLoggedIn(UUID uuid) {
        return PlayerCache.hasAccount(uuid);
    }
}