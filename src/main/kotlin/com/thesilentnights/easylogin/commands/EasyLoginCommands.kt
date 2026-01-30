package com.thesilentnights.easylogin.commands

import com.mojang.brigadier.CommandDispatcher
import com.thesilentnights.easylogin.commands.admin.AdminCommands
import com.thesilentnights.easylogin.commands.common.CommonCommands
import net.minecraft.commands.CommandSourceStack

interface EasyLoginCommands {
    companion object {
        fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
            AdminCommands.register(dispatcher)
            CommonCommands.registerCommands(dispatcher)
        }
    }
}
