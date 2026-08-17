package cn.thesilentnights.easylogin.services.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;

public interface PlayerInfoService {
        boolean handle(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException;
}
