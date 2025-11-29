package com.thesilentnights.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.thesilentnights.commands.admin.AdminCommands;
import com.thesilentnights.commands.common.CommonCommands;
import net.minecraft.commands.CommandSourceStack;

public interface EasyLoginCommands {
    static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        AdminCommands.register(dispatcher);
        CommonCommands.registerCommands(dispatcher);
    }
}
