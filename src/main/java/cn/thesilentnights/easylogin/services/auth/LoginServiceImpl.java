package cn.thesilentnights.easylogin.services.auth;

import cn.thesilentnights.easylogin.pojo.PlayerPasswordData;
import cn.thesilentnights.easylogin.repo.PlayerCache;
import cn.thesilentnights.easylogin.repo.PositionRepo;
import cn.thesilentnights.easylogin.services.data.DataService;
import cn.thesilentnights.easylogin.services.task.TaskService;
import cn.thesilentnights.easylogin.utils.LogUtil;
import cn.thesilentnights.easylogin.utils.MessageSender;
import cn.thesilentnights.easylogin.utils.MessageSender.MessageType;
import cn.thesilentnights.easylogin.utils.PasswordHasher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.logging.LogUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class LoginServiceImpl implements LoginService {
        private final DataService dataService;
        private final TaskService taskService;

        public LoginServiceImpl(DataService dataService, TaskService taskService) {
                this.dataService = dataService;
                this.taskService = taskService;
        }

        @Override
        public boolean login(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {

                ServerPlayer serverPlayer = context.getSource().getPlayerOrException();
                UUID uuid = serverPlayer.getUUID();


                if (!dataService.hasAccount(uuid)) {
                        MessageSender.sendMessage(
                                context,
                                "you haven't registered",
                                MessageType.ERROR
                        );
                        return false;
                }

                if (PlayerCache.isPlayerLogged(uuid)) {
                        MessageSender.sendMessage(
                                serverPlayer,
                                "you are already logged in",
                                MessageType.ERROR
                        );
                        return false;
                }

                String password = StringArgumentType.getString(context, "password");
                Optional<PlayerPasswordData> account = dataService.getPlayerPasswordData(uuid);

                if (account.isPresent()) {
                        if (PasswordHasher.verify(password, account.get().getPassword())) {

                                MessageSender.sendMessage(
                                        serverPlayer,
                                        "login success",
                                        MessageType.SUCCESS
                                );

                                PlayerCache.addPlayer(uuid);
                                removeLimit(serverPlayer);
                                return true;
                        } else {
                                MessageSender.sendMessage(
                                        serverPlayer,
                                        "password failed",
                                        MessageType.ERROR
                                );
                                return false;
                        }
                }

                MessageSender.sendMessage(
                        serverPlayer,
                        "login failed",
                        MessageType.ERROR
                );
                return false;
        }

        @Override
        public void forceLogin(ServerPlayer serverPlayer) {
                removeLimit(serverPlayer);
                PlayerCache.addPlayer(serverPlayer.getUUID());
        }

        private void removeLimit(ServerPlayer serverPlayer) {
                taskService.cancelPlayer(serverPlayer.getUUID());
                PositionRepo.removePos(serverPlayer.getUUID());
                serverPlayer.removeEffect(MobEffects.BLINDNESS);
        }

        @Override
        public boolean register(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {

                ServerPlayer serverPlayer = context.getSource().getPlayerOrException();
                String password = StringArgumentType.getString(context, "password");
                String repeat = StringArgumentType.getString(context, "repeat");

                UUID uuid = serverPlayer.getUUID();

                if (!password.equals(repeat)) {
                        MessageSender.sendMessage(
                                serverPlayer,
                                "password confirm failed",
                                MessageType.ERROR
                        );
                        return false;
                }

                // data check
                dataService.updatePassword(uuid, password);

                Optional<PlayerPasswordData> auth = dataService.getPlayerPasswordData(uuid);
                if (auth.isEmpty()) {
                        LogUtil.getLogger().error(
                                "internal error found in registering player",
                                new SQLException()
                        );
                        return false;
                } else {
                        MessageSender.sendMessage(
                                serverPlayer,
                                "register success",
                                MessageType.SUCCESS
                        );
                        PlayerCache.addPlayer(uuid);
                        removeLimit(serverPlayer);
                        return true;
                }
        }

        @Override
        public void logout(ServerPlayer serverPlayer) {
                //
                if (!PlayerCache.isPlayerLogged(serverPlayer.getUUID())) {
                        MessageSender.sendMessage(
                                serverPlayer,
                                "you are not logged in",
                                MessageType.ERROR
                        );
                    LogUtils.getLogger().info("player not logged out");
                } else {
                        // save player status
                        LogUtils.getLogger().info("player logged out");
                        dataService.recordPlayer(serverPlayer);
                        PlayerCache.dropPlayerLogged(serverPlayer.getUUID());
                        taskService.cancelPlayer(serverPlayer.getUUID());
                }
        }

        @Override
        public boolean isPlayerLogged(UUID uuid) {
                return PlayerCache.isPlayerLogged(uuid);
        }

}
