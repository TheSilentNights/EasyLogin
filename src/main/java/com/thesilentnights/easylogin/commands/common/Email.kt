package com.thesilentnights.easylogin.commands.common

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.thesilentnights.easylogin.service.EmailService
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands

class Email(val emailService: EmailService) : CommonCommands {
    override val command: LiteralArgumentBuilder<CommandSourceStack>
        get() = Commands.literal("email")
            .then(
                Commands.literal("bind")
                    .then(
                        Commands.argument(
                            "email",
                            StringArgumentType.greedyString()
                        )
                            .executes { commandContext: CommandContext<CommandSourceStack> ->
                                //TODO email
                                if (emailService.bindEmail(commandContext)) {
                                    1
                                } else {
                                    0
                                }
                            }
                    )
            )
}
