package cn.thesilentnights.easylogin.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import cn.thesilentnights.easylogin.service.ByPassService;
import cn.thesilentnights.easylogin.service.LoginService;
import cn.thesilentnights.easylogin.utils.MessageSender;
import cn.thesilentnights.easylogin.utils.MessageSender.MessageType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.GameProfileArgument;

public class ByPass implements ICommands {

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        var bypass = Commands.literal("bypass");
        var playerArg = Commands.argument("player", GameProfileArgument.gameProfile());

        bypass.then(playerArg.executes(
                (CommandContext<CommandSourceStack> context) -> {
                    var player = GameProfileArgument.getGameProfiles(context, "player");
                    if (player.isEmpty()) {
                        MessageSender.sendMessage(
                                context.getSource().getPlayerOrException(),
                                "player not found",
                                MessageType.ERROR);
                        return 0;
                    }
                    GameProfile gameProfile = player.iterator().next();
                    ByPassService.addBypass(gameProfile.getId());
                    LoginService.fakeLogin(context.getSource().getServer().getPlayerList().getPlayer(gameProfile.getId()));
                    
                    MessageSender.sendMessage(
                            context.getSource().getPlayerOrException(),
                            "bypass success",
                            MessageType.SUCCESS);
                    return 1;
                }));
    }

}
