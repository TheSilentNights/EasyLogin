package com.thesilentnights.easylogin.compact

import net.minecraft.world.entity.LivingEntity

interface NpcCompactor {
    fun isNpc(entity: LivingEntity): Boolean
}