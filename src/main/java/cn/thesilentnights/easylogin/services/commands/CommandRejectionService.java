package cn.thesilentnights.easylogin.services.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.neoforged.neoforge.event.CommandEvent;

public interface CommandRejectionService {
    void handleRejection(CommandEvent event) throws CommandSyntaxException;
}
