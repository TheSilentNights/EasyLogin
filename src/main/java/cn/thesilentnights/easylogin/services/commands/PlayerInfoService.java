package cn.thesilentnights.easylogin.services.commands;

import cn.thesilentnights.easylogin.configs.EasyLoginConfig;
import cn.thesilentnights.easylogin.pojo.PlayerExtraData;
import cn.thesilentnights.easylogin.services.data.DataService;
import cn.thesilentnights.easylogin.utils.MessageSender.MessageType;
import cn.thesilentnights.easylogin.utils.MessageSender;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.players.NameAndId;

public class PlayerInfoService {
        public static boolean handle(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
                Collection<NameAndId> player = GameProfileArgument.getGameProfiles(context, "player");

                if (player.isEmpty()) {
                        MessageSender.sendMessage(
                                context,
                                "player not found",
                                MessageType.ERROR
                        );
                        return false;
                }

                NameAndId next = player.iterator().next();

                if (!EasyLoginConfig.enableExtraDataRecord.get()) {
                        MessageSender.sendMessage(
                                context,
                                "Extra data record is disabled",
                                MessageType.ERROR
                        );
                }

                Optional<PlayerExtraData> account = DataService.getPlayerExtraData(next.id());

                if (account.isEmpty()) {
                        MessageSender.sendMessage(context, "Player not found", MessageType.ERROR);
                        return false;
                }

                PlayerExtraData playerExtraData = account.get();
                for (Map.Entry<String, String> entry : playerExtraData.getProperties().entrySet()) {
                        MessageSender.sendMessage(
                                context,
                                entry.getKey() + ": " + entry.getValue(),
                                MessageType.INFO
                        );
                }


                return true;
        }
}
