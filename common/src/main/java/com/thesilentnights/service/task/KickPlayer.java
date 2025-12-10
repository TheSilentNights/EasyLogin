package com.thesilentnights.service.task;

import com.thesilentnights.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.server.level.ServerPlayer;

@Slf4j
public class KickPlayer implements Task {
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
            TaskService.cancelTask(TaskService.generateTaskIdentifier(serverPlayer.getUUID(), TaskService.TaskType.KICK_PLAYER));
        }
    }
}
