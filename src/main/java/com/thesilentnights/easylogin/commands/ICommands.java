package com.thesilentnights.easylogin.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;

public interface ICommands {
    void register(CommandDispatcher<CommandSourceStack> dispatcher);
}
