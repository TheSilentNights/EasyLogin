package com.thesilentnights.easylogin.events

import com.thesilentnights.easylogin.pojo.PlayerAccount
import net.minecraft.server.level.ServerPlayer
import net.minecraftforge.eventbus.api.Event

class EasyLoginEvents {
    class PlayerLoginEvent(var serverPlayer: ServerPlayer, var account: PlayerAccount) : Event()

    class PlayerLogoutEvent(var serverPlayer: ServerPlayer, var account: PlayerAccount) : Event()
}
