package com.thesilentnights.easylogin.commands

import com.mojang.brigadier.CommandDispatcher
import com.thesilentnights.easylogin.commands.admin.AdminCommands
import com.thesilentnights.easylogin.commands.common.CommonCommands
import net.minecraft.commands.CommandSourceStack
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class EasyLoginCommands : KoinComponent {
    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        AdminCommands.register(get(), dispatcher)
        CommonCommands.registerCommands(dispatcher)
    }

}
