package cn.thesilentnights.easylogin.services.task;

import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;

import java.util.UUID;

public class KickPlayer extends Task {

        private final ServerPlayer serverPlayer;
        private final Long endTimeMillis;
        private final Logger logger = LogUtils.getLogger();

        public KickPlayer(ServerPlayer serverPlayer, Long delaySeconds) {
                this.serverPlayer = serverPlayer;
                this.endTimeMillis = delaySeconds * 1000 + System.currentTimeMillis();
        }


        @Override
        public void execute() {
                logger.info("KickPlayer: {}", serverPlayer.getDisplayName().getString());
                serverPlayer.connection.disconnect(Component.translatable("You didn't login in time"));
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
