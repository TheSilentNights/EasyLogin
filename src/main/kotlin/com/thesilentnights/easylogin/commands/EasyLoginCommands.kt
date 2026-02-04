package com.thesilentnights.easylogin.commands

import com.mojang.brigadier.CommandDispatcher
import com.thesilentnights.easylogin.commands.admin.*
import com.thesilentnights.easylogin.commands.common.*
import net.minecraft.commands.CommandSourceStack
import org.koin.core.component.KoinComponent
import org.koin.core.context.GlobalContext

class EasyLoginCommands: KoinComponent {
    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        val koin = GlobalContext.get()
        //common
        with(dispatcher){
            register(koin.get<LoginCommands>().command)
            register(koin.get<RegistrarCommands>().command)
            register(koin.get<LogoutCommand>().command)
            register(koin.get<ChangePassword>().command)
            register(koin.get<Email>().command)
            register(koin.get<RecoverCommands>().command)
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
