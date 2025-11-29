package com.thesilentnights.commands.admin;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.thesilentnights.pojo.PlayerAccount;
import com.thesilentnights.service.PlayerLoginService;
import com.thesilentnights.utils.TextUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class TeleportToOfflinePlayer implements AdminCommands{

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCommand(LiteralArgumentBuilder<CommandSourceStack> mainNode) {
        return mainNode
                .then(Commands.literal("teleport")
                        .then(Commands.argument("targetName",StringArgumentType.string())
                                .executes(commandContext -> {
                                    ServerPlayer playerOrException = commandContext.getSource().getPlayerOrException();
                                    String targetName = StringArgumentType.getString(commandContext, "targetName");
                                    //check the player is offline
                                    if (PlayerLoginService.hasAccount(targetName) && commandContext.getSource().getServer().getPlayerList().getPlayerByName(targetName) == null){
                                        Optional<PlayerAccount> account = PlayerLoginService.getAccount(targetName);
                                        if (account.isPresent()){
                                            PlayerAccount playerAccount = account.get();
                                            playerOrException.teleportTo(playerAccount.getLastlogin_x(),playerAccount.getLastlogin_y(),playerAccount.getLastlogin_z());
                                            return 1;
                                        }else{
                                            commandContext.getSource().sendFailure(TextUtil.createBold(ChatFormatting.RED,"player does not exists"));
                                            return 0;
                                        }
                                    }
                                    commandContext.getSource().sendFailure(TextUtil.createBold(ChatFormatting.RED,"player does not exists"));
                                    return 0;
                                })
                        )
                );
    }
}
