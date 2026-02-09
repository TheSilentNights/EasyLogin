package com.thesilentnights.easylogin.commands.common;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;

public interface CommonCommands {
    LiteralArgumentBuilder<CommandSourceStack> getCommand();
}