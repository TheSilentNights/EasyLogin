package com.thesilentnights.easylogin.service

import com.thesilentnights.easylogin.repo.PlayerCache.hasAccount
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.LivingEntity
import java.util.*

object ActionCheckService {
    fun shouldCancelEvent(entity: LivingEntity?): Boolean {
        if (entity is ServerPlayer) {
            return !isLoggedIn(entity.getUUID())
        }
        return false
    }

    fun isLoggedIn(uuid: UUID): Boolean {
        return hasAccount(uuid)
    }
}
