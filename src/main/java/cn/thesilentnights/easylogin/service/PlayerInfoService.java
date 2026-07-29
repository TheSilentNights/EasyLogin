package cn.thesilentnights.easylogin.service;

import cn.thesilentnights.easylogin.configs.EasyLoginConfig;
import cn.thesilentnights.easylogin.pojo.PlayerExtraData;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

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

        if (!EasyLoginConfig.enableExtraDataRecord.get()){
             MessageSender.sendMessage(
                    context,
                    "Extra data record is disabled",
                    MessageType.ERROR);
        }

        Optional<PlayerExtraData> account = DataService.getPlayerExtraData(next.id());

        if (account.isEmpty()) {
            MessageSender.sendMessage(context, "Player not found", MessageType.ERROR);
            return false;
        }

        MessageSender.sendMessage(
                context,
                "Player info: " + account.get(),
                MessageType.SUCCESS
        );
        return true;
    }
}
