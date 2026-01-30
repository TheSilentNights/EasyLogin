package com.thesilentnights.easylogin.commands.common

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.thesilentnights.easylogin.repo.PlayerCache
import com.thesilentnights.easylogin.utils.TextUtil
import net.minecraft.ChatFormatting
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.server.level.ServerPlayer

class LogoutCommand : CommonCommands {
    override val command: LiteralArgumentBuilder<CommandSourceStack>
        get() = Commands.literal("logout")
            .executes { commandContext: CommandContext<CommandSourceStack> ->
                val player: ServerPlayer = commandContext.getSource().playerOrException
                if (PlayerCache.hasAccount(player.getUUID())) {
                    PlayerCache.dropAccount(player.getUUID(), false)
                    commandContext.getSource().sendFailure(TextUtil.createBold(ChatFormatting.GREEN, "logged out"))
                } else {
                    commandContext.getSource()
                        .sendFailure(TextUtil.createBold(ChatFormatting.RED, "you are not logged in"))
                }
                1
            }
}
