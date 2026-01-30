package com.thesilentnights.easylogin.service

import cn.hutool.extra.mail.MailAccount
import cn.hutool.extra.mail.MailUtil
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.thesilentnights.configs.EasyLoginConfig
import com.thesilentnights.easylogin.pojo.PlayerAccount
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.TranslatableComponent

@Slf4j
object EmailService {
    private var account: MailAccount? = null

    fun init(config: EasyLoginConfig) {
        EmailService.account = MailAccount()
        EmailService.account.setFrom(config.getMailAccountEntry().getFrom())
        EmailService.account.setUser(config.getMailAccountEntry().getUser())
        EmailService.account.setPass(config.getMailAccountEntry().getPassword())
        EmailService.account.setHost(config.getMailAccountEntry().getHost())
        EmailService.account.setPort(config.getMailAccountEntry().getPort())
        EmailService.account.setAuth(config.getMailAccountEntry().isEnableSSL())
        EmailService.account.setStarttlsEnable(config.getMailAccountEntry().isStarttlsEnable())
        EmailService.account.setSslEnable(config.getMailAccountEntry().isEnableSSL())
        EmailService.account.setTimeout(config.getMailAccountEntry().getTimeout())
    }

    fun checkIfInitiated(): Boolean {
        return EmailService.account != null
    }

    fun sendEmail(sender: java.util.UUID?, emailAddress: kotlin.String?, data: kotlin.String?): Boolean {
        if (TimerService.contains(TimerService.generateIdentifier(sender, EmailService::class.java))) {
            return false
        } else {
            MailUtil.send(EmailService.account, emailAddress, "EasyLogin", data, false)
            TimerService.add(
                TimerService.generateIdentifier(sender, EmailService::class.java),
                (1000 * 60 * 5).toLong()
            )
            return true
        }
    }

    fun sendDebug(context: CommandContext<CommandSourceStack?>): Boolean {
        if (!EmailService.checkIfInitiated()) {
            context.getSource()
                .sendFailure(net.minecraft.network.chat.TextComponent("the email feature is not enabled. you can modify it in config.yml"))
        }
        MailUtil.send(
            EmailService.account,
            StringArgumentType.getString(context, "targetEmail"),
            "EasyLogin",
            "testEmail",
            false
        )
        return true
    }

    @kotlin.Throws(CommandSyntaxException::class)
    fun bindEmail(context: CommandContext<CommandSourceStack?>): Boolean {
        if (!EmailService.checkIfInitiated()) {
            context.getSource()
                .sendFailure(net.minecraft.network.chat.TextComponent("the email feature is not enabled"))
        }
        val email: kotlin.String? = StringArgumentType.getString(context, "email")
        if (!LoginService.isLoggedIn(context.getSource().getPlayerOrException().getUUID())) {
            context.getSource().sendFailure(TranslatableComponent("you cannot execute it when you are not logged in"))
            return false
        }
        //check the consistence
        if (EmailService.isValidEmail(email)) {
            val account1: java.util.Optional<PlayerAccount> =
                AccountService.getAccount(context.getSource().getPlayerOrException().getUUID())

            if (account1.isPresent()) {
                val playerAccount: PlayerAccount = account1.get()
                playerAccount.setEmail(email)
                log.info(playerAccount.toString())
                AccountService.updateAccount(playerAccount)
                context.getSource().sendSuccess(
                    TranslatableComponent("bind success").withStyle(ChatFormatting.GREEN)
                        .withStyle(ChatFormatting.BOLD), false
                )
                return true
            } else {
                context.getSource().sendFailure(
                    TranslatableComponent("commands.email.bind.failure").withStyle(ChatFormatting.RED)
                        .withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.RED)
                )
            }
        } else {
            log.info("error")
        }
        return false
    }

    fun isValidEmail(email: kotlin.String?): Boolean {
        if ((email != null) && (!email.isEmpty())) {
            return java.util.regex.Pattern.matches(
                "^(\\w+([-.][A-Za-z0-9]+)*){3,18}@\\w+([-.][A-Za-z0-9]+)*\\.\\w+([-.][A-Za-z0-9]+)*$",
                email
            )
        }
        return false
    }
}
