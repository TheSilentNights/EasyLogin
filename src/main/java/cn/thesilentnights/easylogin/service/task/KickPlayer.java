package cn.thesilentnights.easylogin.service.task;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

import cn.thesilentnights.easylogin.utils.LogUtil;

public class KickPlayer extends Task {

    private final ServerPlayer serverPlayer;
    private Long endTimeMillis;

    public KickPlayer(ServerPlayer serverPlayer, Long delaySeconds) {
        this.serverPlayer = serverPlayer;
        this.endTimeMillis = delaySeconds*1000 + System.currentTimeMillis();
    }


    @Override
    public void execute() {
        LogUtil.getLogger().info("KickPlayer: " + serverPlayer.getDisplayName().getString());
        serverPlayer.connection.disconnect(new TextComponent("You didn't login in time"));
    }

    @Override
    public Long getEndTimeMillis() {
        return endTimeMillis;
    }


    @Override
    public boolean shouldCancel(UUID uuid) {
        return uuid.equals(serverPlayer.getUUID());
    }
}