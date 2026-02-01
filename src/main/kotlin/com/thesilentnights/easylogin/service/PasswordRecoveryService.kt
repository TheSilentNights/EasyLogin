package com.thesilentnights.easylogin.service

import cn.hutool.captcha.generator.RandomGenerator
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.thesilentnights.easylogin.events.EasyLoginEvents
import com.thesilentnights.easylogin.pojo.PlayerAccount
import net.minecraft.ChatFormatting
import net.minecraft.commands.CommandSourceStack
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.server.level.ServerPlayer
import net.minecraftforge.common.MinecraftForge

object PasswordRecoveryService {
    var code: MutableMap<java.util.UUID?, String?> =
        HashMap()

    /***
     * recover the password
     * @param context
     * @return
     * @throws CommandSyntaxException
     */
    @Throws(CommandSyntaxException::class)
    fun recoveryPassword(context: CommandContext<CommandSourceStack>): Boolean {
        val emailConfirm: String = StringArgumentType.getString(context, "emailConfirm")
        val sender: ServerPlayer = context.getSource().playerOrException
        //not logged in
        if (!LoginService.isLoggedIn(context.getSource().playerOrException.getUUID())) {
            context.getSource().sendFailure(TranslatableComponent("you cannot use this feature while unlogged"))
            return false
        }

        val account: java.util.Optional<PlayerAccount> = AccountService.getAccount(sender.getUUID())
        if (account.isPresent && emailConfirm == account.get().email) {
            val randomCode: String? = RandomGenerator(20).generate()
            if (EmailService.sendEmail(sender.getUUID(), emailConfirm, randomCode)) {
                code[sender.getUUID()] = randomCode
                context.getSource().sendSuccess(
                    TranslatableComponent("commands.email.bind.success").withStyle(ChatFormatting.BOLD)
                        .withStyle(ChatFormatting.GOLD), true
                )
                return true
            } else {
                context.getSource().sendFailure(TranslatableComponent("email service error or it hasn't been enabled"))
            }
        } else {
            context.getSource().sendFailure(TranslatableComponent("email is not truee"))
        }
        return false
    }

    /***
     * if the confirm code is consistent. force login current player
     * @param context
     * @return
     * @throws CommandSyntaxException
     */
    @Throws(CommandSyntaxException::class)
    fun confirmRecover(context: CommandContext<CommandSourceStack>): Boolean {
        val confirmCode: String = StringArgumentType.getString(context, "confirmCode")
        val uuid: java.util.UUID = context.source.playerOrException.getUUID()
        val account: java.util.Optional<PlayerAccount> = AccountService.getAccount(uuid)

        //not logged in
        if (!LoginService.isLoggedIn(context.getSource().playerOrException.getUUID())) {
            context.getSource().sendFailure(TranslatableComponent("you cannot use this feature while unlogged"))
            return false
        }

        if (code.containsKey(uuid) && account.isPresent) {
            if (confirmCode == code.get(uuid)) {
                code.remove(uuid)
                //force login
                context.getSource().sendSuccess(
                    TranslatableComponent("commands.login.success").withStyle(ChatFormatting.GREEN)
                        .withStyle(ChatFormatting.BOLD), true
                )
                MinecraftForge.EVENT_BUS.post(
                    EasyLoginEvents.PlayerLoginEvent(
                        context.getSource().playerOrException, account.get()
                    )
                )
                return true
            }
        }
        return false
    }
}
