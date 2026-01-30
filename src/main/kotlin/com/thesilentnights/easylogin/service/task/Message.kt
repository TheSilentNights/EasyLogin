package com.thesilentnights.easylogin.service.task

import com.thesilentnights.easylogin.service.TaskService
import net.minecraft.server.level.ServerPlayer

class Message(serverPlayer: ServerPlayer, message: net.minecraft.network.chat.Component, delay: Long, isLoop: Boolean) :
    com.thesilentnights.easylogin.service.task.Task {
    var serverPlayer: ServerPlayer
    var message: net.minecraft.network.chat.Component
    var delay: Long
    var isLoop: Boolean
    var tickCount: Long = 0

    init {
        this.serverPlayer = serverPlayer
        this.message = message
        this.delay = delay
        this.isLoop = isLoop
    }

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
                        TaskService.TaskType.MESSAGE
                    )
                )
            }
        }
    }
}
