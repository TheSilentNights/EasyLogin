package com.thesilentnights.task;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class Message implements TickTimer {
    ServerPlayer serverPlayer;
    Component message;
    long delay;
    boolean isLoop;
    long tickCount;

    public Message(ServerPlayer serverPlayer, Component message, int delay,boolean isLoop) {
        this.serverPlayer = serverPlayer;
        this.message = message;
        this.delay = delay;
        this.isLoop = isLoop;
    }
    @Override
    public void tick() {
        this.tickCount++;
        if (this.tickCount >= this.delay){
            this.serverPlayer.sendMessage(this.message,this.serverPlayer.getUUID());
        }
    }

    @Override
    public UUID getUUId() {
        return serverPlayer.getUUID();
    }


}
