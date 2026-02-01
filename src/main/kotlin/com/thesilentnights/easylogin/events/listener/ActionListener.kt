package com.thesilentnights.easylogin.events.listener

import com.thesilentnights.easylogin.service.ActionCheckService
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.LivingEntity
import net.minecraftforge.event.entity.living.LivingAttackEvent
import net.minecraftforge.event.entity.living.LivingDropsEvent
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import thedarkcolour.kotlinforforge.forge.FORGE_BUS

class ActionListener {
    init {
        FORGE_BUS.addListener (::onPlayerInteract)
        FORGE_BUS.addListener (::onPlayerAttack)
        FORGE_BUS.addListener (::onPlayerPick)
        FORGE_BUS.addListener (::onPlayerDrop)

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

    fun onPlayerPick(event: PlayerEvent.ItemPickupEvent){
        if (ActionCheckService.shouldCancelEvent(event.player)){
            event.isCanceled = true
        }
    }

    fun onPlayerDrop(event: LivingDropsEvent){
        if (event.entity is LivingEntity && ActionCheckService.shouldCancelEvent(event.entity as LivingEntity)){
            event.isCanceled = true
        }
    }

}