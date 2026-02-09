package com.thesilentnights.easylogin.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.thesilentnights.easylogin.commands.common.Login;
import net.minecraft.commands.CommandSourceStack;


public class EasyLoginCommands {
    private final Login login;


    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {

        // Common commands
        dispatcher.register();
        dispatcher.register(koin.<Registrar>get().getCommand());
        dispatcher.register(koin.<Logout>get().getCommand());
        dispatcher.register(koin.<ChangePassword>get().getCommand());
        dispatcher.register(koin.<Email>get().getCommand());
        dispatcher.register(koin.<Recover>get().getCommand());

        // Admin commands
        dispatcher.register(koin.<ByPass>get().getCommand(AdminCommands.mainNode));
        dispatcher.register(koin.<TeleportToOfflinePlayer>get().getCommand(AdminCommands.mainNode));
        dispatcher.register(koin.<EmailTest>get().getCommand(AdminCommands.mainNode));
        dispatcher.register(koin.<PlayerInfoCommands>get().getCommand(AdminCommands.mainNode));
    }
}