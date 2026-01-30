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
import net.minecraftforge.common.MinecraftForge
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.forge.MOD_BUS

object LoginService {
    val log: Logger = LogManager.getLogger(LoginService.javaClass)

    @Throws(CommandSyntaxException::class)
    fun login(context: CommandContext<CommandSourceStack>): Boolean {
        if (!AccountService.hasAccount(context.getSource().playerOrException.getUUID())) {
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
        val account: java.util.Optional<PlayerAccount> = AccountService.getAccount(serverPlayer.getUUID())

        if (account.isPresent) {
            if (account.get().password == password) {
                context.getSource().sendSuccess(
                    TextUtil.createText(
                        ChatFormatting.GREEN, "commands.login.success",
                        context.getSource().playerOrException.displayName.string
                    ), false
                )
                MOD_BUS.post(EasyLoginEvents.PlayerLoginEvent(serverPlayer, account.get()))
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

    @Throws(CommandSyntaxException::class)
    fun register(context: CommandContext<CommandSourceStack>): Boolean {
        val serverPlayer: ServerPlayer = context.getSource().getPlayerOrException()
        val password: kotlin.String = StringArgumentType.getString(context, "password")
        val repeat: kotlin.String = StringArgumentType.getString(context, "repeat")

        if (password != repeat) {
            context.getSource().sendFailure(
                TranslatableComponent("commands.password.confirm.failure")
                    .withStyle(ChatFormatting.RED).withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.RED)
            )
            return false
        }

        AccountService.updateAccount(
            PlayerAccount(
                uuid = serverPlayer.uuid,
                username = serverPlayer.gameProfile.name,
                password = password,
                lastlogin_ip = serverPlayer.ipAddress,
                serverPlayer.x,
                serverPlayer.y,
                serverPlayer.z,
                lastlogin_world = serverPlayer.getLevel().dimension().location().getNamespace(),
                email = null,
                login_timstamp = System.currentTimeMillis(),
            )
        )

        val auth: java.util.Optional<PlayerAccount> = AccountService.getAccount(serverPlayer.getUUID())
        if (auth.isEmpty()) {
            log.atError().log("sql error found in registering player")
            return false
        } else {
            context.getSource().sendSuccess(
                TranslatableComponent("commands.login.success").withStyle(ChatFormatting.GREEN)
                    .withStyle(ChatFormatting.BOLD), false
            )

            return true
        }
    }

    /**
     * 
     * @param serverPlayer targetPlayer
     */
    fun logoutPlayer(serverPlayer: ServerPlayer) {
        val account: java.util.Optional<PlayerAccount> = PlayerCache.getAccount(serverPlayer.getUUID())
        if (account.isPresent()) {
            val playerAccount: PlayerAccount = account.get()
            playerAccount.lastlogin_ip = serverPlayer.ipAddress
            playerAccount.lastlogin_world = serverPlayer.getLevel().dimension().location().getNamespace()
            playerAccount.lastlogin_x = serverPlayer.x
            playerAccount.lastlogin_y = serverPlayer.y
            playerAccount.lastlogin_z = serverPlayer.z
            playerAccount.login_timstamp = System.currentTimeMillis()
            AccountService.updateAccount(playerAccount)
            // push events
            MinecraftForge.EVENT_BUS.post(EasyLoginEvents.PlayerLogoutEvent(serverPlayer, playerAccount))
        }
    }

    @JvmStatic
    fun isLoggedIn(key: java.util.UUID?): Boolean {
        return PlayerCache.hasAccount(key)
    }

    fun reLogFromCache(serverPlayer: ServerPlayer): Boolean {
        return PlayerSessionCache.hasSession(serverPlayer.getUUID()) && PlayerSessionCache.getSession(serverPlayer.getUUID())!!
            .account.lastlogin_ip == serverPlayer.ipAddress
    }
}
