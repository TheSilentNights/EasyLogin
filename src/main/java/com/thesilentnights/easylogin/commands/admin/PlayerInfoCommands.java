package com.thesilentnights.easylogin.commands.admin;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.thesilentnights.easylogin.pojo.PlayerAccount;
import com.thesilentnights.easylogin.service.AccountService;
import com.thesilentnights.easylogin.service.LoginService;
import com.thesilentnights.easylogin.utils.TextUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import java.util.Optional;
import java.util.UUID;

public class PlayerInfoCommands extends AdminCommands {
    private final AccountService accountService;

    public PlayerInfoCommands(AccountService accountService, LoginService loginService) {
        super(loginService);
        this.accountService = accountService;
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return super.MAIN_NODE
                .then(
                        Commands.literal("playerinfo")
                                .then(
                                        Commands.argument("playerName", StringArgumentType.string())
                                                .executes((CommandContext<CommandSourceStack> context) -> {
                                                    Optional<PlayerAccount> account = accountService.getAccount(
                                                            UUID.fromString(StringArgumentType.getString(context, "playerName"))
                                                    );
                                                    if (account.isPresent()) {
                                                        context.getSource().sendFailure(TextUtil.createText(account.get().toString()));
                                                        return 1;
                                                    }
                                                    return 0;
                                                })
                                )
                );
    }
}