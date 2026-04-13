package cn.thesilentnights.easylogin.service;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import cn.thesilentnights.easylogin.pojo.PlayerAccount;
import cn.thesilentnights.easylogin.utils.MessageSender;
import cn.thesilentnights.easylogin.utils.MessageSender.MessageType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.GameProfileArgument;

import java.util.Collection;
import java.util.Optional;

public class PlayerInfoService {
    public static boolean handle(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<GameProfile> player = GameProfileArgument.getGameProfiles(context, "player");

        Optional<PlayerAccount> account = AccountService.getAccount(player.iterator().next().getId());

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
