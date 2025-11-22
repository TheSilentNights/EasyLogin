package com.thesilentnights.commands.admin;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.thesilentnights.pojo.PlayerAccount;
import com.thesilentnights.service.PlayerLoginService;
import com.thesilentnights.utils.TextUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.UUID;

public class PlayerInfoCommands implements AdminCommands {
    @Autowired
    private PlayerLoginService loginAuth;

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCommand(LiteralArgumentBuilder<CommandSourceStack> mainNode) {
        return mainNode
                .then(Commands.literal("playerinfo")
                        .then(Commands.argument("playerName", StringArgumentType.string())
                                .executes(context -> {
                                    Optional<PlayerAccount> account = loginAuth.getAccount(UUID.fromString(StringArgumentType.getString(context, "playerName")));
                                    if (account.isPresent()){
                                        context.getSource().sendFailure(TextUtil.createText(account.get().toString()));
                                        return 1;
                                    }
                                    return 0;
                                }))
                );
    }
}
