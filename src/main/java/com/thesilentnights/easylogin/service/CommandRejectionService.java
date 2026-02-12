package com.thesilentnights.easylogin.service;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.thesilentnights.easylogin.utils.TextUtil;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.event.CommandEvent;

import java.util.Arrays;
import java.util.List;

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
                !bypassList.contains(event.getParseResults().getContext().getNodes().get(0).getNode().getName())) {
            event.setCanceled(true);
            playerOrException.displayClientMessage(
                    TextUtil.serialize(TextUtil.FormatType.FAILURE, new TranslatableComponent("command.rejected")),
                    false
            );
        }
    }
}