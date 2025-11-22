package com.thesilentnights.task;

import lombok.extern.slf4j.Slf4j;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

@Slf4j
public class KickPlayer implements TickTimer {
    ServerPlayer serverPlayer;
    long delay;
    long tickCount = 0;

    public KickPlayer(ServerPlayer serverPlayer, long delay) {
        this.serverPlayer = serverPlayer;
        this.delay = delay;
    }

    @Override
    public void tick() {
        this.tickCount++;
        if (this.tickCount >= this.delay) {
            serverPlayer.disconnect();
            log.info("player {} has been kicked due to timeout in login", serverPlayer.getGameProfile().getName());
            TickTimerManager.cancel(serverPlayer.getUUID(), TickType.KICK);
        }
    }

    @Override
    public UUID getUUId() {
        return serverPlayer.getUUID();
    }

    @Override
    public TickType getTickType() {
        return TickType.KICK;
    }
}
