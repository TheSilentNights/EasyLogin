package cn.thesilentnights.easylogin.services.commands;

import cn.thesilentnights.easylogin.services.action.ActionCheckService;
import cn.thesilentnights.easylogin.utils.MessageSender;
import cn.thesilentnights.easylogin.utils.MessageSender.MessageType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.neoforged.neoforge.event.CommandEvent;

import java.util.Arrays;
import java.util.List;

public class CommandRejectionServiceImpl implements CommandRejectionService {

        private final ActionCheckService actionCheckService;
        private final List<String> bypassList = Arrays.asList(
                "login",
                "register",
                "changepassword"
        );

        public CommandRejectionServiceImpl(ActionCheckService actionCheckService) {
                this.actionCheckService = actionCheckService;
        }

        @Override
        public void handleRejection(CommandEvent event) throws CommandSyntaxException {
                var context = event.getParseResults().getContext();
                if (context.getSource().getEntity() == null) {
                        return;
                }

                var playerOrException = context.getSource().getPlayerOrException();
                if (actionCheckService.shouldCancelEvent(playerOrException) &&
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
