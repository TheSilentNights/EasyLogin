package com.thesilentnights.easylogin.service

import java.util.*

object TimerService {
    private val entries: MutableMap<String?, Long?> = HashMap<String?, Long?>()

    fun contains(identifier: String?): Boolean {
        return entries[identifier] != null && System.currentTimeMillis() < entries[identifier]!!
    }

    fun cleanExpired() {
        entries.entries.removeIf { entry: MutableMap.MutableEntry<String?, Long?>? -> System.currentTimeMillis() > entry!!.value!! }
    }

    fun generateIdentifier(uuid: UUID?, serviceClass: Class<*>): String {
        return uuid.toString() + "_" + serviceClass.getName()
    }

    fun add(identifier: String?, duration: Long) {
        entries[identifier] = System.currentTimeMillis() + duration
    }

    fun clear() {
        entries.clear()
    }
}
