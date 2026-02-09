package com.thesilentnights.easylogin.commands.admin

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.thesilentnights.easylogin.pojo.PlayerAccount
import com.thesilentnights.easylogin.service.AccountService
import com.thesilentnights.easylogin.utils.TextUtil
import net.minecraft.ChatFormatting
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.server.level.ServerPlayer
import org.koin.core.component.inject
import java.util.*

class TeleportToOfflinePlayer : AdminCommands {
    val AccountService: AccountService by inject()
    override fun getCommand(mainNode: LiteralArgumentBuilder<CommandSourceStack>): LiteralArgumentBuilder<CommandSourceStack> {
        return mainNode
            .then(
                Commands.literal("teleport")
                    .then(
                        Commands.argument(
                            "targetName",
                            StringArgumentType.string()
                        ).executes { context ->
                            val serverPlayer: ServerPlayer = context.source.playerOrException
                            val targetName = StringArgumentType.getString(context, "targetName")
                            //check the player is offline
                            if (AccountService.hasAccount(targetName) && context.getSource().server
                                    .playerList.getPlayerByName(targetName) == null
                            ) {
                                val account: Optional<PlayerAccount> = AccountService.getAccount(targetName)
                                if (account.isPresent) {
                                    val playerAccount: PlayerAccount = account.get()
                                    serverPlayer.teleportTo(
                                        playerAccount.lastLoginX,
                                        playerAccount.lastLoginY,
                                        playerAccount.lastLoginZ
                                    )
                                    return@executes 1
                                } else {
                                    context.getSource()
                                        .sendFailure(TextUtil.createBold(ChatFormatting.RED, "player does not exists"))
                                    return@executes 0
                                }
                            }
                            context.getSource()
                                .sendFailure(TextUtil.createBold(ChatFormatting.RED, "player does not exists"))
                            return@executes 0
                        }
                    )


            )
    }
}
