package com.thesilentnights.easylogin.commands.admin

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack

class EmailTest : AdminCommands {
    override fun getCommand(mainNode: LiteralArgumentBuilder<CommandSourceStack>): LiteralArgumentBuilder<CommandSourceStack> {
        return mainNode.then(
            net.minecraft.commands.Commands.literal("email")
                .then(
                    net.minecraft.commands.Commands.literal("test")
                        .then(
                            net.minecraft.commands.Commands.argument<kotlin.String?>(
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
