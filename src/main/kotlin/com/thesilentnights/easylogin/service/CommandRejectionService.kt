package com.thesilentnights.easylogin.service

import net.minecraftforge.event.CommandEvent

class CommandRejectionService {
    val bypassList: List<String> = listOf(
        "login",
        "register"
    )

    fun handleRejection(event: CommandEvent) {
        val context = event.parseResults.context
        if (context.source.entity == null) {
            return
        }

        val playerOrException = context.source.playerOrException
        if (ActionCheckService.shouldCancelEvent(playerOrException) && !bypassList.contains(event.parseResults.context.nodes[0].node.name)) {
            event.isCanceled = true
        }
    }

}