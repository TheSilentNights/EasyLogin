package com.thesilentnights.easylogin.commands.common

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.thesilentnights.easylogin.service.LoginService
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands

class LoginCommands : CommonCommands {
    override val command: LiteralArgumentBuilder<CommandSourceStack>
        get() = Commands.literal("login")
            .then(
                Commands.argument("password", StringArgumentType.string())
                    .executes(Command { context: CommandContext<CommandSourceStack> ->
                        if (LoginService.login(
                                context
                            )
                        ) 1 else 0
                    })
            )
}
