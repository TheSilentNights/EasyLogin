package com.thesilentnights.easylogin.commands.server.admin

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands

class EmailTest : AdminCommands {
    override fun getCommand(mainNode: LiteralArgumentBuilder<CommandSourceStack>): LiteralArgumentBuilder<CommandSourceStack> {
        return mainNode.then(
            Commands.literal("email")
                .then(
                    Commands.literal("test")
                        .then(
                            Commands.argument<String?>(
                                "targetEmail",
                                StringArgumentType.greedyString()
                            )
                                .executes { context: CommandContext<CommandSourceStack?>? ->
                                    //TODO: Send email
                                    1
                                }
                        )
                )
        )
    }
}
