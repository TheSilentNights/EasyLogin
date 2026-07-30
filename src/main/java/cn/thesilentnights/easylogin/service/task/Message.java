package cn.thesilentnights.easylogin.service.task;

import cn.thesilentnights.easylogin.utils.MessageSender.MessageType;
import cn.thesilentnights.easylogin.utils.MessageSender;
import java.util.UUID;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;

public class Message extends Task implements Loop {
        final int originalDelay;
        Long endTimeMillis;
        MutableComponent message;
        ServerPlayer serverPlayer;

        public Message(ServerPlayer serverPlayer, MutableComponent message, int delaySeconds) {
                this.serverPlayer = serverPlayer;
                this.message = message;
                this.endTimeMillis = delaySeconds * 1000 + System.currentTimeMillis();
                this.originalDelay = delaySeconds;
        }

        @Override
        public void execute() {
                MessageSender.sendMessage(
                        serverPlayer,
                        message,
                        MessageType.INFO
                );
        }

        @Override
        public Long getEndTimeMillis() {
                return endTimeMillis;
        }

        @Override
        public boolean shouldCancel(UUID uuid) {
                return uuid.equals(serverPlayer.getUUID());
        }

        @Override
        public Task regenerate() {
                return new Message(serverPlayer, message, this.originalDelay);
        }


}
