package com.thesilentnights.easylogin.commands.admin;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.thesilentnights.easylogin.pojo.PlayerAccount;
import com.thesilentnights.easylogin.service.AccountService;
import com.thesilentnights.easylogin.service.LoginService;
import com.thesilentnights.easylogin.utils.TextUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class TeleportToOfflinePlayer extends AdminCommands {
    private final AccountService accountService;

    public TeleportToOfflinePlayer(AccountService accountService, LoginService loginService) {
        super(loginService);
        this.accountService = accountService;
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return super.MAIN_NODE
                .then(Commands.literal("teleport")
                        .then(Commands.argument("targetName", StringArgumentType.string())
                                .executes(context -> {
                                    ServerPlayer serverPlayer = context.getSource().getPlayerOrException();
                                    String targetName = StringArgumentType.getString(context, "targetName");

                                    if (accountService.hasAccount(targetName) &&
                                            context.getSource().getServer().getPlayerList().getPlayerByName(targetName) == null) {

                                        Optional<PlayerAccount> account = accountService.getAccount(targetName);
                                        if (account.isPresent()) {
                                            PlayerAccount playerAccount = account.get();
                                            serverPlayer.teleportTo(
                                                    playerAccount.getLastLoginX(),
                                                    playerAccount.getLastLoginY(),
                                                    playerAccount.getLastLoginZ()
                                            );
                                            return 1;
                                        } else {
                                            context.getSource().sendFailure(
                                                    TextUtil.createBold(ChatFormatting.RED, "player does not exists")
                                            );
                                            return 0;
                                        }
                                    }

                                    context.getSource().sendFailure(
                                            TextUtil.createBold(ChatFormatting.RED, "player does not exists")
                                    );
                                    return 0;
                                })
                        )
                );
    }
}