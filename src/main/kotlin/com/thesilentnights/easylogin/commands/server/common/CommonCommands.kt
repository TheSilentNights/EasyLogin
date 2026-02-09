package com.thesilentnights.easylogin.commands.server.common

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.minecraft.commands.CommandSourceStack

interface CommonCommands {
    val command: LiteralArgumentBuilder<CommandSourceStack>
}
