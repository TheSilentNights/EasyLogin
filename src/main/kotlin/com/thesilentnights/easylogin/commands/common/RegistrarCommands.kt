package com.thesilentnights.easylogin.commands.common

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.thesilentnights.easylogin.service.LoginService
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands

class RegistrarCommands(private val loginService: LoginService) : CommonCommands {
    override val command: LiteralArgumentBuilder<CommandSourceStack>
        get() = Commands.literal("register")
            .then(
                Commands.argument("password", StringArgumentType.string())
                    .then(
                        Commands.argument(
                            "repeat",
                            StringArgumentType.string()
                        )
                            .executes { context: CommandContext<CommandSourceStack> ->
                                if (loginService.register(
                                        context
                                    )
                                ) 1 else 0
                            }
                    )
            )
}
