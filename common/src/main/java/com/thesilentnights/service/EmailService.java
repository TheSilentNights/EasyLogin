package com.thesilentnights.service;

import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.thesilentnights.configs.EasyLoginConfig;
import com.thesilentnights.pojo.PlayerAccount;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

public class EmailService {
    private static EasyLoginConfig config;
    private static MailAccount account;

    public static void init(EasyLoginConfig config) {
        EmailService.config = config;
        EmailService.account = new MailAccount();
        account.setFrom(config.getMailAccountEntry().getFrom());
        account.setUser(config.getMailAccountEntry().getUser());
        account.setPass(config.getMailAccountEntry().getPassword());
        account.setHost(config.getMailAccountEntry().getHost());
        account.setPort(config.getMailAccountEntry().getPort());
        account.setAuth(config.getMailAccountEntry().isEnableSSL());
        account.setStarttlsEnable(config.getMailAccountEntry().isStarttlsEnable());
        account.setSslEnable(config.getMailAccountEntry().isEnableSSL());
        account.setTimeout(config.getMailAccountEntry().getTimeout());

    }

    public static boolean sendEmail(UUID sender, String emailAddress, String data) {
        if (TimerService.contains(TimerService.generateIdentifier(sender, EmailService.class))) {
            return false;
        } else {
            MailUtil.send(account, emailAddress, "EasyLogin", data, false);
            TimerService.add(TimerService.generateIdentifier(sender, EmailService.class), 1000 * 60 * 5);
            return true;
        }
    }

    public static boolean bindEmail(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String email = StringArgumentType.getString(context, "email");
        String emailConfirm = StringArgumentType.getString(context, "emailConfirm");
        if (email.equals(emailConfirm) && isValidEmail(email)){
            Optional<PlayerAccount> account1 = AccountService.getAccount(context.getSource().getPlayerOrException().getUUID());

            if (account1.isPresent()){
                PlayerAccount playerAccount = account1.get();
                playerAccount.setEmail(email);
                AccountService.updateAccount(playerAccount);
            }else{
                context.getSource().sendFailure(
                        new TranslatableComponent("commands.email.bind.failure").withStyle(ChatFormatting.RED).withStyle(ChatFormatting.BOLD)
                );
                return false;
            }
        }

    }

    public static boolean isValidEmail(String email) {
        if ((email != null) && (!email.isEmpty())) {
            return Pattern.matches("^(\\w+([-.][A-Za-z0-9]+)*){3,18}@\\w+([-.][A-Za-z0-9]+)*\\.\\w+([-.][A-Za-z0-9]+)*$", email);
        }
        return false;
    }


}
