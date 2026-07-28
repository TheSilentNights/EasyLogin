package cn.thesilentnights.easylogin.service;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import cn.thesilentnights.easylogin.pojo.PlayerPasswordData;
import cn.thesilentnights.easylogin.repo.PlayerCache;
import cn.thesilentnights.easylogin.repo.PositionRepo;
import cn.thesilentnights.easylogin.utils.LogUtil;
import cn.thesilentnights.easylogin.utils.MessageSender;
import cn.thesilentnights.easylogin.utils.MessageSender.MessageType;
import cn.thesilentnights.easylogin.utils.PasswordHasher;
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
            return false;
        }

        if (PlayerCache.isPlayerLogged(uuid)) {
            MessageSender.sendMessage(
                    serverPlayer,
                    "you are already logged in",
                    MessageType.ERROR);
            return false;
        }

        String password = StringArgumentType.getString(context, "password");
        Optional<PlayerPasswordData> account = AccountService.getAccount(uuid);

        if (account.isPresent()) {
            if (PasswordHasher.verify(password, account.get().getPassword())) {

                MessageSender.sendMessage(
                        serverPlayer,
                        "login success",
                        MessageType.SUCCESS);

                PlayerCache.addPlayer(uuid);
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

    public static boolean forceLogin(ServerPlayer serverPlayer) {
        removeLimit(serverPlayer);
        PlayerCache.addPlayer(serverPlayer.getUUID());
        return true;
    }

    private static void removeLimit(ServerPlayer serverPlayer) {
        TaskService.cancelPlayer(serverPlayer.getUUID());
        PositionRepo.removePos(serverPlayer.getUUID());
        serverPlayer.removeEffect(MobEffects.BLINDNESS);
    }

    public static boolean register(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer serverPlayer = context.getSource().getPlayerOrException();
        String password = StringArgumentType.getString(context, "password");
        String repeat = StringArgumentType.getString(context, "repeat");

        UUID uuid = serverPlayer.getUUID();

        if (!password.equals(repeat)) {
            MessageSender.sendMessage(
                    serverPlayer,
                    "password confirm failed",
                    MessageType.ERROR);
            return false;
        }

        PlayerPasswordData newAccount = new PlayerPasswordData(
                uuid,
                PasswordHasher.hash(password));

        // data check
        AccountService.updateAccount(newAccount);

        Optional<PlayerPasswordData> auth = AccountService.getAccount(serverPlayer.getUUID());
        if (auth.isEmpty()) {
            LogUtil.getLogger().error("internal error found in registering player", new SQLException());
            return false;
        } else {
            MessageSender.sendMessage(
                    serverPlayer,
                    "register success",
                    MessageType.SUCCESS);
            PlayerCache.addPlayer(uuid);
            removeLimit(serverPlayer);
            return true;
        }
    }

    public static void logoutPlayer(ServerPlayer serverPlayer) {
        //
        if (!PlayerCache.isPlayerLogged(serverPlayer.getUUID())) {
            MessageSender.sendMessage(
                    serverPlayer,
                    "you are not logged in",
                    MessageType.ERROR);
            return;
        }

        if (PlayerCache.isPlayerLogged(serverPlayer.getUUID())) {
            PlayerCache.dropPlayerLogged(serverPlayer.getUUID());
            TaskService.cancelPlayer(serverPlayer.getUUID());
        }
    }

    public static boolean isLoggedIn(UUID key) {
        return PlayerCache.isPlayerLogged(key);
    }

}