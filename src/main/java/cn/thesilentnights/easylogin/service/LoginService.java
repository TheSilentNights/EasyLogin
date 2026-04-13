package cn.thesilentnights.easylogin.service;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import cn.thesilentnights.easylogin.pojo.PlayerAccount;
import cn.thesilentnights.easylogin.repo.PlayerCache;
import cn.thesilentnights.easylogin.repo.PlayerSessionCache;
import cn.thesilentnights.easylogin.utils.LogUtil;
import cn.thesilentnights.easylogin.utils.MessageSender;
import cn.thesilentnights.easylogin.utils.MessageSender.MessageType;
import net.minecraft.commands.CommandSourceStack;
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
            MessageSender.sendMessage(context, "you haven't registered", MessageType.ERROR);
            return true;
        }

        if (PlayerCache.hasAccount(uuid)) {
            MessageSender.sendMessage(
                    serverPlayer,
                    "you are already logged in",
                    MessageType.ERROR
            );
            return false;
        }

        String password = StringArgumentType.getString(context, "password");
        Optional<PlayerAccount> account = AccountService.getAccount(uuid);

        if (account.isPresent()) {
            if (account.get().getPassword().equals(password)) {

                MessageSender.sendMessage(
                        serverPlayer,
                        "login success",
                        MessageType.SUCCESS
                );
                removeLimit(account.get(), serverPlayer);
                return true;
            }
        }

        MessageSender.sendMessage(
                serverPlayer,
                "login failed",
                MessageType.ERROR
        );
        return false;
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
            MessageSender.sendMessage(
                    serverPlayer,
                    "password confirm failed",
                    MessageType.ERROR
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
                serverPlayer.level().dimension().location().getNamespace(),
                System.currentTimeMillis()
        );

        AccountService.updateAccount(newAccount);

        Optional<PlayerAccount> auth = AccountService.getAccount(serverPlayer.getUUID());
        if (auth.isEmpty()) {
            LogUtil.getLogger().error("internal error found in registering player", new SQLException());
            return false;
        } else {
            MessageSender.sendMessage(
                    serverPlayer,
                    "register success",
                    MessageType.SUCCESS
            );
            removeLimit(auth.get(), serverPlayer);
            return true;
        }
    }

    public static void logoutPlayer(ServerPlayer serverPlayer) {
        Optional<PlayerAccount> account = AccountService.getAccount(serverPlayer.getUUID());
        if (account.isPresent()) {
            PlayerAccount playerAccount = account.get();
            playerAccount.setLastLoginIp(serverPlayer.getIpAddress());
            playerAccount.setLastLoginWorld(serverPlayer.level().dimension().location().getNamespace());
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