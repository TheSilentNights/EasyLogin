package cn.thesilentnights.easylogin.service.task;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

import cn.thesilentnights.easylogin.utils.LogUtil;

public class KickPlayer extends Task {

    private final ServerPlayer serverPlayer;
    private int delay;

    public KickPlayer(ServerPlayer serverPlayer, int delay) {
        this.serverPlayer = serverPlayer;
        this.delay = delay;
    }


    @Override
    public void execute() {
        LogUtil.getLogger().info("KickPlayer: " + serverPlayer.getDisplayName().getString());
        serverPlayer.connection.disconnect(Component.translatable("You didn't login in time"));
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