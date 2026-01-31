package com.thesilentnights.easylogin.repo

import com.thesilentnights.easylogin.pojo.PlayerAccount
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object PlayerCache {
    private val cacheMap: MutableMap<UUID, PlayerAccount> = ConcurrentHashMap<UUID, PlayerAccount>()

    fun addAccount(account: PlayerAccount) {
        cacheMap[account.uuid] = account
    }

    fun getAccount(uuid: UUID): Optional<PlayerAccount> {
        return Optional.ofNullable(cacheMap[uuid])
    }

    fun hasAccount(uuid: UUID): Boolean {
        return cacheMap.containsKey(uuid)
    }

    fun dropAccount(uuid: UUID, tempDrop: Boolean) {
        if (tempDrop) {
            val uuid1: PlayerAccount = cacheMap.get(uuid)!!
            PlayerSessionCache.scheduleDrop(uuid1)
        }
        cacheMap.remove(uuid)
    }
}
