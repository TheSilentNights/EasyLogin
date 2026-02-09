package com.thesilentnights.easylogin.commands.common;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.thesilentnights.easylogin.service.LoginService;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class Login implements CommonCommands {

    private final LoginService loginService;

    public Login(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return Commands.literal("login")
                .then(Commands.argument("password", StringArgumentType.string())
                        .executes(context -> {
                            boolean success = loginService.login(context);
                            return success ? 1 : 0;
                        }));
    }
}