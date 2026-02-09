package com.thesilentnights.easylogin.service

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.thesilentnights.easylogin.pojo.SqlColumnDefinition
import net.minecraft.ChatFormatting
import net.minecraft.commands.CommandSourceStack
import net.minecraft.network.chat.TranslatableComponent

object ChangePasswordService {
    @Throws(CommandSyntaxException::class)
    fun changePassword(context: CommandContext<CommandSourceStack>): Boolean {
        if (!LoginService.isLoggedIn(context.getSource().playerOrException.getUUID())) {
            context.source.sendFailure(
                TranslatableComponent("commands.password.change.failure.unlogged").apply {
                    withStyle(ChatFormatting.BOLD)
                    withStyle(ChatFormatting.RED)
                }
            )
            return false
        }

        val newPassword = StringArgumentType.getString(context, "newPassword")
        val newPasswordConfirm = StringArgumentType.getString(context, "newPasswordConfirm")

        //match
        if (newPassword == newPasswordConfirm) {
            AccountService.updateSingleColumn(
                SqlColumnDefinition.PASSWORD,
                newPassword,
                context.source.playerOrException.getUUID()
            )
            context.source.sendSuccess(TranslatableComponent("commands.password.change.success").apply {
                withStyle(ChatFormatting.BOLD)
                withStyle(ChatFormatting.GREEN)
            }, true)
            return true
        } else {
            context.source.sendFailure(
                TranslatableComponent("commands.password.confirm.failure").apply {
                    withStyle(ChatFormatting.BOLD)
                    withStyle(ChatFormatting.RED)
                }
            )
        }
        return false
    }
}
