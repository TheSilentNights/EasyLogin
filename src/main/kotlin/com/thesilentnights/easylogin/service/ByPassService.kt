package com.thesilentnights.easylogin.service

import java.util.*

object ByPassService {
    val list: MutableList<UUID> = ArrayList()

    fun addBypass(uuid: UUID) {
        list.add(uuid)
    }

    fun isBypassed(uuid: UUID): Boolean {
        return list.contains(uuid)
    }

    fun removeBypass(uuid: UUID) {
        list.remove(uuid)
    }
}