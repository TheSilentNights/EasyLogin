package com.thesilentnights.easylogin.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.thesilentnights.easylogin.commands.admin.ByPass;
import com.thesilentnights.easylogin.commands.admin.EmailTest;
import com.thesilentnights.easylogin.commands.admin.PlayerInfoCommands;
import com.thesilentnights.easylogin.commands.admin.TeleportToOfflinePlayer;
import com.thesilentnights.easylogin.commands.common.*;
import net.minecraft.commands.CommandSourceStack;

public class EasyLoginCommands {
    private final Login login;
    private final Registrar registrar;
    private final Logout logout;
    private final Recover recover;
    private final ByPass byPass;
    private final TeleportToOfflinePlayer teleportToOfflinePlayer;
    private final EmailTest emailTest;
    private final PlayerInfoCommands playerInfoCommands;
    private final ChangePassword changePassword;


    public EasyLoginCommands(Login login, Registrar registrar, Logout logout, ChangePassword changePassword, Recover recover, ByPass byPass, TeleportToOfflinePlayer teleportToOfflinePlayer, EmailTest emailTest, PlayerInfoCommands playerInfoCommands) {
        this.login = login;
        this.registrar = registrar;
        this.logout = logout;
        this.changePassword = changePassword;
        this.recover = recover;
        this.byPass = byPass;
        this.teleportToOfflinePlayer = teleportToOfflinePlayer;
        this.emailTest = emailTest;
        this.playerInfoCommands = playerInfoCommands;
    }

    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {

        // Common commands
        dispatcher.register(login.getCommand());
        dispatcher.register(registrar.getCommand());
        dispatcher.register(logout.getCommand());
        dispatcher.register(recover.getCommand());
        dispatcher.register(changePassword.getCommand());

        // Admin commands
        dispatcher.register(byPass.getCommand());
        dispatcher.register(emailTest.getCommand());
        dispatcher.register(teleportToOfflinePlayer.getCommand());
        dispatcher.register(playerInfoCommands.getCommand());
    }
}