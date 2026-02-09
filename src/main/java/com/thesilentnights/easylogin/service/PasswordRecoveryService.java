package com.thesilentnights.easylogin.service;

import cn.hutool.captcha.generator.RandomGenerator;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.thesilentnights.easylogin.pojo.PlayerAccount;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class PasswordRecoveryService {

    private static final Map<UUID, String> code = new HashMap<>();


    public static boolean recoveryPassword(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String emailConfirm = StringArgumentType.getString(context, "emailConfirm");
        ServerPlayer sender = context.getSource().getPlayerOrException();

        if (!LoginService.isLoggedIn(sender.getUUID())) {
            context.getSource().sendFailure(new TranslatableComponent("you cannot use this feature while unlogged"));
            return false;
        }

        Optional<PlayerAccount> account = AccountService.getAccount(sender.getUUID());
        if (account.isPresent() && emailConfirm.equals(account.get().getEmail())) {
            String randomCode = new RandomGenerator(20).generate();
            if (EmailService.sendEmail(sender.getUUID(), emailConfirm, randomCode)) {
                code.put(sender.getUUID(), randomCode);
                context.getSource().sendSuccess(
                        new TranslatableComponent("commands.email.bind.success")
                                .withStyle(ChatFormatting.BOLD)
                                .withStyle(ChatFormatting.GOLD),
                        true
                );
                return true;
            } else {
                context.getSource().sendFailure(new TranslatableComponent("email service error or it hasn't been enabled"));
            }
        } else {
            context.getSource().sendFailure(new TranslatableComponent("email is not truee"));
        }
        return false;
    }

    public static boolean confirmRecover(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String confirmCode = StringArgumentType.getString(context, "confirmCode");
        UUID uuid = context.getSource().getPlayerOrException().getUUID();
        Optional<PlayerAccount> account = AccountService.getAccount(uuid);

        if (!LoginService.isLoggedIn(uuid)) {
            context.getSource().sendFailure(new TranslatableComponent("you cannot use this feature while unlogged"));
            return false;
        }

        if (code.containsKey(uuid) && account.isPresent()) {
            if (confirmCode.equals(code.get(uuid))) {
                code.remove(uuid);
                context.getSource().sendSuccess(
                        new TranslatableComponent("commands.login.success")
                                .withStyle(ChatFormatting.GREEN)
                                .withStyle(ChatFormatting.BOLD),
                        true
                );
                //TODO remove limit
                return true;
            }
        }
        return false;
    }
}