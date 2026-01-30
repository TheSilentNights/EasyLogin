package com.thesilentnights.easylogin.commands.common

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.minecraft.commands.CommandSourceStack

interface CommonCommands {
    val command: LiteralArgumentBuilder<CommandSourceStack>

    companion object {
        fun registerCommands(commandDispatcher: CommandDispatcher<CommandSourceStack>) {
            with(commandDispatcher) {
                register(LoginCommands().command)
                register(RegistrarCommands().command)
                register(LogoutCommand().command)
                register(RecoverCommands().command)
                register(ChangePassword().command)
                register(Email().command)
            }
        }
    }
}
