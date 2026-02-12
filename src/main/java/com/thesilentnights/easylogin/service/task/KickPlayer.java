package com.thesilentnights.easylogin.service.task;

import com.thesilentnights.easylogin.utils.LogUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class KickPlayer extends Task {

    private final ServerPlayer serverPlayer;
    private int delay;

    public KickPlayer(ServerPlayer serverPlayer, int delay) {
        this.serverPlayer = serverPlayer;
        this.delay = delay;
    }


    @Override
    public void execute() {
        LogUtil.logInfo(KickPlayer.class, "KickPlayer: " + serverPlayer.getDisplayName().getString());
        serverPlayer.connection.disconnect(Component.literal("You didn't login in time"));
    }

    @Override
    public int getTickDelay() {
        return this.delay;
    }

    @Override
    public void reduceTickDelay(int tickDelay) {
        this.delay -= tickDelay;
    }

    @Override
    public boolean shouldCancel(UUID uuid) {
        return uuid == serverPlayer.getUUID();
    }
}