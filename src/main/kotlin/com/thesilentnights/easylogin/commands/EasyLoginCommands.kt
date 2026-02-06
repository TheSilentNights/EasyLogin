package com.thesilentnights.easylogin.commands

import com.mojang.brigadier.CommandDispatcher
import com.thesilentnights.easylogin.commands.admin.*
import com.thesilentnights.easylogin.commands.common.*
import net.minecraft.commands.CommandSourceStack
import org.koin.core.context.GlobalContext

object EasyLoginCommands {
    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        val koin = GlobalContext.get()
        //common
        with(dispatcher){
            register(koin.get<Login>().command)
            register(koin.get<Registrar>().command)
            register(koin.get<Logout>().command)
            register(koin.get<ChangePassword>().command)
            register(koin.get<Email>().command)
            register(koin.get<Recover>().command)
        }

        //admin
        with(dispatcher){
            register(koin.get<ByPass>().getCommand(AdminCommands.mainNode))
            register(koin.get<TeleportToOfflinePlayer>().getCommand(AdminCommands.mainNode))
            register(koin.get<EmailTest>().getCommand(AdminCommands.mainNode))
            register(koin.get<PlayerInfoCommands>().getCommand(AdminCommands.mainNode))
        }
    }

}
