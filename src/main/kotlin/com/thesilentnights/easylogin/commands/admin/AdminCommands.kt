package com.thesilentnights.easylogin.commands.admin

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.thesilentnights.easylogin.service.LoginService
import net.minecraft.commands.CommandSourceStack
import net.minecraft.world.entity.Entity
import org.koin.core.component.KoinComponent

interface AdminCommands : KoinComponent {
    fun getCommand(mainNode: LiteralArgumentBuilder<CommandSourceStack>): LiteralArgumentBuilder<CommandSourceStack>

    companion object{
        fun register(loginService: LoginService,dispatcher: CommandDispatcher<CommandSourceStack>) {
            val mainNode: LiteralArgumentBuilder<CommandSourceStack> =
                net.minecraft.commands.Commands.literal("easylogin")
                    .requires({ commandSourceStack: CommandSourceStack ->
                        val entity: Entity? = commandSourceStack.entity
                        if (entity == null) {
                            //execute from terminal
                            return@requires commandSourceStack.hasPermission(4)
                        } else {
                            return@requires commandSourceStack.hasPermission(4) && loginService.isLoggedIn(entity.getUUID())
                        }
                    })
        }
    }


}

