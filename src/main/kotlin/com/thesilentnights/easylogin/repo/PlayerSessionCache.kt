package com.thesilentnights.easylogin.repo

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import com.thesilentnights.easylogin.pojo.PlayerAccount
import com.thesilentnights.easylogin.pojo.PlayerSession
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

import java.time.Duration
import java.util.*

object PlayerSessionCache {
    private val log: Logger = LogManager.getLogger(PlayerSessionCache::class.java)
    private val sessions: Cache<String, PlayerSession?> = Caffeine.newBuilder().maximumSize(60).expireAfterAccess(
        Duration.ofSeconds((5 * 60).toLong())
    ).build<String, PlayerSession>()

    fun scheduleDrop(account: PlayerAccount) {
        PlayerSessionCache.log.info(account.toString())
        sessions.put(account.uuid.toString(), PlayerSession(account))
    }

    fun hasSession(uuid: UUID): Boolean {
        return sessions.getIfPresent(uuid.toString()) != null
    }

    fun getSession(key: UUID): PlayerSession? {
        return sessions.getIfPresent(key.toString())
    }
}
