package com.thesilentnights.easylogin.commands

import com.mojang.brigadier.CommandDispatcher
import com.thesilentnights.easylogin.commands.server.admin.*
import com.thesilentnights.easylogin.commands.server.common.*
import net.minecraft.commands.CommandSourceStack

object EasyLoginCommands {
    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        //common
        with(dispatcher) {
            register(Login().command)
            register(Registrar().command)
            register(ChangePassword().command)
            register(Email().command)
            register(Logout().command)
            register(Recover().command)
        }

        //admin
        with(dispatcher) {
            with(AdminCommands) {
                register(ByPass().getCommand(mainNode))
                register(PlayerInfoCommands().getCommand(mainNode))
                register(TeleportToOfflinePlayer().getCommand(mainNode))
                register(EmailTest().getCommand(mainNode))
            }
        }
    }

}
