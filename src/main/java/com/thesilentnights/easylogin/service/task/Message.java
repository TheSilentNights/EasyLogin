package com.thesilentnights.easylogin.service.task;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class Message extends Task implements Loop {
    final int originalDelay;
    int delay;
    Component message;
    ServerPlayer serverPlayer;

    public Message(ServerPlayer serverPlayer, Component message, int delay) {
        this.serverPlayer = serverPlayer;
        this.message = message;
        this.delay = delay;
        this.originalDelay = delay;
    }

    @Override
    public void execute() {
        serverPlayer.sendSystemMessage(message);
    }

    @Override
    public int getTickDelay() {
        return delay;
    }

    @Override
    public void reduceTickDelay(int tickDelay) {
        this.delay -= tickDelay;
    }

    @Override
    public boolean shouldCancel(UUID uuid) {
        return serverPlayer.getUUID() == uuid;
    }

    @Override
    public Task regenerate() {
        return new Message(serverPlayer, message, this.originalDelay);
    }
}