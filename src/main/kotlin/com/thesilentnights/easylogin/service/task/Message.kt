package com.thesilentnights.easylogin.service.task

import com.thesilentnights.easylogin.service.TaskService
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer

class Message(var serverPlayer: ServerPlayer,
              var message: Component,
              var delay: Long,
              var isLoop: Boolean
) :
    Task {
    var tickCount: Long = 0

    override fun tick() {
        this.tickCount++
        if (this.tickCount >= this.delay) {
            this.serverPlayer.sendMessage(this.message, this.serverPlayer.getUUID())
            if (isLoop) {
                this.tickCount = 0
            } else {
                TaskService.cancelTask(
                    TaskService.generateTaskIdentifier(
                        serverPlayer.getUUID(),
                        TaskService.Suffix.MESSAGE.name
                    )
                )
            }
        }
    }
}
