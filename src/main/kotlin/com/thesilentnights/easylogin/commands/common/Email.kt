package com.thesilentnights.easylogin.commands.common

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands

class Email : CommonCommands {
    override val command: LiteralArgumentBuilder<CommandSourceStack>
        get() = Commands.literal("email")
            .then(
                Commands.literal("bind")
                    .then(
                        Commands.argument(
                            "email",
                            StringArgumentType.greedyString()
                        )
                            .executes { commandContext: CommandContext<CommandSourceStack?>? ->
                                //TODO email
                                1
                            }
                    )
            )
}
