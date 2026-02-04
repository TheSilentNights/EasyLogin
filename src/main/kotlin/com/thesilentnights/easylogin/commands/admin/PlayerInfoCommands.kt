package com.thesilentnights.easylogin.commands.admin

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.thesilentnights.easylogin.pojo.PlayerAccount
import com.thesilentnights.easylogin.service.AccountService
import com.thesilentnights.easylogin.utils.TextUtil
import net.minecraft.commands.CommandSourceStack
import org.koin.core.component.inject
import java.util.*

class PlayerInfoCommands : AdminCommands {
    val accountService: AccountService by inject()
    override fun getCommand(mainNode: LiteralArgumentBuilder<CommandSourceStack>): LiteralArgumentBuilder<CommandSourceStack> {
        return mainNode
            .then(
                net.minecraft.commands.Commands.literal("playerinfo")
                    .then(
                        net.minecraft.commands.Commands.argument(
                            "playerName",
                            StringArgumentType.string()
                        )
                            .executes { context: CommandContext<CommandSourceStack> ->
                                val account: Optional<PlayerAccount> = accountService.getAccount(
                                    UUID.fromString(
                                        StringArgumentType.getString(
                                            context,
                                            "playerName"
                                        )
                                    )
                                )
                                if (account.isPresent) {
                                    context.getSource().sendFailure(TextUtil.createText(account.get().toString()))
                                    return@executes 1
                                }
                                return@executes 0
                            }
                    )
            )
    }
}
