package com.thesilentnights.easylogin.service;

import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.thesilentnights.easylogin.configs.EasyLoginConfig;
import com.thesilentnights.easylogin.pojo.PlayerAccount;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

public class EmailService {

    private static final Logger log = LogManager.getLogger(EmailService.class);
    private static MailAccount account;

    static {
        if (EasyLoginConfig.enableEmailFunction.get()) {
            account = new MailAccount();
            account.setFrom(EasyLoginConfig.from.get());
            account.setUser(EasyLoginConfig.username.get());
            account.setPass(EasyLoginConfig.password.get());
            account.setHost(EasyLoginConfig.host.get());
            account.setPort(EasyLoginConfig.port.get());
            account.setAuth(EasyLoginConfig.enableSSL.get());
            account.setStarttlsEnable(EasyLoginConfig.starttlsEnable.get());
            account.setSslEnable(EasyLoginConfig.enableSSL.get());
            account.setTimeout(EasyLoginConfig.timeout.get());
        }
    }

    public static boolean checkIfInitiated() {
        return account != null;
    }

    public static boolean sendEmail(UUID sender, String emailAddress, String data) {
        String identifier = TimerService.generateIdentifier(sender, EmailService.class);
        if (TimerService.contains(identifier)) {
            return false;
        } else {
            MailUtil.send(account, emailAddress, "com.thesilentnights.easylogin.EasyLogin", data, false);
            TimerService.add(identifier, 1000L * 60 * 5);
            return true;
        }
    }

    public static boolean sendDebug(CommandContext<CommandSourceStack> context) {
        if (!checkIfInitiated()) {
            context.getSource().sendFailure(new TextComponent("the email feature is not enabled. you can modify it in config.yml"));
        }
        String targetEmail = StringArgumentType.getString(context, "targetEmail");
        MailUtil.send(account, targetEmail, "com.thesilentnights.easylogin.EasyLogin", "testEmail", false);
        return true;
    }

    public static boolean bindEmail(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        if (!checkIfInitiated()) {
            context.getSource().sendFailure(new TextComponent("the email feature is not enabled"));
            return false;
        }

        String email = StringArgumentType.getString(context, "email");
        ServerPlayer player = context.getSource().getPlayerOrException();
        UUID uuid = player.getUUID();

        if (!LoginService.isLoggedIn(uuid)) {
            context.getSource().sendFailure(new TranslatableComponent("you cannot execute it when you are not logged in"));
            return false;
        }

        if (isValidEmail(email)) {
            Optional<PlayerAccount> accountOpt = AccountService.getAccount(uuid);
            if (accountOpt.isPresent()) {
                PlayerAccount playerAccount = accountOpt.get();
                playerAccount.setEmail(email);
                log.info(playerAccount.toString());
                AccountService.updateAccount(playerAccount);
                context.getSource().sendSuccess(
                        new TranslatableComponent("bind success")
                                .withStyle(ChatFormatting.GREEN)
                                .withStyle(ChatFormatting.BOLD),
                        false
                );
                return true;
            } else {
                context.getSource().sendFailure(
                        new TranslatableComponent("commands.email.bind.failure")
                                .withStyle(ChatFormatting.RED)
                                .withStyle(ChatFormatting.BOLD)
                );
            }
        } else {
            log.info("error");
        }
        return false;
    }

    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return Pattern.matches("^(\\w+([-.][A-Za-z0-9]+)*){3,18}@\\w+([-.][A-Za-z0-9]+)*\\.\\w+([-.][A-Za-z0-9]+)*$", email);
    }
}