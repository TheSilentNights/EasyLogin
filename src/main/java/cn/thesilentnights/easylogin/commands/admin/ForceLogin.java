package cn.thesilentnights.easylogin.commands.admin;

import cn.thesilentnights.easylogin.commands.ICommands;
import cn.thesilentnights.easylogin.commands.PermissionRequired;
import cn.thesilentnights.easylogin.services.auth.LoginService;
import cn.thesilentnights.easylogin.utils.Dependencies;
import cn.thesilentnights.easylogin.utils.MessageSender;
import cn.thesilentnights.easylogin.utils.MessageSender.MessageType;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.NameAndId;

public class ForceLogin extends PermissionRequired implements ICommands {

        @Override
        public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
                var bypass = Commands.literal("forceLogin").requires(
                        (sourceStack) ->
                                requireAdminPermission(sourceStack) && requireLoginAuth(sourceStack)
                );
                var playerArg = Commands.argument("player", GameProfileArgument.gameProfile());

                bypass.then(playerArg.executes(
                        (CommandContext<CommandSourceStack> context) -> {
                                var player = GameProfileArgument.getGameProfiles(context, "player");
                                if (player.isEmpty()) {
                                        MessageSender.sendMessage(
                                                context.getSource().getPlayerOrException(),
                                                "player not found",
                                                MessageType.ERROR
                                        );
                                        return 0;
                                }
                                NameAndId next = player.iterator().next();

                                ServerPlayer serverPlayer = context
                                        .getSource()
                                        .getServer()
                                        .getPlayerList()
                                        .getPlayer(next.id());


                                Dependencies.getDependency(LoginService.class).forceLogin(serverPlayer);

                                MessageSender.sendMessage(
                                        context.getSource().getPlayerOrException(),
                                        "forceLogin success",
                                        MessageType.SUCCESS
                                );
                                return 1;
                        }));
        }

}
