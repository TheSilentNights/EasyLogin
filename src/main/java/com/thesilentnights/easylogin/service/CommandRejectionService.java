package com.thesilentnights.easylogin.service

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.TranslatableComponent
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
            playerOrException.displayClientMessage(TranslatableComponent("command.rejected").apply {
                withStyle(ChatFormatting.RED)
                withStyle(ChatFormatting.BOLD)
            }, false)
        }
    }

}