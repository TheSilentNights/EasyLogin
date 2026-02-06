package com.thesilentnights.easylogin.service

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.thesilentnights.easylogin.events.EasyLoginEvents
import com.thesilentnights.easylogin.pojo.PlayerAccount
import com.thesilentnights.easylogin.repo.PlayerCache
import com.thesilentnights.easylogin.repo.PlayerSessionCache
import com.thesilentnights.easylogin.utils.TextUtil
import net.minecraft.ChatFormatting
import net.minecraft.commands.CommandSourceStack
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.effect.MobEffects
import net.minecraftforge.common.MinecraftForge
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import java.util.*

class LoginService : KoinComponent {
    val log: Logger = LogManager.getLogger(LoginService::class)
    val accountService: AccountService = get()

    @Throws(CommandSyntaxException::class)
    fun login(context: CommandContext<CommandSourceStack>): Boolean {
        if (!accountService.hasAccount(context.getSource().playerOrException.getUUID())) {
            context.getSource().sendFailure(TextUtil.createText(ChatFormatting.RED, "you haven't registered"))
            return true
        }

        val serverPlayer: ServerPlayer = context.getSource().playerOrException
        if (PlayerCache.hasAccount(serverPlayer.getUUID())) {
            context.getSource().sendFailure(
                TranslatableComponent("account.already_loggedin", serverPlayer.gameProfile.name)
                    .withStyle(ChatFormatting.RED).withStyle(ChatFormatting.BOLD)
            )
        }

        val password: String = StringArgumentType.getString(context, "password")
        val account: Optional<PlayerAccount> = accountService.getAccount(serverPlayer.getUUID())

        if (account.isPresent) {
            if (account.get().password == password) {
                context.getSource().sendSuccess(
                    TextUtil.createText(
                        ChatFormatting.GREEN, "commands.login.success",
                        context.getSource().playerOrException.displayName.string
                    ), false
                )
                removeLimit(account.get(), serverPlayer)
                return true
            }
        } else {
            context.getSource().sendFailure(
                TextUtil.createText(
                    ChatFormatting.RED, "commands.login.failure",
                    context.getSource().playerOrException.displayName.string
                )
            )
            return false
        }

        return false
    }

    private fun removeLimit(account: PlayerAccount, serverPlayer: ServerPlayer) {
        PlayerCache.addAccount(account)
        TaskService.cancelPlayer(serverPlayer.uuid)
        serverPlayer.removeEffect(MobEffects.BLINDNESS)
    }

    @Throws(CommandSyntaxException::class)
    fun register(context: CommandContext<CommandSourceStack>): Boolean {
        val serverPlayer: ServerPlayer = context.getSource().playerOrException
        val password: String = StringArgumentType.getString(context, "password")
        val repeat: String = StringArgumentType.getString(context, "repeat")

        if (password != repeat) {
            context.getSource().sendFailure(
                TranslatableComponent("commands.password.confirm.failure")
                    .withStyle(ChatFormatting.RED).withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.RED)
            )
            return false
        }

        accountService.updateAccount(
            PlayerAccount(
                uuid = serverPlayer.uuid,
                username = serverPlayer.gameProfile.name,
                password = password,
                lastLoginIp = serverPlayer.ipAddress,
                lastLoginX = serverPlayer.x,
                lastLoginY = serverPlayer.y,
                lastLoginZ = serverPlayer.z,
                lastLoginWorld = serverPlayer.getLevel().dimension().location().getNamespace(),
                email = null,
                loginTimestamp = System.currentTimeMillis(),
            )
        )

        val auth: Optional<PlayerAccount> = accountService.getAccount(serverPlayer.getUUID())
        if (auth.isEmpty) {
            log.atError().log("sql error found in registering player")
            return false
        } else {
            context.getSource().sendSuccess(
                TranslatableComponent("commands.login.success").withStyle(ChatFormatting.GREEN)
                    .withStyle(ChatFormatting.BOLD), false
            )
            MinecraftForge.EVENT_BUS.post(EasyLoginEvents.PlayerLoginEvent(serverPlayer, auth.get()))
            return true
        }
    }

    /**
     * 
     * @param serverPlayer targetPlayer
     */
    fun logoutPlayer(serverPlayer: ServerPlayer) {
        val account: Optional<PlayerAccount> = PlayerCache.getAccount(serverPlayer.getUUID())
        if (account.isPresent) {
            val playerAccount: PlayerAccount = account.get()
            playerAccount.lastLoginIp = serverPlayer.ipAddress
            playerAccount.lastLoginWorld = serverPlayer.getLevel().dimension().location().getNamespace()
            playerAccount.lastLoginX = serverPlayer.x
            playerAccount.lastLoginY = serverPlayer.y
            playerAccount.lastLoginZ = serverPlayer.z
            playerAccount.loginTimestamp = System.currentTimeMillis()
            accountService.updateAccount(playerAccount)
            // push events
            PlayerCache.dropAccount(serverPlayer.uuid, true)
        }
    }


    fun isLoggedIn(key: UUID): Boolean {
        return PlayerCache.hasAccount(key)
    }

    fun reLogFromCache(serverPlayer: ServerPlayer): Boolean {
        return PlayerSessionCache.hasSession(serverPlayer.getUUID()) && PlayerSessionCache.getSession(serverPlayer.getUUID())!!
            .account.lastLoginIp == serverPlayer.ipAddress
    }
}
