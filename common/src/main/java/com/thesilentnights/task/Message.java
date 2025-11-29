package com.thesilentnights.task;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class Message implements TickTimer {
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
                TickTimerManager.cancel(this.serverPlayer.getUUID(), TickType.MESSAGE);
            }
        }
    }

    @Override
    public UUID getUUId() {
        return serverPlayer.getUUID();
    }

    @Override
    public TickType getTickType() {
        return TickType.MESSAGE;
    }


}
