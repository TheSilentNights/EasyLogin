package com.thesilentnights.easylogin.commands.common

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.thesilentnights.easylogin.service.PasswordRecoveryService
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands

class Recover(val passwordRecoveryService: PasswordRecoveryService) : CommonCommands {
    override val command: LiteralArgumentBuilder<CommandSourceStack>
        get() = Commands.literal("recover")
            .then(
                Commands.literal("send")
                    .then(
                        Commands.argument(
                            "emailConfirm",
                            StringArgumentType.greedyString()
                        )
                            .executes { context: CommandContext<CommandSourceStack> ->
                                if (passwordRecoveryService.recoveryPassword(
                                        context
                                    )
                                ) 1 else 0
                            }
                    )
            )
            .then(
                Commands.literal("check")
                    .then(
                        Commands.argument(
                            "confirmCode",
                            StringArgumentType.greedyString()
                        )
                            .executes { context: CommandContext<CommandSourceStack> ->
                                if (passwordRecoveryService.confirmRecover(
                                        context
                                    )
                                ) 1 else 0
                            }
                    )
            )
}
