package com.thesilentnights.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.thesilentnights.exception.AlreadyLoggedInException;
import com.thesilentnights.service.PlayerLoginAuth;
import com.thesilentnights.task.TickTimerManager;
import com.thesilentnights.utils.TextUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class LoginCommands implements ICommands{
    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return Commands.literal("login").then(Commands.argument("password", StringArgumentType.string()).executes(context->{
            //if not registered
            if (!PlayerLoginAuth.hasAccount(context.getSource().getPlayerOrException().getGameProfile().getName())){
                context.getSource().sendFailure(TextUtil.createText(ChatFormatting.RED,"you haven't registered"));
                return 0;
            }

            boolean flag;
            try{
                flag = PlayerLoginAuth.authPlayerWithPwd(context.getSource().getPlayerOrException(), StringArgumentType.getString(context, "password"));
            }
            catch (AlreadyLoggedInException e){
                context.getSource().sendFailure(TextUtil.createText(ChatFormatting.RED,e.getMessage()));
                return 0;
            }

            if (flag){
                context.getSource().sendSuccess(TextUtil.createText(ChatFormatting.GREEN,"commands.login.success", context.getSource().getPlayerOrException().getDisplayName().getString()), false);
                TickTimerManager.cancel(context.getSource().getPlayerOrException().getUUID());
                return 1;
            }
            else{
                context.getSource().sendFailure(TextUtil.createText(ChatFormatting.RED,"commands.login.failure", context.getSource().getPlayerOrException().getDisplayName().getString()));
                return 0;
            }
        }));
    }
}
