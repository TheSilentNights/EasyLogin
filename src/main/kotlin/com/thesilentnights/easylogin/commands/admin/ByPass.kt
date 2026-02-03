package com.thesilentnights.easylogin.commands.admin

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands

class ByPass: AdminCommands {
    override fun getCommand(mainNode: LiteralArgumentBuilder<CommandSourceStack>): LiteralArgumentBuilder<CommandSourceStack> {
        return mainNode.then(
            Commands.literal("bypass").then(
                Commands.literal("add").then(
                    Commands.argument("target", StringArgumentType.greedyString())
                        .executes { context: CommandContext<CommandSourceStack> ->

                            1
                        }
                )
            )
        )
    }
}