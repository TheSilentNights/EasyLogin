package com.thesilentnights.easylogin.service

import cn.hutool.extra.mail.MailAccount
import cn.hutool.extra.mail.MailUtil
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.thesilentnights.easylogin.configs.EasyLoginConfig
import com.thesilentnights.easylogin.configs.EasyLoginConfig.username
import com.thesilentnights.easylogin.pojo.PlayerAccount
import com.thesilentnights.easylogin.utils.logError
import com.thesilentnights.easylogin.utils.logInfo
import net.minecraft.ChatFormatting
import net.minecraft.commands.CommandSourceStack
import net.minecraft.network.chat.TranslatableComponent
import java.util.*


object EmailService {
    private var account: MailAccount? = null
    private lateinit var accountService: AccountService
    private lateinit var loginService: LoginService

    fun init(accountService: AccountService, loginService: LoginService) {
        this.accountService = accountService
        this.loginService = loginService
    }

    init {
        if (EasyLoginConfig.enableEmailFunction.get()) {
            account = MailAccount()
            with(account!!) {
                from = EasyLoginConfig.from.get()
                user = username.get()
                pass = EasyLoginConfig.password.get()
                host = EasyLoginConfig.host.get()
                port = EasyLoginConfig.port.get()
                isAuth = EasyLoginConfig.enableSSL.get()
                isStarttlsEnable = EasyLoginConfig.starttlsEnable.get()
                isSslEnable = EasyLoginConfig.enableSSL.get()
                setTimeout(EasyLoginConfig.timeout.get())
            }
        }
    }

    fun checkIfInitiated(): Boolean {
        return account != null
    }

    fun sendEmail(sender: UUID, emailAddress: String, data: String?): Boolean {
        if (TimerService.contains(TimerService.generateIdentifier(sender, EmailService::class))) {
            return false
        } else {
            MailUtil.send(account, emailAddress, "EasyLogin", data, false)
            TimerService.add(
                TimerService.generateIdentifier(sender, EmailService::class),
                (1000 * 60 * 5).toLong()
            )
            return true
        }
    }

    fun sendDebug(context: CommandContext<CommandSourceStack>): Boolean {
        if (!checkIfInitiated()) {
            context.source
                .sendFailure(net.minecraft.network.chat.TextComponent("the email feature is not enabled. you can modify it in config.yml"))
        }
        MailUtil.send(
            account,
            StringArgumentType.getString(context, "targetEmail"),
            "EasyLogin",
            "testEmail",
            false
        )
        return true
    }

    @Throws(CommandSyntaxException::class)
    fun bindEmail(context: CommandContext<CommandSourceStack>): Boolean {
        if (!checkIfInitiated()) {
            context.getSource()
                .sendFailure(net.minecraft.network.chat.TextComponent("the email feature is not enabled"))
        }
        val email: String? = StringArgumentType.getString(context, "email")
        if (!loginService.isLoggedIn(context.getSource().playerOrException.getUUID())) {
            context.getSource().sendFailure(TranslatableComponent("you cannot execute it when you are not logged in"))
            return false
        }
        //check the consistence
        if (isValidEmail(email)) {
            val account1: Optional<PlayerAccount> =
                accountService.getAccount(context.getSource().playerOrException.getUUID())

            if (account1.isPresent) {
                val playerAccount: PlayerAccount = account1.get()
                playerAccount.email = (email)
                logInfo(EmailService::class, "update email")
                accountService.updateAccount(playerAccount)
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
            logError(EmailService::class, "invalid email", InvalidPropertiesFormatException("invalid email"))
        }
        return false
    }

    fun isValidEmail(email: String?): Boolean {
        if (!email.isNullOrEmpty()) {
            return java.util.regex.Pattern.matches(
                "^(\\w+([-.][A-Za-z0-9]+)*){3,18}@\\w+([-.][A-Za-z0-9]+)*\\.\\w+([-.][A-Za-z0-9]+)*$",
                email
            )
        }
        return false
    }
}
