package com.thesilentnights.easylogin.service.task;

import com.thesilentnights.easylogin.service.TaskService;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class Message implements Task {

    private final ServerPlayer serverPlayer;
    private final Component message;
    private final long delay;
    private final boolean isLoop;
    private long tickCount = 0;

    public Message(ServerPlayer serverPlayer, Component message, long delay, boolean isLoop) {
        this.serverPlayer = serverPlayer;
        this.message = message;
        this.delay = delay;
        this.isLoop = isLoop;
    }

    @Override
    public void tick() {
        tickCount++;
        if (tickCount >= delay) {
            serverPlayer.sendMessage(message, serverPlayer.getUUID());
            if (isLoop) {
                tickCount = 0;
            } else {
                String taskId = TaskService.generateTaskIdentifier(
                        serverPlayer.getUUID(),
                        TaskService.Suffix.MESSAGE.name()
                );
                TaskService.cancelTask(taskId);
            }
        }
    }
}