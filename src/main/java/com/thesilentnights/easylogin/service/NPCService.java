package com.thesilentnights.easylogin.service

import com.thesilentnights.easylogin.compact.CurtainCompact
import com.thesilentnights.easylogin.compact.NpcCompactor
import net.minecraft.world.entity.LivingEntity
import net.minecraftforge.fml.ModList

object NPCService {
    val checkList: MutableList<NpcCompactor> = ArrayList();

    init{
        if (ModList.get().isLoaded("curtain")){
            checkList.add(CurtainCompact)
        }
    }
    fun isNPC(entity: LivingEntity): Boolean {
        checkList.ifEmpty { return false }

        return checkList.any { compactor -> compactor.isNpc(entity) }
    }
}