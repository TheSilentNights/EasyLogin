package cn.thesilentnights.easylogin.services.passwords;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.commands.CommandSourceStack;

public interface ChangePasswordService {
    boolean changePassword(CommandContext<CommandSourceStack> context) throws CommandSyntaxException;
    boolean changePasswordAdmin(CommandContext<CommandSourceStack> context) throws CommandSyntaxException;
}
