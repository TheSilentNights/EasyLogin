package com.thesilentnights.easylogin.service;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.event.CommandEvent;

import java.util.Arrays;
import java.util.List;

public class CommandRejectionService {

    private final List<String> bypassList = Arrays.asList(
            "login",
            "register"
    );

    public void handleRejection(CommandEvent event) throws CommandSyntaxException {
        var context = event.getParseResults().getContext();
        if (context.getSource().getEntity() == null) {
            return;
        }

        var playerOrException = context.getSource().getPlayerOrException();
        if (ActionCheckService.shouldCancelEvent(playerOrException) &&
                !bypassList.contains(event.getParseResults().getContext().getNodes().get(0).getNode().getName())) {
            event.setCanceled(true);
            playerOrException.displayClientMessage(
                    new TranslatableComponent("command.rejected")
                            .withStyle(ChatFormatting.RED)
                            .withStyle(ChatFormatting.BOLD),
                    false
            );
        }
    }
}