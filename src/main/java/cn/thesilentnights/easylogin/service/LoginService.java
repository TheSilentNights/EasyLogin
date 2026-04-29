package cn.thesilentnights.easylogin.service;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import cn.thesilentnights.easylogin.pojo.PlayerAccount;
import cn.thesilentnights.easylogin.repo.PlayerCache;
import cn.thesilentnights.easylogin.utils.LogUtil;
import cn.thesilentnights.easylogin.utils.MessageSender;
import cn.thesilentnights.easylogin.utils.MessageSender.MessageType;
import cn.thesilentnights.easylogin.utils.PasswordHasher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class LoginService {

    public static boolean login(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer serverPlayer = context.getSource().getPlayerOrException();
        UUID uuid = serverPlayer.getUUID();

        // bypass
        if (ByPassService.isBypassed(uuid)) {
            return true;
        }

        if (!AccountService.hasAccount(uuid)) {
            MessageSender.sendMessage(context, "you haven't registered", MessageType.ERROR);
            return false;
        }

        if (PlayerCache.hasAccount(uuid)) {
            MessageSender.sendMessage(
                    serverPlayer,
                    "you are already logged in",
                    MessageType.ERROR);
            return false;
        }

        String password = StringArgumentType.getString(context, "password");
        Optional<PlayerAccount> account = AccountService.getAccount(uuid);

        if (account.isPresent()) {
            if (PasswordHasher.verify(password, account.get().getPassword())) {

                MessageSender.sendMessage(
                        serverPlayer,
                        "login success",
                        MessageType.SUCCESS);

                PlayerCache.addAccount(account.get());
                removeLimit(serverPlayer);
                return true;
            } else {
                MessageSender.sendMessage(
                        serverPlayer,
                        "password failed",
                        MessageType.ERROR);
                return false;
            }
        }

        MessageSender.sendMessage(
                serverPlayer,
                "login failed",
                MessageType.ERROR);
        return false;
    }

    public static boolean fakeLogin(ServerPlayer serverPlayer) {
        removeLimit(serverPlayer);
        return true;
    }

    private static void removeLimit( ServerPlayer serverPlayer) {
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
                    MessageType.ERROR);
            return false;
        }

        PlayerAccount newAccount = new PlayerAccount(
                serverPlayer.getUUID(),
                serverPlayer.getGameProfile().getName(),
                PasswordHasher.hash(password),
                serverPlayer.getIpAddress(),
                serverPlayer.getX(),
                serverPlayer.getY(),
                serverPlayer.getZ(),
                serverPlayer.level().dimension().location().getNamespace(),
                System.currentTimeMillis());
        // data check
        AccountService.updateAccount(newAccount);

        Optional<PlayerAccount> auth = AccountService.getAccount(serverPlayer.getUUID());
        if (auth.isEmpty()) {
            LogUtil.getLogger().error("internal error found in registering player", new SQLException());
            return false;
        } else {
            MessageSender.sendMessage(
                    serverPlayer,
                    "register success",
                    MessageType.SUCCESS);
            PlayerCache.addAccount(auth.get());
            removeLimit(serverPlayer);
            return true;
        }
    }

    public static void logoutPlayer(ServerPlayer serverPlayer) {

        if (ByPassService.isBypassed(serverPlayer.getUUID())) {
            ByPassService.removeBypass(serverPlayer.getUUID());
            return;
        }

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
            PlayerCache.dropAccount(serverPlayer.getUUID());
            TaskService.cancelPlayer(serverPlayer.getUUID());
        }
    }

    public static boolean isLoggedIn(UUID key) {
        return PlayerCache.hasAccount(key) || ByPassService.isBypassed(key);
    }

}