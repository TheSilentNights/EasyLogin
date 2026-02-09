package com.thesilentnights.easylogin.service

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.thesilentnights.easylogin.pojo.PlayerAccount
import com.thesilentnights.easylogin.repo.PlayerCache
import com.thesilentnights.easylogin.repo.PlayerSessionCache
import com.thesilentnights.easylogin.utils.TextUtil
import com.thesilentnights.easylogin.utils.logError
import net.minecraft.ChatFormatting
import net.minecraft.commands.CommandSourceStack
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.effect.MobEffects
import java.sql.SQLException
import java.util.*

object LoginService {

    @Throws(CommandSyntaxException::class)
    fun login(context: CommandContext<CommandSourceStack>): Boolean {
        if (!AccountService.hasAccount(context.getSource().playerOrException.getUUID())) {
            context.getSource().sendFailure(TextUtil.createText(ChatFormatting.RED, "you haven't registered"))
            return true
        }

        val serverPlayer: ServerPlayer = context.getSource().playerOrException
        if (PlayerCache.hasAccount(serverPlayer.getUUID())) {
            context.getSource().sendFailure(
                TranslatableComponent("account.already_loggedin", serverPlayer.gameProfile.name).apply {
                    withStyle(ChatFormatting.GREEN)
                    withStyle(ChatFormatting.BOLD)
                }
            )
        }

        val password: String = StringArgumentType.getString(context, "password")
        val account: Optional<PlayerAccount> = AccountService.getAccount(serverPlayer.getUUID())

        if (account.isPresent) {
            if (account.get().password == password) {
                context.getSource().sendSuccess(
                    TranslatableComponent("commands.login.success", serverPlayer.displayName.string).apply {
                        withStyle(ChatFormatting.GREEN)
                        withStyle(ChatFormatting.BOLD)
                    }, false
                )
                removeLimit(account.get(), serverPlayer)
                return true
            }
        } else {
            context.getSource().sendFailure(
                TranslatableComponent("commands.login.failure").apply {
                    withStyle(ChatFormatting.RED)
                    withStyle(ChatFormatting.BOLD)
                }
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

    /***
     * @param  account  the account to be logged in
     * @param  serverPlayer  the player to be logged in
     *
     * this function is called to force a player to log in
     */
    fun systemLogin(account: PlayerAccount, serverPlayer: ServerPlayer) {
        removeLimit(account, serverPlayer)
    }

    @Throws(CommandSyntaxException::class)
    fun register(context: CommandContext<CommandSourceStack>): Boolean {
        val serverPlayer: ServerPlayer = context.getSource().playerOrException
        val password: String = StringArgumentType.getString(context, "password")
        val repeat: String = StringArgumentType.getString(context, "repeat")

        if (password != repeat) {
            context.getSource().sendFailure(
                TranslatableComponent("commands.password.confirm.failure").apply {
                    withStyle(ChatFormatting.RED)
                    withStyle(ChatFormatting.BOLD)
                }
            )
            return false
        }

        AccountService.updateAccount(
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

        val auth: Optional<PlayerAccount> = AccountService.getAccount(serverPlayer.getUUID())
        if (auth.isEmpty) {
            logError(LoginService::class, "sql error found in registering player", SQLException())
            return false
        } else {
            context.getSource().sendSuccess(
                TranslatableComponent("commands.login.success").apply {
                    withStyle(ChatFormatting.GREEN)
                    withStyle(ChatFormatting.BOLD)
                }, false
            )
            removeLimit(auth.get(), serverPlayer)
            return true
        }
    }

    /**
     * 
     * @param serverPlayer targetPlayer
     *
     * if the player is bypassed by the bypass service, the logout process will be skipped through account.isPresent
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
            AccountService.updateAccount(playerAccount)
            // push events
            PlayerCache.dropAccount(serverPlayer.uuid, true)
        }
    }


    fun isLoggedIn(key: UUID): Boolean {
        return PlayerCache.hasAccount(key)
    }

    fun reLogFromCache(serverPlayer: ServerPlayer): Boolean {
        if (!PlayerSessionCache.hasSession(serverPlayer.uuid)) {
            return false
        }

        val account: PlayerAccount = PlayerSessionCache.getSession(serverPlayer.uuid)!!.account
        removeLimit(account, serverPlayer)
        return true
    }
}
