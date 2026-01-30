package com.thesilentnights.easylogin.service

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.thesilentnights.easylogin.pojo.PlayerAccount
import com.thesilentnights.easylogin.service.AccountService.getAccount
import com.thesilentnights.easylogin.service.LoginService.isLoggedIn
import net.minecraft.ChatFormatting
import net.minecraft.commands.CommandSourceStack
import net.minecraft.network.chat.TranslatableComponent
import java.util.function.Consumer

object ChangePasswordService {
    @Throws(CommandSyntaxException::class)
    fun changePassword(context: CommandContext<CommandSourceStack?>): Boolean {
        if (!isLoggedIn(context.getSource()!!.playerOrException.getUUID())) {
            context.getSource()!!.sendFailure(
                TranslatableComponent("commands.password.change.failure.unlogged").withStyle(ChatFormatting.BOLD)
                    .withStyle(ChatFormatting.RED)
            )
            return false
        }

        val newPassword = StringArgumentType.getString(context, "newPassword")
        val newPasswordConfirm = StringArgumentType.getString(context, "newPasswordConfirm")

        if (newPassword == newPasswordConfirm) {
            getAccount(
                context.getSource()!!.playerOrException.getUUID()
            ).ifPresent(Consumer { playerAccount: PlayerAccount? ->
                playerAccount!!.password = newPassword
                AccountService.updateAccount(playerAccount)
            })
            return true
        } else {
            context.getSource()!!.sendFailure(
                TranslatableComponent("commands.password.confirm.failure").withStyle(ChatFormatting.RED)
                    .withStyle(ChatFormatting.BOLD)
            )
        }
        return false
    }
}
