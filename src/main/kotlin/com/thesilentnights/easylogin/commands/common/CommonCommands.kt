package com.thesilentnights.easylogin.commands.common

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.minecraft.commands.CommandSourceStack
import org.koin.core.component.KoinComponent

interface CommonCommands: KoinComponent {
    val command: LiteralArgumentBuilder<CommandSourceStack>
}
