package com.thesilentnights.service;

import com.thesilentnights.repo.PlayerCache;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class ActionCheckService {
    public static boolean shouldCancelEvent(Player entity) {
        if (entity instanceof ServerPlayer entity1) {
            return !isLoggedIn(entity1.getUUID());
        }
        return false;
    }

    public static boolean isLoggedIn(UUID uuid){
        return PlayerCache.hasAccount(uuid);
    }
}
