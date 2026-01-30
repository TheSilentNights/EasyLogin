package com.thesilentnights.easylogin.service.task

import com.thesilentnights.easylogin.service.TaskService
import net.minecraft.server.level.ServerPlayer
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class KickPlayer(serverPlayer: ServerPlayer, delay: Long) : Task {
    var serverPlayer: ServerPlayer
    var delay: Long
    var tickCount: Long = 0
    val log: Logger = LogManager.getLogger(KickPlayer::class.java)

    init {
        this.serverPlayer = serverPlayer
        this.delay = delay
    }

    override fun tick() {
        this.tickCount++
        if (this.tickCount >= this.delay) {
            serverPlayer.disconnect()
            log.info("player {} has been kicked due to timeout in login", serverPlayer.getGameProfile().getName())
            TaskService.cancelTask(
                TaskService.generateTaskIdentifier(
                    serverPlayer.getUUID(),
                    TaskService.TaskType.KICK_PLAYER
                )
            )
        }
    }
}
