package cn.thesilentnights.easylogin.services;

import cn.thesilentnights.easylogin.utils.MessageSender.MessageType;
import cn.thesilentnights.easylogin.utils.MessageSender;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Arrays;
import java.util.List;
import net.neoforged.neoforge.event.CommandEvent;

public class CommandRejectionService {

        private static final List<String> bypassList = Arrays.asList(
                "login",
                "register"
        );

        public static void handleRejection(CommandEvent event) throws CommandSyntaxException {
                var context = event.getParseResults().getContext();
                if (context.getSource().getEntity() == null) {
                        return;
                }

                var playerOrException = context.getSource().getPlayerOrException();
                if (ActionCheckService.shouldCancelEvent(playerOrException) &&
                        !bypassList.contains(event.getParseResults().getContext().getNodes().getFirst().getNode().getName())) {
                        event.setCanceled(true);
                        MessageSender.sendMessage(
                                context,
                                "command.rejected",
                                MessageType.ERROR
                        );
                }
        }
}
