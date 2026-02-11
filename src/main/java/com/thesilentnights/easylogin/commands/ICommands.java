package com.thesilentnights.easylogin.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;

public interface ICommands {
    void register(CommandDispatcher<CommandSourceStack> dispatcher) throws CommandSyntaxException;
}
