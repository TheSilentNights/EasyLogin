package com.thesilentnights.easylogin.service;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.thesilentnights.easylogin.pojo.PlayerAccount;
import com.thesilentnights.easylogin.repo.PlayerCache;
import com.thesilentnights.easylogin.repo.PlayerSessionCache;
import com.thesilentnights.easylogin.utils.LogUtil;
import com.thesilentnights.easylogin.utils.TextUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class LoginService {
    private final AccountService accountService;

    public LoginService(AccountService accountService) {
        this.accountService = accountService;
    }

    public boolean login(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer serverPlayer = context.getSource().getPlayerOrException();
        UUID uuid = serverPlayer.getUUID();

        if (!accountService.hasAccount(uuid)) {
            context.getSource().sendFailure(TextUtil.createText(ChatFormatting.RED, "you haven't registered"));
            return true;
        }

        if (PlayerCache.hasAccount(uuid)) {
            MutableComponent message = new TranslatableComponent("account.already_loggedin", serverPlayer.getGameProfile().getName())
                    .withStyle(ChatFormatting.GREEN)
                    .withStyle(ChatFormatting.BOLD);
            context.getSource().sendFailure(message);
            return false;
        }

        String password = StringArgumentType.getString(context, "password");
        Optional<PlayerAccount> account = accountService.getAccount(uuid);

        if (account.isPresent()) {
            if (account.get().getPassword().equals(password)) {
                MutableComponent successMessage = new TranslatableComponent("commands.login.success", serverPlayer.getDisplayName().getString())
                        .withStyle(ChatFormatting.GREEN)
                        .withStyle(ChatFormatting.BOLD);
                context.getSource().sendSuccess(successMessage, false);
                removeLimit(account.get(), serverPlayer);
                return true;
            }
        }

        MutableComponent failureMessage = new TranslatableComponent("commands.login.failure")
                .withStyle(ChatFormatting.RED)
                .withStyle(ChatFormatting.BOLD);
        context.getSource().sendFailure(failureMessage);
        return false;
    }

    private void removeLimit(PlayerAccount account, ServerPlayer serverPlayer) {
        PlayerCache.addAccount(account);
        TaskService.cancelPlayer(serverPlayer.getUUID());
        serverPlayer.removeEffect(MobEffects.BLINDNESS);
    }

    public boolean register(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer serverPlayer = context.getSource().getPlayerOrException();
        String password = StringArgumentType.getString(context, "password");
        String repeat = StringArgumentType.getString(context, "repeat");

        if (!password.equals(repeat)) {
            MutableComponent message = new TranslatableComponent("commands.password.confirm.failure")
                    .withStyle(ChatFormatting.RED)
                    .withStyle(ChatFormatting.BOLD);
            context.getSource().sendFailure(message);
            return false;
        }

        PlayerAccount newAccount = new PlayerAccount(
                serverPlayer.getUUID(),
                serverPlayer.getGameProfile().getName(),
                password,
                serverPlayer.getIpAddress(),
                serverPlayer.getX(),
                serverPlayer.getY(),
                serverPlayer.getZ(),
                serverPlayer.getLevel().dimension().location().getNamespace(),
                null,
                System.currentTimeMillis()
        );

        accountService.updateAccount(newAccount);

        Optional<PlayerAccount> auth = accountService.getAccount(serverPlayer.getUUID());
        if (auth.isEmpty()) {
            LogUtil.logError(LoginService.class, "sql error found in registering player", new SQLException());
            return false;
        } else {
            MutableComponent successMessage = new TranslatableComponent("commands.login.success")
                    .withStyle(ChatFormatting.GREEN)
                    .withStyle(ChatFormatting.BOLD);
            context.getSource().sendSuccess(successMessage, false);
            removeLimit(auth.get(), serverPlayer);
            return true;
        }
    }

    public void logoutPlayer(ServerPlayer serverPlayer) {
        Optional<PlayerAccount> account = PlayerCache.getAccount(serverPlayer.getUUID());
        if (account.isPresent()) {
            PlayerAccount playerAccount = account.get();
            playerAccount.setLastLoginIp(serverPlayer.getIpAddress());
            playerAccount.setLastLoginWorld(serverPlayer.getLevel().dimension().location().getNamespace());
            playerAccount.setLastLoginX(serverPlayer.getX());
            playerAccount.setLastLoginY(serverPlayer.getY());
            playerAccount.setLastLoginZ(serverPlayer.getZ());
            playerAccount.setLoginTimestamp(System.currentTimeMillis());
            accountService.updateAccount(playerAccount);
            PlayerCache.dropAccount(serverPlayer.getUUID(), true);
        }
    }

    public boolean isLoggedIn(UUID key) {
        return PlayerCache.hasAccount(key);
    }

    public boolean reLogFromCache(ServerPlayer serverPlayer) {
        if (!PlayerSessionCache.hasSession(serverPlayer.getUUID())) {
            return false;
        }

        PlayerAccount account = PlayerSessionCache.getSession(serverPlayer.getUUID()).getAccount();
        removeLimit(account, serverPlayer);
        return true;
    }


}