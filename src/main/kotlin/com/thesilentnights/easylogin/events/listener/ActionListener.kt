package com.thesilentnights.easylogin.events.listener

import com.thesilentnights.easylogin.service.ActionCheckService
import net.minecraft.server.level.ServerPlayer
import net.minecraftforge.event.entity.living.LivingAttackEvent
import net.minecraftforge.event.entity.living.LivingHurtEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import thedarkcolour.kotlinforforge.forge.FORGE_BUS

class ActionListener {
    init {
        FORGE_BUS.addListener (::onPlayerInteract)

    }
    fun onPlayerInteract(event: PlayerInteractEvent){
        if (ActionCheckService.shouldCancelEvent(event.player)){
            event.isCanceled = true
        }
    }

    fun onPlayerAttack(event: LivingAttackEvent){
        if (event.entityLiving is ServerPlayer && ActionCheckService.shouldCancelEvent(event.entityLiving)){
            event.isCanceled = true
        }
    }

    fun onPlayerHurt(event: LivingHurtEvent){
        if (event.entityLiving is ServerPlayer && ActionCheckService.shouldCancelEvent(event.entityLiving)){
            event.isCanceled = true
        }
    }
}