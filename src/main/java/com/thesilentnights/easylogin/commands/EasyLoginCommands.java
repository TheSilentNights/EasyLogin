package com.thesilentnights.easylogin.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.thesilentnights.easylogin.commands.admin.ByPass;
import com.thesilentnights.easylogin.commands.admin.EmailTest;
import com.thesilentnights.easylogin.commands.admin.PlayerInfoCommands;
import com.thesilentnights.easylogin.commands.admin.TeleportToOfflinePlayer;
import com.thesilentnights.easylogin.commands.common.*;
import net.minecraft.commands.CommandSourceStack;

public class EasyLoginCommands {
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {

        // Common commands
        dispatcher.register(new Login().getCommand());
        dispatcher.register(new Registrar().getCommand());
        dispatcher.register(new Recover().getCommand());
        dispatcher.register(new ChangePassword().getCommand());

        // Admin commands
        dispatcher.register(new ByPass().getCommand());
        dispatcher.register(new EmailTest().getCommand());
        dispatcher.register(new TeleportToOfflinePlayer().getCommand());
        dispatcher.register(new PlayerInfoCommands().getCommand());
    }
}