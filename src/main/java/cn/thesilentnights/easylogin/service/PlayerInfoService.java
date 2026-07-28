package cn.thesilentnights.easylogin.service;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import cn.thesilentnights.easylogin.pojo.PlayerPasswordData;
import cn.thesilentnights.easylogin.utils.MessageSender;
import cn.thesilentnights.easylogin.utils.MessageSender.MessageType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.server.players.NameAndId;


import java.util.Collection;
import java.util.Optional;

public class PlayerInfoService {
    public static boolean handle(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<NameAndId> player = GameProfileArgument.getGameProfiles(context, "player");

        if (player.isEmpty()) {
            MessageSender.sendMessage(
                    context,
                    "player not found",
                    MessageType.ERROR);
            return false;
        }
        
        NameAndId next = player.iterator().next();

        Optional<PlayerPasswordData> account = AccountService.getAccount(next.id());

        if (account.isEmpty()) {
            MessageSender.sendMessage(context, "Player not found", MessageType.ERROR);
            return false;
        }

        MessageSender.sendMessage(
                context,
                "Player info: " + account.get().toString(),
                MessageType.SUCCESS
        );
        return true;
    }
}
