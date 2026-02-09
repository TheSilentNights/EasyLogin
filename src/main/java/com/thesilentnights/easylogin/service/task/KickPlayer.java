package com.thesilentnights.easylogin.service.task;

import com.thesilentnights.easylogin.service.TaskService;
import net.minecraft.server.level.ServerPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class KickPlayer implements Task {

    private static final Logger log = LogManager.getLogger(KickPlayer.class);
    private final ServerPlayer serverPlayer;
    private final long delay;
    private long tickCount = 0;

    public KickPlayer(ServerPlayer serverPlayer, long delay) {
        this.serverPlayer = serverPlayer;
        this.delay = delay;
    }

    @Override
    public void tick() {
        tickCount++;
        if (tickCount >= delay) {
            serverPlayer.disconnect();
            log.info("player {} has been kicked due to timeout in login", serverPlayer.getGameProfile().getName());
            String taskId = TaskService.generateTaskIdentifier(
                    serverPlayer.getUUID(),
                    TaskService.Suffix.TIMEOUT.name()
            );
            TaskService.cancelTask(taskId);
        }
    }
}