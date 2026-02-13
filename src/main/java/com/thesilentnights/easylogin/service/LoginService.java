package com.thesilentnights.easylogin.service;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.thesilentnights.easylogin.pojo.PlayerAccount;
import com.thesilentnights.easylogin.repo.PlayerCache;
import com.thesilentnights.easylogin.repo.PlayerSessionCache;
import com.thesilentnights.easylogin.utils.LogUtil;
import com.thesilentnights.easylogin.utils.TextUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class LoginService {

    public static boolean login(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer serverPlayer = context.getSource().getPlayerOrException();
        UUID uuid = serverPlayer.getUUID();

        if (!AccountService.hasAccount(uuid)) {
            context.getSource().sendFailure(TextUtil.serialize(TextUtil.FormatType.FAILURE, "you haven't registered"));
            return true;
        }

        if (PlayerCache.hasAccount(uuid)) {

            context.getSource().sendFailure(
                    TextUtil.serialize(
                            TextUtil.FormatType.FAILURE,
                            Component.translatable("account.already_loggedin")
                    )
            );
            return false;
        }

        String password = StringArgumentType.getString(context, "password");
        Optional<PlayerAccount> account = AccountService.getAccount(uuid);

        if (account.isPresent()) {
            if (account.get().getPassword().equals(password)) {

                context.getSource().sendSuccess(
                        () -> TextUtil.serialize(TextUtil.FormatType.SUCCESS, Component.translatable("commands.login.success", serverPlayer.getGameProfile().getName()))
                        , false
                );
                removeLimit(account.get(), serverPlayer);
                return true;
            }
        }

        context.getSource().sendFailure(
                TextUtil.serialize(TextUtil.FormatType.FAILURE, Component.translatable("commands.login.failure", serverPlayer.getGameProfile().getName()))
        );
        return false;
    }

    public static void forceLogin(PlayerAccount account, ServerPlayer serverPlayer) {
        removeLimit(account, serverPlayer);
    }

    private static void removeLimit(PlayerAccount account, ServerPlayer serverPlayer) {
        PlayerCache.addAccount(account);
        TaskService.cancelPlayer(serverPlayer.getUUID());
        serverPlayer.removeEffect(MobEffects.BLINDNESS);
    }

    public static boolean register(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer serverPlayer = context.getSource().getPlayerOrException();
        String password = StringArgumentType.getString(context, "password");
        String repeat = StringArgumentType.getString(context, "repeat");

        if (!password.equals(repeat)) {
            context.getSource().sendFailure(
                    TextUtil.serialize(TextUtil.FormatType.FAILURE, Component.translatable("commands.password.confirm.failure"))
            );
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
                serverPlayer.serverLevel().dimension().location().getNamespace(),
                System.currentTimeMillis()
        );

        AccountService.updateAccount(newAccount);

        Optional<PlayerAccount> auth = AccountService.getAccount(serverPlayer.getUUID());
        if (auth.isEmpty()) {
            LogUtil.logError(LoginService.class, "sql error found in registering player", new SQLException());
            return false;
        } else {

            context.getSource().sendSuccess(
                    () -> TextUtil.serialize(
                            TextUtil.FormatType.SUCCESS, Component.translatable(
                                    "commands.login.success",
                                    serverPlayer.getGameProfile().getName()
                            )
                    ), false
            );
            removeLimit(auth.get(), serverPlayer);
            return true;
        }
    }

    public static void logoutPlayer(ServerPlayer serverPlayer) {
        Optional<PlayerAccount> account = PlayerCache.getAccount(serverPlayer.getUUID());
        if (account.isPresent() && !account.get().isFake()) {
            PlayerAccount playerAccount = account.get();
            playerAccount.setLastLoginIp(serverPlayer.getIpAddress());
            playerAccount.setLastLoginWorld(serverPlayer.serverLevel().dimension().location().getNamespace());
            playerAccount.setLastLoginX(serverPlayer.getX());
            playerAccount.setLastLoginY(serverPlayer.getY());
            playerAccount.setLastLoginZ(serverPlayer.getZ());
            playerAccount.setLoginTimestamp(System.currentTimeMillis());
            AccountService.updateAccount(playerAccount);
            PlayerCache.dropAccount(serverPlayer.getUUID(), true);
            TaskService.cancelPlayer(serverPlayer.getUUID());
        }
    }

    public static boolean isLoggedIn(UUID key) {
        return PlayerCache.hasAccount(key);
    }

    public static boolean reLogFromCache(ServerPlayer serverPlayer) {
        if (!PlayerSessionCache.hasSession(serverPlayer.getUUID())) {
            return false;
        }

        PlayerAccount account = PlayerSessionCache.getSession(serverPlayer.getUUID()).getAccount();
        removeLimit(account, serverPlayer);
        return true;
    }


}