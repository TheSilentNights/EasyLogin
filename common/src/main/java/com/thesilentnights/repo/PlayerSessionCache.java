package com.thesilentnights.repo;

import com.thesilentnights.pojo.PlayerAccount;
import com.thesilentnights.pojo.PlayerSession;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.server.level.ServerPlayer;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class PlayerSessionCache {
    private static final Map<UUID, PlayerSession> forRemoval = new ConcurrentHashMap<>();

    public static void scheduleDrop(PlayerAccount account) {
        log.info(account.toString());
        forRemoval.put(UUID.fromString(account.getUuid()), new PlayerSession(account, 60 * 60 * 20));
    }

    public static boolean hasSession(ServerPlayer serverPlayer) {
        return forRemoval.containsKey(serverPlayer.getUUID());
    }

    public static PlayerSession getSession(UUID key) {
        return forRemoval.get(key);
    }

    public static void tick() {
        if (!forRemoval.isEmpty()) {
            forRemoval.forEach((uuid, playerSession) -> {
                if (playerSession.getLeftTime() > 0) {
                    playerSession.setLeftTime(playerSession.getLeftTime() - 1);
                }
            });
        }
    }
}
