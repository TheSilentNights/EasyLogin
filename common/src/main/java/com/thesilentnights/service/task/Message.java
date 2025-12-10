package com.thesilentnights.service.task;

import com.thesilentnights.service.TaskService;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class Message implements Task {
    ServerPlayer serverPlayer;
    Component message;
    long delay;
    boolean isLoop;
    long tickCount = 0;

    public Message(ServerPlayer serverPlayer, Component message, long delay, boolean isLoop) {
        this.serverPlayer = serverPlayer;
        this.message = message;
        this.delay = delay;
        this.isLoop = isLoop;
    }

    @Override
    public void tick() {
        this.tickCount++;
        if (this.tickCount >= this.delay) {
            this.serverPlayer.sendMessage(this.message, this.serverPlayer.getUUID());
            if (isLoop) {
                this.tickCount = 0;
            } else {
                TaskService.cancelTask(TaskService.generateTaskIdentifier(serverPlayer.getUUID(), TaskService.TaskType.MESSAGE));
            }
        }
    }


}
