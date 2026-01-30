package com.thesilentnights.easylogin.commands.common

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.thesilentnights.easylogin.service.ChangePasswordService
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands

class ChangePassword : CommonCommands {
    override val command: LiteralArgumentBuilder<CommandSourceStack>
        get() = Commands.literal("changepassword")
            .then(
                Commands.argument(
                    "newPassword",
                    StringArgumentType.greedyString()
                )
                    .then(
                        Commands.argument(
                            "newPasswordConfirm",
                            StringArgumentType.greedyString()
                        )
                            .executes { context: CommandContext<CommandSourceStack> ->
                                if (ChangePasswordService.changePassword(
                                        context
                                    )
                                ) 1 else 0
                            }
                    )
            )
}
